import models.Message;
import models.Subscriber;
import models.Topic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.function.Function;


public class MessageBroker {
    private final Map<Topic, SubscribersHolder> subscriberMaps;

    public MessageBroker() {
        subscriberMaps = new ConcurrentHashMap<>();
    }

    public void publish(Message message, Topic topic) {
     subscriberReadOperation(topic, subscribers -> {
         subscribers.parallelStream().forEach(subscriber -> subscriber.receiveMessage(message));
         return true;
     });
    }

    public boolean subscribe(Subscriber subscriber, Topic topic) {
        return subscriberWriteOperation(topic, subscribers -> subscribers.add(subscriber));
    }

    public boolean unsubscribe(Subscriber subscriber, Topic topic) {
        return subscriberWriteOperation(topic, subscribers -> subscribers.remove(subscriber));
    }

    public boolean isSubscribed(Subscriber subscriber, Topic topic) {
        return subscriberReadOperation(topic, subscribers -> subscribers.contains(subscriber));
    }

    public Set<Topic> getTopics() {
        return Collections.unmodifiableSet(subscriberMaps.keySet());
    }

    private boolean subscriberReadOperation(Topic topic, Function<Set<Subscriber>, Boolean> function) {
        SubscribersHolder subscribersHolder = getSubscribers(topic);
        subscribersHolder.readLock.lock();
        try {
            return function.apply(subscribersHolder.subscribers);
        } finally {
            subscribersHolder.readLock.unlock();
        }
    }

    private boolean subscriberWriteOperation(Topic topic, Function<Set<Subscriber>, Boolean> function) {
        SubscribersHolder subscribersHolder = getSubscribers(topic);
        subscribersHolder.writeLock.lock();
        try {
            return function.apply(subscribersHolder.subscribers);
        } finally {
            subscribersHolder.writeLock.unlock();
        }
    }

    private SubscribersHolder getSubscribers(Topic topic) {
        return subscriberMaps.computeIfAbsent(topic, topic1 -> new SubscribersHolder(ConcurrentHashMap.newKeySet()));
    }

    private static class SubscribersHolder {
        private final Set<Subscriber> subscribers;
        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private ReadLock readLock = lock.readLock();
        private WriteLock writeLock = lock.writeLock();
        SubscribersHolder(Set<Subscriber> subscribers) {
            this.subscribers = subscribers;
        }
    }
}
