package com.message_broker.service;

import com.message_broker.models.Subscriber;

public interface SubscriberService extends AbstractService<Long, Subscriber> {
    Subscriber getSubscriber(Long id);
}
