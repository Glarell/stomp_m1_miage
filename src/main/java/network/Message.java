package network;

/**
 * The type Message.
 */
public class Message {

    private String content;
    private int offset;


    /**
     * Instantiates a new Message.
     *
     * @param content the content
     * @param offset  the offset
     */
    public Message(String content, int offset) {
        this.content = content;
        this.offset = offset;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets offset.
     *
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets offset.
     *
     * @param offset the offset
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", offset=" + offset +
                '}';
    }
}
