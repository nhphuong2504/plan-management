package tz9.Calendar.chat.chatlogs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.chat.Chat;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer")
public class ChatLog {

    @SequenceGenerator (name = "chat_log_Sequence", sequenceName = "chat_log_Sequence", allocationSize = 1)    //Generate sequence for this table
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "chat_log_sequence")
    long id;

    private String message;

    private boolean edited;

    // Needs To be mapped to a user -> one user can have many chats
    @ManyToOne
    private AppUser user;

    // Also mapped to Chats -> One chat can have many logs
    // JsonIgnored to prevent recursively calling itself.
    @ManyToOne
    @JsonIgnore
    private Chat chat;

    public ChatLog() {
        user = null;
        message = "";
        chat = null;
        edited = false;
    }

    public ChatLog(AppUser user, String message, Chat chat) {
        this.user = user;
        this.message = message;
        this.chat = chat;
        edited = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void editLog(String newMessage, AppUser appUser) {
        setMessage(newMessage);
        setEdited(true);
    }

    public void unbindLog() {
        setChat(null);
        setUser(null);
    }

    public long getId() {
        return id;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }

}
