import com.message_broker.MessageBroker;
import com.message_broker.MessageBrokerImpl;
import com.message_broker.models.Message;
import com.message_broker.models.Subscriber;
import com.message_broker.models.TextMessage;
import com.message_broker.models.Topic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MessageBrokerImpl}
 */

public class MessageBrokerTest {
    /*private MessageBroker messageBroker = new MessageBrokerImpl();
    private Topic topic0 = new Topic("Topic-0", 0);
    private Topic topic1 = new Topic("Topic-1", 1);
    private Topic topic2 = new Topic("Topic-2", 2);

    private final int[] sum1 = {0, 0, 0};
    private final int[] sum2 = {0, 0, 0};
    private Subscriber subscriber0 = new Subscriber(0) {
        @Override
        public void receiveMessage(Topic topic, Message message) {
            TextMessage textMessage = (TextMessage) message;
            if ("0".equals(textMessage.getMessage())) {
                ++sum1[0];
            } else {
                ++sum2[0];
            }
        }
    };

    private Subscriber subscriber1 = new Subscriber(1) {
        @Override
        public void receiveMessage(Topic topic, Message message) {
            TextMessage textMessage = (TextMessage) message;
            if ("0".equals(textMessage.getMessage())) {
                ++sum1[1];
            } else {
                ++sum2[1];
            }
        }
    };

    private Subscriber subscriber2 = new Subscriber(1) {
        @Override
        public void receiveMessage(Topic topic, Message message) {
            TextMessage textMessage = (TextMessage) message;
            if ("0".equals(textMessage.getMessage())) {
                ++sum1[2];
            } else {
                ++sum2[2];
            }
        }
    };

    @Test
    public void simpleTest() {
        messageBroker.subscribe(subscriber0, topic0);
        messageBroker.publish(new TextMessage("0"), topic0);
        assertEquals(1, sum1[0]);
    }

    @Test
    public void doubleSubscribeTest() {
        messageBroker.subscribe(subscriber0, topic0);
        messageBroker.subscribe(subscriber0, topic0);
        messageBroker.publish(new TextMessage("0"), topic0);
        assertEquals(1, sum1[0]);
    }

    @Test
    public void multiSubscribeTest() {
        messageBroker.subscribe(subscriber0, topic0);
        messageBroker.subscribe(subscriber1, topic0);
        messageBroker.subscribe(subscriber2, topic0);
        messageBroker.publish(new TextMessage("0"), topic0);
        assertEquals(1, sum1[0]);
        assertEquals(1, sum1[1]);
        assertEquals(1, sum1[2]);
    }

    @Test
    public void multiPublishTest() {
        messageBroker.subscribe(subscriber0, topic0);
        messageBroker.publish(new TextMessage("0"), topic0);
        messageBroker.publish(new TextMessage("0"), topic0);
        messageBroker.publish(new TextMessage("1"), topic0);

        assertEquals(2, sum1[0]);
        assertEquals(1, sum2[0]);
    }

    @Test
    public void unsubscribeTest() {
        messageBroker.unsubscribe(subscriber0, topic0);
        messageBroker.publish(new TextMessage("0"), topic0);
        assertEquals(0, sum1[0]);

        messageBroker.subscribe(subscriber0, topic0);
        messageBroker.publish(new TextMessage("0"), topic0);
        messageBroker.unsubscribe(subscriber0, topic0);
        messageBroker.publish(new TextMessage("0"), topic0);
        messageBroker.publish(new TextMessage("0"), topic0);
        messageBroker.publish(new TextMessage("0"), topic0);

        assertEquals(1, sum1[0]);
    }

    @Test
    public void isSubscribed() {
        messageBroker.subscribe(subscriber0, topic1);
        messageBroker.subscribe(subscriber0, topic2);

        assertFalse(messageBroker.isSubscribed(subscriber0, topic0));
        assertTrue(messageBroker.isSubscribed(subscriber0, topic1));
        assertTrue(messageBroker.isSubscribed(subscriber0, topic2));

        messageBroker.unsubscribe(subscriber0, topic1);
        assertFalse(messageBroker.isSubscribed(subscriber0, topic1));
    }

    @Test
    public void getTopicsTest() {
        messageBroker.publish(new TextMessage("0"), topic0);
        assertTrue(messageBroker.getTopics().contains(topic0));
        assertEquals(1, messageBroker.getTopics().size());
        assertFalse(messageBroker.getTopics().contains(topic1));
    }
*/
}
