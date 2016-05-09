package com.message_broker.app;

import com.message_broker.app.MessageBroker;
import com.message_broker.app.MessageBrokerImpl;
import com.message_broker.config.HibernateConfiguration;
import com.message_broker.models.Message;
import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MessageBrokerImpl}
 */
public class MessageBrokerTest extends CommonMessageBrokerUtils {

    @Autowired
    private MessageBroker messageBroker;

    @Test
    public void simpleTest() {
        Topic topic = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();
        Message message = getMessageFactory().newInstance();

        messageBroker.subscribe(subscriber, topic);
        messageBroker.publish(message, topic);

        assertEquals(1, subscriber.getSavedMessages().size());
        assertEquals(message.getMessage(), subscriber.getSavedMessages().poll().getMessage());
    }

    @Test
    public void doubleSubscribeTest() {
        Subscriber subscriber = getSubscriberFactory().newInstance();
        Topic topic = getTopicFactory().newInstance();
        Message message = getMessageFactory().newInstance();

        messageBroker.subscribe(subscriber, topic);
        messageBroker.subscribe(subscriber, topic);
        messageBroker.publish(message, topic);

        assertEquals(message.getMessage(), subscriber.getSavedMessages().poll().getMessage());
        assertEquals(0, subscriber.getSavedMessages().size());
    }

    @Test
    public void multiSubscribeTest() {
        Subscriber subscriber1 = getSubscriberFactory().newInstance();
        Subscriber subscriber2 = getSubscriberFactory().newInstance();
        Subscriber subscriber3 = getSubscriberFactory().newInstance();
        Topic topic = getTopicFactory().newInstance();
        Message message = getMessageFactory().newInstance();

        messageBroker.subscribe(subscriber1, topic);
        messageBroker.subscribe(subscriber2, topic);
        messageBroker.subscribe(subscriber3, topic);
        messageBroker.publish(message, topic);

        assertEquals(message.getMessage(), subscriber1.getSavedMessages().poll());
        assertEquals(message.getMessage(), subscriber2.getSavedMessages().poll());
        assertEquals(message.getMessage(), subscriber3.getSavedMessages().poll());
    }

    @Test
    public void multiPublishTest() {
        Topic topic = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();

        messageBroker.subscribe(subscriber, topic);
        messageBroker.publish(getMessageFactory().newInstance(), topic);
        messageBroker.publish(getMessageFactory().newInstance(), topic);
        messageBroker.publish(getMessageFactory().newInstance(), topic);

        assertEquals(3, subscriber.getSavedMessages().size());
    }

    @Test
    public void unsubscribeTest() {
        Topic topic = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();

        messageBroker.unsubscribe(subscriber, topic);
        messageBroker.publish(getMessageFactory().newInstance(), topic);
        assertEquals(0, subscriber.getSavedMessages().size());

        messageBroker.subscribe(subscriber, topic);
        messageBroker.publish(getMessageFactory().newInstance(), topic);
        messageBroker.unsubscribe(subscriber, topic);
        messageBroker.publish(getMessageFactory().newInstance(), topic);
        messageBroker.publish(getMessageFactory().newInstance(), topic);

        assertEquals(1, subscriber.getSavedMessages().size());
    }

    @Test
    public void isSubscribed() {
        Topic topic1 = getTopicFactory().newInstance();
        Topic topic2 = getTopicFactory().newInstance();
        Topic topic3 = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();

        messageBroker.subscribe(subscriber, topic1);
        messageBroker.subscribe(subscriber, topic2);

        assertTrue(messageBroker.isSubscribed(subscriber, topic1));
        assertTrue(messageBroker.isSubscribed(subscriber, topic2));
        assertFalse(messageBroker.isSubscribed(subscriber, topic3));

        messageBroker.unsubscribe(subscriber, topic1);
        assertFalse(messageBroker.isSubscribed(subscriber, topic1));
    }

//    @Test
//    public void getTopicsTest() {
//        messageBroker.publish(new TextMessage("0"), topic0);
//        assertTrue(messageBroker.getTopics().contains(topic0));
//        assertEquals(1, messageBroker.getTopics().size());
//        assertFalse(messageBroker.getTopics().contains(topic1));
//    }

}
