package tz9.Calendar.websocket;

public class Message {

    private String userSending;
    private String message;

    private long sendingID;

    private String sender;
    private String content;
    private long userId;

    public Message() {
        this.userSending = "";
        this.message = "";
    }

    public Message(String from, String message, long id) {
        this.userSending = from;
        this.message = message;
        this.sendingID = id;
    }

    // New constructor for online status messages
    public Message(String sender) {
        this.sender = sender;
    }

    public String getFrom() {
        return userSending;
    }

    public void setFrom(String from) {
        this.userSending = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return sendingID;
    }

    public void setId(long id) {
        this.sendingID = id;
    }

}
