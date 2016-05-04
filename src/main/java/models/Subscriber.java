package models;

public interface Subscriber {

    int getId();

    void receiveMessage(Message message);

}
