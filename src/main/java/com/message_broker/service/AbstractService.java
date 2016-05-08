package com.message_broker.service;


import java.io.Serializable;

public interface AbstractService<K extends Serializable, T> {
    T findById(K id);

    void save(T object);

    void update(T object);

    void delete(T object);
}
