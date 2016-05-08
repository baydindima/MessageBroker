import com.message_broker.MessageBroker;
import com.message_broker.MessageBrokerImpl;
import com.message_broker.models.Message;
import com.message_broker.models.Subscriber;
import com.message_broker.models.TextMessage;
import com.message_broker.models.Topic;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MessageBrokerImpl}
 */
public class MessageBrokerConcurrentTest {
    /*private static final int THREAD_NUM = 5;
    private static final int MAX_TOPIC_COUNT = 5;
    private Random random = new Random();
    private volatile MessageBroker messageBroker = new MessageBrokerImpl();
    private volatile AtomicIntegerArray messageCount = new AtomicIntegerArray(MAX_TOPIC_COUNT);
    private volatile AtomicIntegerArray subscriberCount = new AtomicIntegerArray(MAX_TOPIC_COUNT);
    private volatile AtomicInteger subscriberId = new AtomicInteger();
    private volatile AtomicInteger totalMessageCount = new AtomicInteger();

    @Test
    public void simpleConcurrentTest() {
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < THREAD_NUM; i++) {
            runnables.add(() -> {
                        for (int j = 0; j < 10; j++) {
                            Subscriber subscriber = new Subscriber(subscriberId.getAndIncrement()) {
                                @Override
                                public void receiveMessage(Topic topic, Message message) {
                                    messageCount.incrementAndGet((int) topic.getId());
                                }
                            };
                            int topicId = random.nextInt(MAX_TOPIC_COUNT);
                            messageBroker.subscribe(subscriber, new Topic("Text", topicId));
                            subscriberCount.incrementAndGet(topicId);
                            Thread.yield();
                        }
                    }
            );
        }
        try {
            assertConcurrent("Simple subscribe", runnables, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int sum = 0;
        for (int i = 0; i < MAX_TOPIC_COUNT; i++) {
            sum += subscriberCount.get(i);
        }
        assertEquals(10 * THREAD_NUM, sum);

        runnables.clear();

        for (int i = 0; i < THREAD_NUM; i++) {
            runnables.add(() -> {
                        for (int j = 0; j < 10; j++) {
                            int topicId = random.nextInt(MAX_TOPIC_COUNT);
                            totalMessageCount.addAndGet(
                                    subscriberCount.get(topicId)
                            );
                            messageBroker.publish(new TextMessage(""), new Topic("Text", topicId));
                            Thread.yield();
                        }
                    }
            );
        }

        try {
            assertConcurrent("Simple publish ", runnables, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sum = 0;
        for (int i = 0; i < MAX_TOPIC_COUNT; i++) {
            sum += messageCount.get(i);
        }
        assertEquals(totalMessageCount.get(), sum);
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
    }*/

}
