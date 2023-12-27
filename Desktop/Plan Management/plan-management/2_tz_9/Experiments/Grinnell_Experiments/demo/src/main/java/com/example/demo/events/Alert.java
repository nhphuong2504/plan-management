package com.example.demo.events;

public class Alert {
    private String message;

    public Alert(String message) {
        this.message = message;
    }

    public Alert() {
        this.message = "Default Message";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String newMessage) {
        this.message = newMessage;
    }
}
