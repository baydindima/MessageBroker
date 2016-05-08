package com.message_broker.dao;

import java.io.Serializable;

public interface AbstractDao<K extends Serializable, T> {

    public T getByKey(K key);

    public void delete(T object);

    public void persist(T object);

    public void update(T object);

}
