package com.message_broker.service;

import com.message_broker.models.MessageBroadcast;

import java.util.Set;

public interface BroadcastService extends AbstractService<Long, MessageBroadcast> {
    void merge(MessageBroadcast broadcast);

    Set<MessageBroadcast> getAllBroadcasts();

    MessageBroadcast getBroadcast(Long id);
}
