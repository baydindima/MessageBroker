package com.message_broker.dao;

import java.io.Serializable;
import java.util.List;

public interface AbstractDao<K extends Serializable, T> {

    T getByKey(K key);

    void delete(T object);

    void persist(T object);

    void update(T object);

    void merge(T object);

    @SuppressWarnings("unchecked")
    List<T> getAll();
}
