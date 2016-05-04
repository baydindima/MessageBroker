package models;

import java.util.Date;

public class Message {
    private final String title;
    private final User author;
    private final String text;
    private final Date date;

    public Message(String title, User author, String text, Date date) {
        this.title = title;
        this.author = author;
        this.text = text;
        this.date = new Date();
    }

    public String getTitle() {
        return title;
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }
}
