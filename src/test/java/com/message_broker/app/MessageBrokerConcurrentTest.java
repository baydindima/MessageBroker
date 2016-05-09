package com.message_broker.app;

import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MessageBrokerImpl}
 */
public class MessageBrokerConcurrentTest extends CommonMessageBrokerUtils {
    private static final int THREAD_NUM = 5;
    private static final int MAX_SEC_TO_WAIT = 300;


    @Test
    public void simpleConcurrentTest() {
        final int subscribePerThread = 10;
        final BlockingQueue<Subscriber> subscribers = new LinkedBlockingQueue<>();
        final BlockingQueue<Topic> topics = new LinkedBlockingQueue<>();

        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < THREAD_NUM; i++) {
            runnables.add(() -> {
                        Topic topic = getTopicFactory().newInstance();
                        topics.add(topic);
                        for (int j = 0; j < subscribePerThread; j++) {
                            Subscriber subscriber = getSubscriberFactory().newInstance();
                            subscribers.add(subscriber);
                            getMessageBroker().subscribe(subscriber, topic);
                            Thread.yield();
                        }
                    }
            );
        }
        try {
            assertConcurrent("Simple subscribe", runnables, MAX_SEC_TO_WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        assertTrue(
                subscribePerThread * THREAD_NUM <= getMessageBroker().getSubscribers().size()
        );

        assertTrue(
                THREAD_NUM <= getMessageBroker().getTopics().size()
        );

        runnables.clear();

        final int messagePerThread = 10;
        for (int i = 0; i < THREAD_NUM; i++) {
            runnables.add(() -> {
                        Topic topic = topics.poll();
                        for (int j = 0; j < messagePerThread; j++) {
                            getMessageBroker().publish(getMessageFactory().newInstance(), topic);
                            Thread.yield();
                        }
                    }
            );
        }

        try {
            assertConcurrent("Simple publish ", runnables, MAX_SEC_TO_WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        for (Subscriber subscriber : subscribers) {
            assertEquals(
                    messagePerThread,
                    subscriber.getSavedMessages().size()
            );
        }
    }

    @Test
    public void multiSubscribe() {
        final String text = "multiSubscribe-Subscriber";
        final Topic topic = getTopicFactory().newInstance();


        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < THREAD_NUM; i++) {
            runnables.add(() -> getMessageBroker().subscribe(new Subscriber(text), topic)
            );
        }

        try {
            assertConcurrent("Multi subscribe", runnables, MAX_SEC_TO_WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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
