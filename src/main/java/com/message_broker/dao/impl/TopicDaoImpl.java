package com.message_broker.dao.impl;

import com.message_broker.dao.TopicDao;
import com.message_broker.models.Topic;
import org.springframework.stereotype.Repository;

@Repository("topicDao")
public class TopicDaoImpl extends AbstractDaoImpl<Long, Topic> implements TopicDao {

    public TopicDaoImpl() {
        super(Topic.class);
    }
}
