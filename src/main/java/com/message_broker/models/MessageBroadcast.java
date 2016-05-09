package com.message_broker.models;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@DynamicUpdate
@Table(name = "BROADCAST")
public class MessageBroadcast extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOPIC_ID", nullable = false)
    private volatile Topic topic;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private Message message;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BROADCAST_SUBSCRIBER", joinColumns = {
            @JoinColumn(name = "BROADCAST_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "SUBSCRIBER_ID",
                    nullable = false, updatable = false)})
    private final Set<Subscriber> visitedSubscribers = ConcurrentHashMap.newKeySet();

    private MessageBroadcast() {
    }

    public MessageBroadcast(Topic topic, Message message) {
        this.topic = topic;
        this.message = message;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Message getMessage() {
        return message;
    }

    public Set<Subscriber> getVisitedSubscribers() {
        return visitedSubscribers;
    }
}
