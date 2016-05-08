package com.message_broker.service;

import com.message_broker.models.Message;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageServiceTest extends CommonServiceUtilsTest {

    @Test
    public void saveGetTest() {
        final String text = "First";
        final int intValue = 121;
        final long longValue = 212L;

        Message messageText = new Message(text);
        getMessageService().save(messageText);
        messageText = getMessageService().findById(messageText.getId());
        assertEquals(text, messageText.getMessage());

        Message messageInt = new Message(intValue);
        getMessageService().save(messageInt);
        messageInt = getMessageService().findById(messageInt.getId());
        assertEquals(intValue, messageInt.getMessage());

        Message messageLong = new Message(longValue);
        getMessageService().save(messageLong);
        messageLong = getMessageService().findById(messageLong.getId());
        assertEquals(longValue, messageLong.getMessage());
    }

    public void deleteTest() {
        final String text = "Second";

        Message messageText = new Message(text);
        getMessageService().save(messageText);
        messageText = getMessageService().findById(messageText.getId());
        assertEquals(text, messageText.getMessage());

        getMessageService().delete(messageText);
        messageText = getMessageService().findById(messageText.getId());

        assertNull(messageText);
    }

}
