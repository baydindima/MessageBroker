package com.message_broker;

import com.message_broker.models.Message;
import com.message_broker.models.Subscriber;
import com.message_broker.models.Topic;

import java.util.Set;

public interface MessageBroker {

    void publish(Message message, Topic topic);

    boolean subscribe(Subscriber subscriber, Topic topic);

    boolean unsubscribe(Subscriber subscriber, Topic topic);

    boolean isSubscribed(Subscriber subscriber, Topic topic);

    Set<Topic> getTopics();

}
