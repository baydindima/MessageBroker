package com.message_broker.service.impl;

import com.message_broker.dao.SubscriberDao;
import com.message_broker.models.Subscriber;
import com.message_broker.service.SubscriberService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("subscriberService")
@Transactional
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberDao subscriberDao;

    @Override
    public Subscriber findById(Long id) {
        return subscriberDao.getByKey(id);
    }

    @Override
    public void save(Subscriber subscriber) {
        subscriberDao.persist(subscriber);
    }

    @Override
    public void update(Subscriber subscriber) {
        subscriberDao.update(subscriber);
    }

    @Override
    public void delete(Subscriber subscriber) {
        subscriberDao.delete(subscriber);
    }

    @Override
    public Subscriber getSubscriber(Long id) {
        Subscriber subscriber = subscriberDao.getByKey(id);
        Hibernate.initialize(subscriber.getSavedMessages());
        return subscriber;
    }

}
