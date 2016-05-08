package com.message_broker.models;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.*;

@Entity
@DynamicUpdate
@Table(name = "SUBSCRIBER")
public class Subscriber extends BaseEntity {
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @ManyToMany()
    @JoinTable(name = "SUBSCRIBER_MESSAGE",
            joinColumns = @JoinColumn(name = "SUBSCRIBER_ID", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "MESSAGE_ID", nullable = false, updatable = false))
    private final List<Message> savedMessages = new LinkedList<>();

    private Subscriber() {
    }

    public Subscriber(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Message> getSavedMessages() {
        return savedMessages;
    }

    public boolean isEmpty() {
        synchronized (savedMessages) {
            return savedMessages.isEmpty();
        }
    }

    public Message getMessage() {
        synchronized (savedMessages) {
            if (!isEmpty()) {
                Message message = savedMessages.get(0);
                savedMessages.remove(0);
                return message;
            }
            return null;
        }
    }

    public void receiveMessage(Topic topic, Message message) {
        synchronized (savedMessages) {
            savedMessages.add(message);
            savedMessages.notifyAll();
        }
    }

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
