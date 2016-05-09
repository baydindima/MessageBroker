package com.message_broker.service;

import com.message_broker.config.HibernateConfiguration;
import com.message_broker.models.Message;
import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateConfiguration.class})
@TestPropertySource(value = {"classpath:application.test.properties"})
public class CommonServiceUtilsTest {

    @Autowired
    private TopicService topicService;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private BroadcastService broadcastService;

    @Test
    public void simpleTest() {
        assertEquals(1, 1);
    }

    public abstract static class Factory<T> {
        private static AtomicInteger count = new AtomicInteger();

        protected abstract T newObject(int i);

        T newInstance() {
            return newObject(count.incrementAndGet());
        }
    }

    private final Factory<Topic> topicFactory = new Factory<Topic>() {
        @Override
        protected Topic newObject(int i) {
            Topic topic = new Topic("Service-Topic" + i);
            topicService.save(topic);
            return topic;
        }
    };

    private final Factory<Subscriber> subscriberFactory = new Factory<Subscriber>() {
        @Override
        protected Subscriber newObject(int i) {
            Subscriber subscriber = new Subscriber("Service-Subscriber" + i);
            subscriberService.save(subscriber);
            return subscriber;
        }
    };

    private final Factory<Message> messageFactory = new Factory<Message>() {
        @Override
        protected Message newObject(int i) {
            Message message = new Message("Service-Message" + i);
            messageService.save(message);
            return message;
        }
    };

    public TopicService getTopicService() {
        return topicService;
    }

    public SubscriberService getSubscriberService() {
        return subscriberService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public BroadcastService getBroadcastService() {
        return broadcastService;
    }

    public Factory<Topic> getTopicFactory() {
        return topicFactory;
    }

    public Factory<Subscriber> getSubscriberFactory() {
        return subscriberFactory;
    }

    public Factory<Message> getMessageFactory() {
        return messageFactory;
    }
}
