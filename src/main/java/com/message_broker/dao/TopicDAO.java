package com.message_broker.dao;


import com.message_broker.models.Topic;

import java.util.List;

public interface TopicDao extends AbstractDao<Long, Topic> {
    List<Topic> getAllTopics();
}
