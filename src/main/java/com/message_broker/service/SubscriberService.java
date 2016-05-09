package com.message_broker.service;

import com.message_broker.models.Subscriber;

import java.util.Set;

public interface SubscriberService extends AbstractService<Long, Subscriber> {
    Set<Subscriber> getAllSubscribers();

    Subscriber getSubscriber(Long id);
}
