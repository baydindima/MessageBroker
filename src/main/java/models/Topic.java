package models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Topic {
    private final String name;
    private List<Message> messages;

    public Topic(String name) {
        this.name = name;
        messages = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}
