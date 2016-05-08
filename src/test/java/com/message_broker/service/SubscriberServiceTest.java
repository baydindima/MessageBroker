package com.message_broker.service;

import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubscriberServiceTest extends CommonServiceUtilsTest {

    @Test
    public void saveGetTest() {
        final String text = "First";
        Topic topic = getTopicFactory().newInstance();

        Subscriber subscriber = new Subscriber(text);
        subscriber.receiveMessage(topic, getMessageFactory().newInstance());
        subscriber.receiveMessage(topic, getMessageFactory().newInstance());
        subscriber.receiveMessage(topic, getMessageFactory().newInstance());

        getSubscriberService().save(subscriber);

        subscriber = getSubscriberService().getSubscriber(subscriber.getId());

        assertEquals(3, subscriber.getSavedMessages().size());
        assertEquals(text, subscriber.getName());
    }

    @Test
    public void updateTest() {
        final String text = "Second";
        final int messageCount = 10;
        Topic topic = getTopicFactory().newInstance();

        Subscriber subscriber = new Subscriber(text);
        getSubscriberService().save(subscriber);

        for (int i = 0; i < messageCount; i++) {
            subscriber.receiveMessage(topic, getMessageFactory().newInstance());
            getSubscriberService().update(subscriber);
        }

        subscriber = getSubscriberService().getSubscriber(subscriber.getId());
        assertEquals(messageCount, subscriber.getSavedMessages().size());
    }

}
