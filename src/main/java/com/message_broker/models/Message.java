package com.message_broker.models;

import org.springframework.util.SerializationUtils;

import javax.persistence.*;

@Entity
@Table(name = "MESSAGE")
public class Message extends BaseEntity {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Object", nullable = false)
    private byte[] bytes;

//    @Transient
//    private volatile T cachedObject;

    public Message() {
    }

    public Message(Object bytes) {
//        cachedObject = bytes;
        this.bytes = SerializationUtils.serialize(bytes);
    }

    @SuppressWarnings("unchecked")
    public Object getMessage() {
//        if (cachedObject == null) {
//            cachedObject = (T) SerializationUtils.deserialize(bytes);
//        }
        return SerializationUtils.deserialize(bytes);
    }

}
