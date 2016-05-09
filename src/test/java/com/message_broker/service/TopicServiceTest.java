package com.message_broker.service;

import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TopicServiceTest extends CommonServiceUtilsTest {

    @Test
    public void saveGetTest() {
        final String topicName = "TOPIC_NAME";
        final int subscriberCount = 10;

        Topic topic = new Topic(topicName);
        for (int i = 0; i < subscriberCount; i++) {
            topic.getSubscribers().add(getSubscriberFactory().newInstance());
        }
        getTopicService().save(topic);
        topic = getTopicService().getTopic(topic.getId());

        assertEquals(topicName, topic.getName());
        assertEquals(subscriberCount, topic.getSubscribers().size());
    }

    @Test
    public void getAllTest() {
        final String topicName1 = "TOPIC_NAME1";
        final String topicName2 = "TOPIC_NAME2";

        getTopicService().save(new Topic(topicName1));
        getTopicService().save(new Topic(topicName2));

        Set<Topic> topics = getTopicService().getAllTopics();
        assertTrue(topics.size() >= 2);

        boolean isExist = false;
        for (Topic topic : topics) {
            if (topic.getName().equals(topicName1)) {
                isExist = true;
                break;
            }
        }

        assertTrue(isExist);
    }

    @org.junit.Test
    public void deleteTest() {
        Subscriber subscriber = getSubscriberFactory().newInstance();
        final String subscriberName = subscriber.getName();

        final String topicName = "TOPIC_NAME3";
        Topic topic = new Topic(topicName);

        topic.getSubscribers().add(subscriber);
        getTopicService().save(topic);

        getTopicService().delete(topic);

        topic = getTopicService().findById(topic.getId());
        subscriber = getSubscriberService().findById(subscriber.getId());

        assertNull(topic);
        assertEquals(subscriberName, subscriber.getName());
    }

    @Test
    public void updateTest() {
        final String topicName = "TOPIC_NAME4";
        Topic topic = new Topic(topicName);
        getTopicService().save(topic);

        Subscriber subscriber = getSubscriberFactory().newInstance();
        topic.getSubscribers().add(subscriber);
        getTopicService().update(topic);

        topic = getTopicService().getTopic(topic.getId());
        assertEquals(1, topic.getSubscribers().size());

        topic.getSubscribers().remove(subscriber);
        getTopicService().update(topic);

        topic = getTopicService().getTopic(topic.getId());
        assertEquals(0, topic.getSubscribers().size());
    }

}
