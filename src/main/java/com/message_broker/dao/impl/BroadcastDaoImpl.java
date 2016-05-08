package com.message_broker.dao.impl;

import com.message_broker.dao.BroadcastDao;
import com.message_broker.models.MessageBroadcast;
import org.springframework.stereotype.Repository;

@Repository("broadcastDao")
public class BroadcastDaoImpl extends AbstractDaoImpl<Long, MessageBroadcast> implements BroadcastDao {
}
