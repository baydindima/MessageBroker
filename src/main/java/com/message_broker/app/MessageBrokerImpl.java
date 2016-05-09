package com.message_broker.app;

import com.message_broker.models.Message;
import com.message_broker.models.MessageBroadcast;
import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import com.message_broker.service.BroadcastService;
import com.message_broker.service.MessageService;
import com.message_broker.service.SubscriberService;
import com.message_broker.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.function.Function;

/**
 * Implementation of {@link MessageBroker}
 */
@Component
public class MessageBrokerImpl implements MessageBroker {

    @Autowired
    private TopicService topicService;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private BroadcastService broadcastService;

    private final ConcurrentHashMap<Topic, TopicHolder> topics;
    private final ConcurrentHashMap<Subscriber, Subscriber> subscribers;

    public MessageBrokerImpl() {
        topics = new ConcurrentHashMap<>();
        subscribers = new ConcurrentHashMap<>();
    }

    @Override
    public void publish(Message message, Topic topic) {
        final MessageBroadcast broadcast = new MessageBroadcast(getTopicHolder(topic).topic, message);
        broadcastService.save(broadcast);
        subscriberReadOperation(topic, topic1 -> {
            topic1.getSubscribers().parallelStream().forEach(subscriber -> {
                subscriber.receiveMessage(topic1, message);
                broadcast.getVisitedSubscribers().add(subscriber);
                broadcastService.merge(broadcast);
            });
            return true;
        });
//        broadcastService.update(broadcast);
//        broadcastService.delete(broadcast);
    }

    @Override
    public boolean subscribe(Subscriber subscriber, Topic topic) {
        Subscriber subscriber1 = getSubscriber(subscriber);
        return subscriberWriteOperation(topic, topic1 -> {
            if (topic1.getSubscribers().add(subscriber1)) {
                topicService.update(topic1);
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
                topicService.merge(topic1);
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
            subscriberService.save(subscriber1);
            return subscriber1;
        });
    }

    private TopicHolder getTopicHolder(Topic topic) {
        return topics.computeIfAbsent(topic,
                topic1 -> {
                    topicService.save(topic1);
                    return new TopicHolder(topic1);
                }
        );
    }

    private static class TopicHolder {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final Lock readLock = lock.readLock();
        private final Lock writeLock = lock.writeLock();
        private final Topic topic;

        private TopicHolder(Topic topic) {
            this.topic = topic;
        }
    }

}
