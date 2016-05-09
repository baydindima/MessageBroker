package com.message_broker.app;

import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MessageBrokerImpl}
 */
public class MessageBrokerConcurrentTest extends CommonMessageBrokerUtils {
    private static final int THREAD_NUM = 5;


    @Test
    public void simpleConcurrentTest() {
        final int subscribePerThread = 10;

        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < THREAD_NUM; i++) {
            runnables.add(() -> {
                        Topic topic = getTopicFactory().newInstance();
                        for (int j = 0; j < subscribePerThread; j++) {
                            Subscriber subscriber = getSubscriberFactory().newInstance();
                            getMessageBroker().subscribe(subscriber, topic);
                            Thread.yield();
                        }
                    }
            );
        }
        try {
            assertConcurrent("Simple subscribe", runnables, 300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(
                subscribePerThread * THREAD_NUM,
                getMessageBroker().getSubscribers().size()
        );

        assertEquals(
                THREAD_NUM,
                getMessageBroker().getTopics().size()
        );


        Topic[] topics = getMessageBroker().getTopics().toArray(new Topic[0]);
        AtomicInteger threadNum = new AtomicInteger();

        runnables.clear();

        final int messagePerThread = 10;
        for (int i = 0; i < THREAD_NUM; i++) {
            runnables.add(() -> {
                        Topic topic = topics[threadNum.getAndIncrement()];
                        for (int j = 0; j < messagePerThread; j++) {
                            getMessageBroker().publish(getMessageFactory().newInstance(), topic);
                            Thread.yield();
                        }
                    }
            );
        }

        try {
            assertConcurrent("Simple publish ", runnables, 300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Subscriber subscriber : getMessageBroker().getSubscribers()) {
            assertEquals(
                    messagePerThread,
                    subscriber.getSavedMessages().size()
            );
        }
    }

    private static void assertConcurrent(
            final String message,
            final List<? extends Runnable> runnables,
            final int maxTimeoutSeconds) throws InterruptedException {
        final int numThreads = runnables.size();
        final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());
        final ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        try {
            final CountDownLatch allExecutorThreadsReady = new CountDownLatch(numThreads);
            final CountDownLatch afterInitBlocker = new CountDownLatch(1);
            final CountDownLatch allDone = new CountDownLatch(numThreads);
            for (final Runnable submittedTestRunnable : runnables) {
                threadPool.submit(() -> {
                    allExecutorThreadsReady.countDown();
                    try {
                        afterInitBlocker.await();
                        submittedTestRunnable.run();
                    } catch (final Throwable e) {
                        exceptions.add(e);
                    } finally {
                        allDone.countDown();
                    }
                });
            }
            // wait until all threads are ready
            assertTrue(
                    "Timeout initializing threads! "
                            + "Perform long lasting initializations "
                            + "before passing runnables to assertConcurrent",
                    allExecutorThreadsReady.await(runnables.size() * 10, TimeUnit.MILLISECONDS));
            // start all test runners
            afterInitBlocker.countDown();
            assertTrue(
                    String.format("%s timeout! More than %d seconds",
                            message,
                            maxTimeoutSeconds),
                    allDone.await(maxTimeoutSeconds, TimeUnit.SECONDS)
            );
        } finally {
            threadPool.shutdownNow();
        }
        assertTrue(
                String.format("%s failed with exception(s)%s",
                        message,
                        exceptions),
                exceptions.isEmpty()
        );
    }

}
