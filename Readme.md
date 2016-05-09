# Message Broker

Implementation of local failover Message Broker in Java. It allows you to send messages to arbitrary topics. And also subscribe to these topics. It uses the embedded database (HSQLDB) for persistence.

## Build

For build project 

```
mvn clean install
```

## API

To create MessageBroker
```JAVA
    MessageBroker messageBroker = MessageBrokerApp.getDefaultInstance();
```

Subscribe and publish
```JAVA
    Topic topic = new Topic("TopicName");
    Subscriber subscriber = new Subscriber("SubscriberName");

    messageBroker.subscribe(subscriber, topic);
    messageBroker.publish(new Message("text"), topic);
    
    System.out.println(subscriber.getSavedMessages().poll().getMessage());
```

Create new instance of Message for every publish.