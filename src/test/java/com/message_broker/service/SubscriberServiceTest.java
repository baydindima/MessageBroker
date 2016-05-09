package com.message_broker.service;

import com.message_broker.models.Subscriber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubscriberServiceTest extends CommonServiceUtilsTest {

    @Test
    public void saveGetTest() {
        Subscriber subscriber = getSubscriberFactory().newInstance();
        final String text = subscriber.getName();

        subscriber = getSubscriberService().getSubscriber(subscriber.getId());

        assertEquals(text, subscriber.getName());
    }

}
