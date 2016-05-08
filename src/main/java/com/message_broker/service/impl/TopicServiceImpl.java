package com.message_broker.service.impl;

import com.message_broker.dao.TopicDao;
import com.message_broker.models.Topic;
import com.message_broker.service.TopicService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service("topicService")
@Transactional
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;

    @Override
    public Topic findById(Long id) {
        return topicDao.getByKey(id);
    }

    @Override
    public Topic getTopic(Long id) {
        Topic topic = topicDao.getByKey(id);
        Hibernate.initialize(topic.getSubscribers());
        return topic;
    }

    @Override
    public void save(Topic topic) {
        topicDao.persist(topic);
    }

    @Override
    public void update(Topic topic) {
        topicDao.update(topic);
    }

    @Override
    public void delete(Topic topic) {
        topicDao.delete(topic);
    }

    @Override
    public Set<Topic> getAllTopics() {
        return new HashSet<>(topicDao.getAllTopics());
    }

}
