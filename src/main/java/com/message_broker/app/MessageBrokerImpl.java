package com.message_broker.app;

import com.message_broker.dao.BroadcastDao;
import com.message_broker.dao.SubscriberDao;
import com.message_broker.dao.TopicDao;
import com.message_broker.models.Message;
import com.message_broker.models.MessageBroadcast;
import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * Implementation of {@link MessageBroker}
 */
@Component
@Transactional
public class MessageBrokerImpl implements MessageBroker {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private SubscriberDao subscriberDao;

    @Autowired
    private BroadcastDao broadcastDao;

    private final ConcurrentHashMap<Topic, TopicHolder> topics;
    private final ConcurrentHashMap<Subscriber, Subscriber> subscribers;

    public MessageBrokerImpl() {
        topics = new ConcurrentHashMap<>();
        subscribers = new ConcurrentHashMap<>();
    }

    @Override
    public void refresh(
            Set<Topic> topics,
            Set<MessageBroadcast> broadcasts
    ) {
        for (Topic topic : topics) {
            this.topics.put(topic, new TopicHolder(topic));
            for (Subscriber subscriber : topic.getSubscribers()) {
                this.subscribers.put(subscriber, subscriber);
            }
        }

        for (MessageBroadcast broadcast : broadcasts) {
            Topic topic = this.topics.get(broadcast.getTopic()).topic;
            topic.getSubscribers().forEach(subscriber -> {
                        if (!broadcast.getVisitedSubscribers().contains(subscriber)) {
                            sendMessage(topic, broadcast, subscriber);
                        }
                    }
            );
            broadcastDao.delete(broadcast);
        }
    }

    @Override
    public void publish(Message message, Topic topic) {
        final MessageBroadcast broadcast = new MessageBroadcast(getTopicHolder(topic).topic, message);
        broadcastDao.persist(broadcast);
        subscriberReadOperation(topic, topic1 -> {
            topic1.getSubscribers().stream().forEach(subscriber -> sendMessage(topic, broadcast, subscriber));
            return true;
        });
        broadcastDao.delete(broadcast);
    }

    private void sendMessage(Topic topic, MessageBroadcast broadcast, Subscriber subscriber) {
        subscriber.receiveMessage(topic, broadcast.getMessage());
        broadcast.getVisitedSubscribers().add(subscriber);
        broadcastDao.update(broadcast);
    }

    @Override
    public boolean subscribe(Subscriber subscriber, Topic topic) {
        Subscriber subscriber1 = getSubscriber(subscriber);
        return subscriberWriteOperation(topic, topic1 -> {
            if (topic1.getSubscribers().add(subscriber1)) {
                topicDao.update(topic1);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean unsubscribe(Subscriber subscriber, Topic topic) {
        Subscriber subscriber1 = getSubscriber(subscriber);
        return subscriberWriteOperation(topic, topic1 -> {
            if (topic1.getSubscribers().remove(subscriber1)) {
                topicDao.update(topic1);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean isSubscribed(Subscriber subscriber, Topic topic) {
        return subscriberReadOperation(topic, topic1 -> topic.getSubscribers().contains(subscriber));
    }

    @Override
    public Set<Topic> getTopics() {
        return Collections.unmodifiableSet(topics.keySet());
    }

    @Override
    public Set<Subscriber> getSubscribers() {
        return Collections.unmodifiableSet(subscribers.keySet());
    }

    private boolean subscriberReadOperation(Topic topic, Function<Topic, Boolean> function) {
        TopicHolder topicHolder = getTopicHolder(topic);
        topicHolder.readLock.lock();
        try {
            return function.apply(topicHolder.topic);
        } finally {
            topicHolder.readLock.unlock();
        }
    }

    private boolean subscriberWriteOperation(Topic topic, Function<Topic, Boolean> function) {
        TopicHolder topicHolder = getTopicHolder(topic);
        topicHolder.writeLock.lock();
        try {
            return function.apply(topicHolder.topic);
        } finally {
            topicHolder.writeLock.unlock();
        }
    }

    private Subscriber getSubscriber(Subscriber subscriber) {
        return subscribers.computeIfAbsent(subscriber, subscriber1 -> {
            subscriberDao.persist(subscriber1);
            return subscriber1;
        });
    }

    private TopicHolder getTopicHolder(Topic topic) {
        return topics.computeIfAbsent(topic,
                topic1 -> {
                    topicDao.persist(topic1);
                    return new TopicHolder(topic1);
                }
        );
    }

    private static final class TopicHolder {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final Lock readLock = lock.readLock();
        private final Lock writeLock = lock.writeLock();
        private final Topic topic;

        private TopicHolder(Topic topic) {
            this.topic = topic;
        }
    }

}
