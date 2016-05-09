package com.message_broker.app;

import com.message_broker.config.HibernateTestRestartConfiguration;
import com.message_broker.models.MessageBroadcast;
import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import com.message_broker.service.BroadcastService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AppTest {

    private CommonMessageBrokerUtils utils = new CommonMessageBrokerUtils();

    @Test
    public void simpleInitTest() {
        MessageBrokerApp.getDefaultInstance();
    }

    @Test
    public void restartTest() {
        final int subscriberCount = 10;

        start(subscriberCount);

        ApplicationContext context = new AnnotationConfigApplicationContext(
                HibernateTestRestartConfiguration.class
        );
        MessageBroker messageBroker = MessageBrokerApp.newInstance(context);


        Topic[] topics = messageBroker.getTopics().toArray(new Topic[0]);
        assertEquals(1, topics.length);

        Topic topic = topics[0];

        Set<Subscriber> subscribers = topic.getSubscribers();
        assertEquals(subscriberCount, subscribers.size());

        messageBroker.publish(
                utils.getMessageFactory().newInstance(),
                topic
        );

        for (Subscriber subscriber : subscribers) {
            assertEquals(2, subscriber.getSavedMessages().size());
        }

        for (Subscriber subscriber : messageBroker.getSubscribers()) {
            assertEquals(2, subscriber.getSavedMessages().size());
        }
    }

    private void start(int subscriberCount) {

        ApplicationContext context = new AnnotationConfigApplicationContext(
                HibernateTestRestartConfiguration.class
        );

        MessageBroker messageBroker = MessageBrokerApp.getDefaultInstance();
        Topic topic = utils.getTopicFactory().newInstance();
        for (int i = 0; i < subscriberCount; i++) {
            messageBroker.subscribe(
                    utils.getSubscriberFactory().newInstance(),
                    topic
            );
        }
        messageBroker.publish(utils.getMessageFactory().newInstance(), topic);

        BroadcastService broadcastService = context.getBean(BroadcastService.class);
        broadcastService.save(
                new MessageBroadcast(topic, utils.getMessageFactory().newInstance())
        );
    }

}
