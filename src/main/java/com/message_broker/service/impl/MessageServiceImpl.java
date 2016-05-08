package com.message_broker.service.impl;

import com.message_broker.dao.MessageDao;
import com.message_broker.models.Message;
import com.message_broker.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public Message findById(Long id) {
        return messageDao.getByKey(id);
    }

    @Override
    public void save(Message message) {
        messageDao.persist(message);
    }

    @Override
    public void update(Message message) {
        messageDao.update(message);
    }

    @Override
    public void delete(Message message) {
        messageDao.delete(message);
    }

}
