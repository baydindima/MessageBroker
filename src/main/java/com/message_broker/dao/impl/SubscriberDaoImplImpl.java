package com.message_broker.dao.impl;


import com.message_broker.dao.SubscriberDao;
import com.message_broker.models.Subscriber;
import org.springframework.stereotype.Repository;

@Repository("subscriberDao")
public class SubscriberDaoImplImpl extends AbstractDaoImpl<Long, Subscriber> implements SubscriberDao {

    public SubscriberDaoImplImpl() {
        super(Subscriber.class);
    }

}
