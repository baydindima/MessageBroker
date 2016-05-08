package com.message_broker.service.impl;

import com.message_broker.dao.BroadcastDao;
import com.message_broker.models.MessageBroadcast;
import com.message_broker.service.BroadcastService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void save(MessageBroadcast object) {
        broadcastDao.persist(object);
    }

    @Override
    public void update(MessageBroadcast object) {
        broadcastDao.update(object);
    }

    @Override
    public void delete(MessageBroadcast object) {
        broadcastDao.delete(object);
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
