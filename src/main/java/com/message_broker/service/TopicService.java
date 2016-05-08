package com.message_broker.service;

import com.message_broker.models.Topic;

import java.util.Set;

public interface TopicService  extends AbstractService<Long, Topic>  {

    Topic getTopic(Long id);

    Set<Topic> getAllTopics();
}
