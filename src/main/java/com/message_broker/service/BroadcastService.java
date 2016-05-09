package com.message_broker.service;

import com.message_broker.models.MessageBroadcast;

public interface BroadcastService extends AbstractService<Long, MessageBroadcast> {
    void merge(MessageBroadcast broadcast);

    MessageBroadcast getBroadcast(Long id);
}
