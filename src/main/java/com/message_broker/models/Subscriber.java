package com.message_broker.models;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "SUBSCRIBER")
public abstract class Subscriber extends BaseEntity {
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subscribers")
    private final Set<Topic> topics = ConcurrentHashMap.newKeySet();

    private Subscriber(){
    }

    public Subscriber(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public abstract void receiveMessage(Topic topic, Message message);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscriber that = (Subscriber) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "name='" + name + '\'' +
                '}';
    }
}
