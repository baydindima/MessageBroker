package com.message_broker.app;


import com.message_broker.config.HibernateConfiguration;
import com.message_broker.models.MessageBroadcast;
import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;
import com.message_broker.service.BroadcastService;
import com.message_broker.service.SubscriberService;
import com.message_broker.service.TopicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Set;

public final class MessageBrokerApp {

    private static MessageBroker messageBroker;

    private static SubscriberService subscriberService;

    private static TopicService topicService;

    private static BroadcastService broadcastService;

    private MessageBrokerApp() {
    }

    public static MessageBroker newInstance(ApplicationContext applicationContext) {
        init(applicationContext);
        return messageBroker;
    }

    public static MessageBroker getDefaultInstance() {
        if (messageBroker == null) {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                    HibernateConfiguration.class
            );
            init(applicationContext);
        }
        return messageBroker;
    }

    private static void init(ApplicationContext applicationContext) {
        topicService = applicationContext.getBean(TopicService.class);
        broadcastService = applicationContext.getBean(BroadcastService.class);
        subscriberService = applicationContext.getBean(SubscriberService.class);

        Set<Topic> allTopics = topicService.getAllTopics();
        Set<MessageBroadcast> allBroadcasts = broadcastService.getAllBroadcasts();

        messageBroker = applicationContext.getBean(MessageBroker.class);
        messageBroker.refresh(allTopics, allBroadcasts);
    }

}
