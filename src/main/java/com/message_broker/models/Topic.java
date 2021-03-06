package com.message_broker.models;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@DynamicUpdate
@Table(name = "TOPIC")
public class Topic extends BaseEntity {

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TOPIC_SUBSCRIBER", joinColumns = {
            @JoinColumn(name = "TOPIC_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "SUBSCRIBER_ID",
                    nullable = false, updatable = false)})
    private final Set<Subscriber> subscribers = ConcurrentHashMap.newKeySet();

    public Topic() {
    }

    public Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Subscriber> getSubscribers() {
        return subscribers;
    }

    @Override
    public String toString() {
        return String.format("Topic{name='%s'}", name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Topic topic = (Topic) o;
        return Objects.equals(name, topic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
