package com.message_broker.dao.impl;

import com.message_broker.dao.MessageDao;
import com.message_broker.models.Message;
import org.springframework.stereotype.Repository;

@Repository("textMessageDao")
public class MessageDaoImpl extends AbstractDaoImpl<Long, Message> implements MessageDao {

    public MessageDaoImpl() {
        super(Message.class);
    }

}
