package com.message_broker.app;

import com.message_broker.models.Message;
import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MessageBrokerImpl}
 */
public class MessageBrokerTest extends CommonMessageBrokerUtils {

    @Test
    public void simpleTest() {
        Topic topic = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();
        Message message = getMessageFactory().newInstance();

        getMessageBroker().subscribe(subscriber, topic);
        getMessageBroker().publish(message, topic);

        assertEquals(1, subscriber.getSavedMessages().size());
        assertEquals(message.getMessage(), subscriber.getSavedMessages().poll().getMessage());
    }

    @Test
    public void doubleSubscribeTest() {
        Subscriber subscriber = getSubscriberFactory().newInstance();
        Topic topic = getTopicFactory().newInstance();
        Message message = getMessageFactory().newInstance();

        getMessageBroker().subscribe(subscriber, topic);
        getMessageBroker().subscribe(subscriber, topic);
        getMessageBroker().publish(message, topic);

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

        getMessageBroker().subscribe(subscriber1, topic);
        getMessageBroker().subscribe(subscriber2, topic);
        getMessageBroker().subscribe(subscriber3, topic);
        getMessageBroker().publish(message, topic);

        assertEquals(message.getMessage(), subscriber1.getSavedMessages().poll().getMessage());
        assertEquals(message.getMessage(), subscriber2.getSavedMessages().poll().getMessage());
        assertEquals(message.getMessage(), subscriber3.getSavedMessages().poll().getMessage());
    }

    @Test
    public void multiPublishTest() {
        Topic topic = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();

        getMessageBroker().subscribe(subscriber, topic);
        getMessageBroker().publish(getMessageFactory().newInstance(), topic);
        getMessageBroker().publish(getMessageFactory().newInstance(), topic);
        getMessageBroker().publish(getMessageFactory().newInstance(), topic);

        assertEquals(3, subscriber.getSavedMessages().size());
    }

    @Test
    public void unsubscribeTest() {
        Topic topic = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();

        getMessageBroker().unsubscribe(subscriber, topic);
        getMessageBroker().publish(getMessageFactory().newInstance(), topic);
        assertEquals(0, subscriber.getSavedMessages().size());

        getMessageBroker().subscribe(subscriber, topic);
        getMessageBroker().publish(getMessageFactory().newInstance(), topic);
        getMessageBroker().unsubscribe(subscriber, topic);
        getMessageBroker().publish(getMessageFactory().newInstance(), topic);
        getMessageBroker().publish(getMessageFactory().newInstance(), topic);

        assertEquals(1, subscriber.getSavedMessages().size());
    }

    @Test
    public void isSubscribed() {
        Topic topic1 = getTopicFactory().newInstance();
        Topic topic2 = getTopicFactory().newInstance();
        Topic topic3 = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();

        getMessageBroker().subscribe(subscriber, topic1);
        getMessageBroker().subscribe(subscriber, topic2);

        assertTrue(getMessageBroker().isSubscribed(subscriber, topic1));
        assertTrue(getMessageBroker().isSubscribed(subscriber, topic2));
        assertFalse(getMessageBroker().isSubscribed(subscriber, topic3));

        getMessageBroker().unsubscribe(subscriber, topic1);
        assertFalse(getMessageBroker().isSubscribed(subscriber, topic1));
    }

    @Test
    public void getTopicsTest() {
        Topic topic = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();

        getMessageBroker().subscribe(subscriber, topic);

        assertTrue(getMessageBroker().getTopics().contains(topic));
        assertFalse(getMessageBroker().getTopics().contains(getTopicFactory().newInstance()));
    }

    @Test
    public void getSubscribersTest() {
        Topic topic = getTopicFactory().newInstance();
        Subscriber subscriber = getSubscriberFactory().newInstance();

        getMessageBroker().subscribe(subscriber, topic);

        assertTrue(getMessageBroker().getSubscribers().contains(subscriber));
        assertFalse(getMessageBroker().getSubscribers().contains(getSubscriberFactory().newInstance()));
    }

}
