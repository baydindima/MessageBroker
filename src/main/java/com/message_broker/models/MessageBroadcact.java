package com.message_broker.models;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "BROADCAST")
public class MessageBroadcact extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOPIC_ID", nullable = false)
    private final Topic topic;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private final Message message;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BROADCAST_SUBSCRIBER", joinColumns = {
            @JoinColumn(name = "BROADCAST_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "SUBSCRIBER_ID",
                    nullable = false, updatable = false)})
    private final Set<Subscriber> visitedSubscribers = ConcurrentHashMap.newKeySet();

    public MessageBroadcact(Topic topic, Message message) {
        this.topic = topic;
        this.message = message;
    }

    public Topic getTopic() {
        return topic;
    }

    public Message getMessage() {
        return message;
    }

    public Set<Subscriber> getVisitedSubscribers() {
        return visitedSubscribers;
    }
}
