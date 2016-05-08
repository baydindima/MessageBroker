package com.message_broker.dao;

import java.io.Serializable;

public interface AbstractDao<K extends Serializable, T> {

    T getByKey(K key);

    void delete(T object);

    void persist(T object);

    void update(T object);

}
