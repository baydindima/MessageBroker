package com.message_broker.service;

import com.message_broker.models.Message;
import com.message_broker.models.MessageBroadcast;
import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BroadcastServiceTest extends CommonServiceUtilsTest {

    @Test
    public void saveAndGetTest() {
        final String text = "Message-Text";

        Topic topic = getTopicFactory().newInstance();
        MessageBroadcast broadcast = new MessageBroadcast(topic, new Message(text));
        broadcast.getVisitedSubscribers().add(getSubscriberFactory().newInstance());
        broadcast.getVisitedSubscribers().add(getSubscriberFactory().newInstance());
        broadcast.getVisitedSubscribers().add(getSubscriberFactory().newInstance());
        broadcast.getVisitedSubscribers().add(getSubscriberFactory().newInstance());

        getBroadcastService().save(broadcast);
        broadcast = getBroadcastService().getBroadcast(broadcast.getId());

        assertEquals(text, broadcast.getMessage().getMessage());
        assertEquals(topic.getName(), broadcast.getTopic().getName());
        assertEquals(4, broadcast.getVisitedSubscribers().size());
    }

    @Test
    public void updateTest() {
        final String text = "Message-Text2";
        final int subscriberCount = 10;

        Topic topic = getTopicFactory().newInstance();
        MessageBroadcast broadcast = new MessageBroadcast(topic, new Message(text));
        getBroadcastService().save(broadcast);

        for (int i = 0; i < subscriberCount; i++) {
            broadcast.getVisitedSubscribers().add(getSubscriberFactory().newInstance());
            getBroadcastService().update(broadcast);
        }

        broadcast = getBroadcastService().getBroadcast(broadcast.getId());
        assertEquals(subscriberCount, broadcast.getVisitedSubscribers().size());
    }

    @Test
    public void deleteTest() {
        final String text = "Message-Text3";
        Subscriber subscriber = getSubscriberFactory().newInstance();

        Topic topic = getTopicFactory().newInstance();
        Message message = new Message(text);

        MessageBroadcast broadcast = new MessageBroadcast(topic, message);
        broadcast.getVisitedSubscribers().add(subscriber);
        getBroadcastService().save(broadcast);

        message = getMessageService().findById(message.getId());
        assertEquals(text, message.getMessage());

        getBroadcastService().delete(broadcast);

        message = getMessageService().findById(message.getId());
        assertNull(message);

        subscriber = getSubscriberService().findById(subscriber.getId());
        assertNotNull(subscriber);
    }

}
