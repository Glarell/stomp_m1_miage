package network;

/**
 * The type Subscription.
 */
public class Subscription {

    private String id;
    private String destination;
    private int cursor;

    /**
     * Instantiates a new Subscription.
     *
     * @param id          the id
     * @param destination the destination
     */
    public Subscription(String id, String destination) {
        this.id = id;
        this.destination = destination;
        this.cursor = 0;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets destination.
     *
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets destination.
     *
     * @param destination the destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Gets cursor.
     *
     * @return the cursor
     */
    public int getCursor() {
        return cursor;
    }

    /**
     * Sets cursor.
     *
     * @param cursor the cursor
     */
    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id='" + id + '\'' +
                ", destination='" + destination + '\'' +
                ", cursor=" + cursor +
                '}';
    }
}
