package com.message_broker.models;

import javax.persistence.*;

@Entity
@Table(name = "TOPIC")
public class Topic {

    private Topic() {
    }

    @Id
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    public Topic(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Topic{"
                + "name='" + name + '\''
                + ", id=" + id
                + '}';
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

        return id == topic.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
