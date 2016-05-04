import models.Message;
import models.Subscriber;
import models.Topic;

import java.util.Set;

public interface MessageBroker {

    void publish(Message message, Topic topic);

    boolean subscribe(Subscriber subscriber, Topic topic);

    boolean unsubscribe(Subscriber subscriber, Topic topic);

    boolean isSubscribed(Subscriber subscriber, Topic topic);

    Set<Topic> getTopics();

}
