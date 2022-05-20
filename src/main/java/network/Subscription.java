package network;

public class Subscription {

    private String id;
    private String destination;
    private int cursor;

    public Subscription(String id, String destination){
        this.id = id;
        this.destination = destination;
        this.cursor = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCursor() {
        return cursor;
    }

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
