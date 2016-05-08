package com.message_broker.models;

import org.springframework.util.SerializationUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MESSAGE")
public class Message<T extends Serializable> extends BaseEntity {

    @Lob @Basic(fetch=FetchType.LAZY)
    @Column(name = "Object", nullable = false)
    private byte[] bytes;

    @Transient
    private volatile T cachedObject;

    private Message() {
    }

    public Message(T bytes) {
        cachedObject = bytes;
        this.bytes = SerializationUtils.serialize(bytes);
    }

    @SuppressWarnings("unchecked")
    public T getMessage() {
        if (cachedObject == null) {
            cachedObject = (T) SerializationUtils.deserialize(bytes);
        }
        return cachedObject;
    }

}
