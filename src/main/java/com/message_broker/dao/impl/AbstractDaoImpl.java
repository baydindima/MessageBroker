package com.message_broker.dao.impl;


import com.message_broker.dao.AbstractDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public class AbstractDaoImpl<K extends Serializable, T> implements AbstractDao<K, T> {

    private final Class<T> persistentClass;

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    AbstractDaoImpl() {
        persistentClass = (Class<T>)
                ((ParameterizedType) getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[1];
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getByKey(K key) {
        return (T) getSession().get(persistentClass, key);
    }

    @Override
    public void delete(T object) {
        getSession().delete(object);
    }

    @Override
    public void persist(T object) {
        getSession().persist(object);
    }

    @Override
    public void update(T object) {
        getSession().update(object);
    }

    @Override
    public void merge(T object) {
        getSession().merge(object);
    }

    protected Criteria createEntityCriteria() {
        return getSession().createCriteria(persistentClass);
    }

}
