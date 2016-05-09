package com.message_broker.models;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Entity
@DynamicUpdate
@Table(name = "SUBSCRIBER")
public class Subscriber extends BaseEntity {
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Transient
    private final BlockingQueue<Message> savedMessages = new LinkedBlockingQueue<>();

    private Subscriber() {
    }

    public Subscriber(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BlockingQueue<Message> getSavedMessages() {
        return savedMessages;
    }

    public void receiveMessage(Topic topic, Message message) {
        savedMessages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subscriber that = (Subscriber) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return String.format("Subscriber{name='%s'}", name);
    }
}
