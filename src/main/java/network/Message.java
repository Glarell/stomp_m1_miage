package network;

public class Message {

    private String content;
    private int offset;


    public Message(String content, int offset) {
        this.content = content;
        this.offset = offset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
