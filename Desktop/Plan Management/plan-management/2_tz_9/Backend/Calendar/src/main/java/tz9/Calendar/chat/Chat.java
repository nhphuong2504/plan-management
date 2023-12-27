package tz9.Calendar.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.chat.chatlogs.ChatLog;
import tz9.Calendar.events.Event;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Chat {

    @SequenceGenerator (name = "chat_Sequence", sequenceName = "chat_Sequence", allocationSize = 1)    //Generate sequence for this table
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "chat_sequence")
    private long id;

    // Many Chats can have Many users in a given chat.
    @ManyToMany (mappedBy = "joinedChats")
    private List<AppUser> usersInChat;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private AppUser chatOwner;

    // List for chats
    @OneToMany (fetch = FetchType.EAGER, mappedBy = "chat")
    private List<ChatLog> chatLogs;

    @OneToOne (mappedBy = "eventChat")
    @JsonIgnore
    private Event eventChat;

    /*
     * Default constructor for a Chat Object
     */
    public Chat() {
        chatOwner = null;
        eventChat = null;
    }

    /*
     * Constructor for creating a Chat Object.  A Chat must have at
     * least two users in the chat.
     */
    public Chat(AppUser appUserOne, AppUser appUserTwo) {
        chatOwner = appUserOne;
        usersInChat = new ArrayList<AppUser>();
        chatLogs = new ArrayList<>();
        usersInChat.add(appUserOne);
        usersInChat.add(appUserTwo);
        eventChat = null;
    }

    /*
     * Constructor for creating a chat with 2 Users and binding to an Event.
     *
     * @param appUserOne
     * @param appUserTwo
     * @param parentEvent
     */
    public Chat(AppUser appUserOne, AppUser appUserTwo, Event parentEvent) {
        usersInChat = new ArrayList<AppUser>();
        usersInChat.add(appUserOne);
        usersInChat.add(appUserTwo);
        this.chatOwner = appUserOne;
        this.eventChat = parentEvent;

    }

    /*
     * Constructor for creating a chat from an Event.
     *
     * @param chatOwner
     * @param parentEvent
     */
    public Chat(AppUser chatOwner, Event parentEvent) {
        usersInChat = new ArrayList<AppUser>();
        usersInChat.add(chatOwner);
        this.eventChat = parentEvent;
        this.chatOwner = chatOwner;
    }

    /*
     * Adds a user to a chat, called when an AppUser 'user' joins a chat.
     */
    public void addUser(AppUser user) {
        if (usersInChat.contains(user)) {
            throw new RuntimeException("Error: That User is already in the chat.");
        }
        usersInChat.add(user);
    }

    /*
     * Attempts to remove a user 'user' from a Chat, needs to have 'chatOwner' passed to prevent
     * unauthorized users from removing others.
     */
    public void removeUser(AppUser user) {
        if (!usersInChat.contains(user)) {
            throw new RuntimeException("Error: that User is not in the chat.");
        }
        usersInChat.remove(user);
    }

    public List<AppUser> getUsersInChat() {
        return usersInChat;
    }

    public void setUsersInChat(List<AppUser> usersInChat) {
        this.usersInChat = usersInChat;
    }

    /*
     * Sets an appUser to be Owner.
     */
    public void setOwner(AppUser appUser) {
        this.chatOwner = appUser;
    }

    /*
     * Getter for Chat Owner.
     */
    public AppUser getChatOwner() {
        return chatOwner;
    }

    public void addChatLog(ChatLog chatLog) {
        chatLogs.add(chatLog);
    }

    public void removeChatLog(ChatLog chatLog) {
        if (!chatLogs.contains(chatLog)) {
            throw new RuntimeException("Error: Chat does not exist in this Chat");
        }
        chatLogs.remove(chatLog);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ChatLog> getChatLogs() {
        return chatLogs;
    }

    public Event getEventChat() {
        return eventChat;
    }

    public void setEventChat(Event event) {
        this.eventChat = event;
    }

    public void removeUsersLogs(AppUser user) {
        for (ChatLog log : chatLogs) {
            if (log.getUser().equals(user)) {
                chatLogs.remove(log);
                log.unbindLog();
            }
        }
    }

    @Override
    public String toString() {
        return "Chat Id: " + id + ". Owner: " + chatOwner.getFirstName() + " " + chatOwner.getLastName();
    }
}
