package models;

public abstract class Subscriber {
    private final long id;

    protected Subscriber(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public abstract void receiveMessage(Topic topic, Message message);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Subscriber that = (Subscriber) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
