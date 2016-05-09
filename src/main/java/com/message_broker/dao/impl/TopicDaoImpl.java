package com.message_broker.dao.impl;

import com.message_broker.dao.TopicDao;
import com.message_broker.models.Topic;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("topicDao")
public class TopicDaoImpl extends AbstractDaoImpl<Long, Topic> implements TopicDao {

    @Override
    public List<Topic> getAllTopics() {
        Criteria criteria = createEntityCriteria();
        return (List<Topic>) criteria.list();
    }

    @Override
    public Topic findByName(String name) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("NAME", name));
        return (Topic) criteria.uniqueResult();
    }

}
