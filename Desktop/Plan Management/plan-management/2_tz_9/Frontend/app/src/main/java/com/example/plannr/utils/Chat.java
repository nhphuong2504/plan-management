package com.example.plannr.utils;
/**
 * This class links a chat id to the user name of the other user in the chat for displaying and server request purposes
 * @author Spencer Thiele
 */
public class Chat {
    private int chatID;
    private String userName;

    /**
     * Constructor for a chat
     * @param id The chat id that will be used in a server request to retrieve messages
     * @param name The name of the other user in the chat
     */
    public Chat(int id, String name)
    {
        chatID = id;
        userName = name;
    }
    /**
     * this is a getter for chat id
     * @return the chat id
     */
    public int GetID()
    {
        return chatID;
    }

    /**
     * this is a getter for username
     * @return the username
     */
    public String GetName()
    {
        return userName;
    }
}
