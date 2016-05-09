package com.message_broker.service.impl;

import com.message_broker.dao.BroadcastDao;
import com.message_broker.models.MessageBroadcast;
import com.message_broker.service.BroadcastService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service("broadcastService")
@Transactional
public class BroadcastServiceImpl implements BroadcastService {
    @Autowired
    private BroadcastDao broadcastDao;

    @Override
    public MessageBroadcast findById(Long id) {
        return broadcastDao.getByKey(id);
    }

    @Override
    public void save(MessageBroadcast broadcast) {
        broadcastDao.persist(broadcast);
    }

    @Override
    public void update(MessageBroadcast object) {
        broadcastDao.update(object);
    }

    @Override
    public void merge(MessageBroadcast broadcast) {
        broadcastDao.merge(broadcast);
    }

    @Override
    public void delete(MessageBroadcast object) {
        broadcastDao.delete(object);
    }

    @Override
    public Set<MessageBroadcast> getAllBroadcasts() {
        Set<MessageBroadcast> messageBroadcasts = new HashSet<>(broadcastDao.getAll());
        for (MessageBroadcast messageBroadcast : messageBroadcasts) {
            Hibernate.initialize(messageBroadcast.getTopic());
        }
        return messageBroadcasts;
    }

    @Override
    public MessageBroadcast getBroadcast(Long id) {
        MessageBroadcast broadcast = broadcastDao.getByKey(id);
        Hibernate.initialize(broadcast.getVisitedSubscribers());
        Hibernate.initialize(broadcast.getMessage());
        Hibernate.initialize(broadcast.getTopic());
        return broadcast;
    }
}
