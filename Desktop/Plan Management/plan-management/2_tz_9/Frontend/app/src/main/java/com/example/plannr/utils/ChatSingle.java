package com.example.plannr.utils;

/**
 * This class holds the messages in a chat and whether or not the signed in user sent the message.
 * This class is used in the ChatAdapter ArrayList.
 * @author Spencer Thiele
 */
public class ChatSingle {
    private boolean sent;
    private String text;
    private String sender;


    public ChatSingle()
    {
        sent = true;
        text = "Example text";
        sender = "Not provided";
    }

    /**
     * ChatSingle constructor that automatically sets sent to true
     * @param text The message that is being sent
     */
    public ChatSingle(String text)
    {
        this.text = text;
        sent = true;
        sender = "Not Provided";
    }


    public ChatSingle(String text, boolean sent, String user)
    {
        this.text = text;
        this.sent = sent;
        sender = user;
    }

    /**
     * Getter for sent
     * @return sent
     */
    public boolean isSent()
    {
        return sent;
    }

    /**
     * Getter for text
     * @return The text in the message
     */
    public String GetText()
    {
        return text;
    }

    public String GetSender()
    {
        return sender;
    }
}
