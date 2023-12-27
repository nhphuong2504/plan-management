package tz9.Calendar.appUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tz9.Calendar.chat.Chat;
import tz9.Calendar.chat.chatlogs.ChatLog;
import tz9.Calendar.events.Event;
import tz9.Calendar.friends.Friend;
import tz9.Calendar.music.Music;
import tz9.Calendar.profilepicture.ProfilePicture;
import tz9.Calendar.registration.token.ConfirmationToken;

import javax.persistence.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
//@JsonIgnoreProperties("hibernateLazyInitializer")
public class AppUser implements UserDetails {

    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)    //Generate sequence for this table
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @JsonIgnore                 // JsonIgnored to prevent password hash from being sent with an AppUser Object
    private String password;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

    private Boolean locked = false;
    private Boolean enabled = false;

    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Friend> friends;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ConfirmationToken> confirmationTokens;

    // Note: Json ignore is used to prevent infinite recursion when passing chats.
    // Many Users can join many Chats
    @ManyToMany
    @JoinTable (name = "user_join_chats",
        joinColumns = @JoinColumn(name = "appUserId"),
        inverseJoinColumns = @JoinColumn(name = "chatId"))
    @JsonIgnore
    private Set<Chat> joinedChats;

    // One user can own many Chats
    @OneToMany (mappedBy = "chatOwner")
    @JsonIgnore
    private List<Chat> ownedChats;

    @OneToMany (mappedBy = "user")
    @JsonIgnore
    private List<ChatLog> sentChats;
    // Adding OneToMany Mapping for Events Owner
    // One person can own Many Events
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.ALL, CascadeType.REMOVE}, orphanRemoval = true)
    @JsonIgnore
    private List<Event> eventList;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Event> ownedEvents;

    // Adding ManyToMany Mapping for Events *----* App Users
    // Many Events can have Many participating AppUsers
    @ManyToMany(fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "user_join_events",
            joinColumns = @JoinColumn(name = "appUserId"),
            inverseJoinColumns = @JoinColumn(name = "eventId"))
    @JsonIgnore
    private Set<Event> joinedEvents;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChatLog> chatLogs;

    public List<ChatLog> getChatLogs() {
        return chatLogs;
    }

    @ManyToOne
    @JoinColumn (name = "pref_music")
    @JsonIgnore
    private Music preferencedMusic;

    @ManyToOne
    @JoinColumn (name = "profile_picture")
    @JsonIgnore
    private ProfilePicture profilePicture;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<Authority> authorities;


    public AppUser(String firstName,
            String lastName,
            String email,
            String password,
            AppUserRole appUserRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Adding Chat object to AppUserList
    public void joinChat(Chat chat) {
        if (joinedChats.contains(chat)) {
            throw new RuntimeException("Error: User has already joined this chat.");
        }
        joinedChats.add(chat);
    }

    // Remove chat from list
    public void leaveChat(Chat chat) {
        if (!joinedChats.contains(chat)) {
            throw new RuntimeException("Error: User is not in this chat.");
        }
        joinedChats.remove(chat);
    }

    // Called when user creates a new Chat
    public void addOwnedChat(Chat chat) {
        if (ownedChats.contains(chat)) {
            throw new RuntimeException("Error: Chat is already in ownedChats.");
        }
        ownedChats.add(chat);
    }

    // Called when user attempts to delete an owned chat
    public void deleteOwnedChat(Chat chat) {
        if (!ownedChats.contains(chat)) {
            throw new RuntimeException("Error: User does not own this chat.");
        }
        ownedChats.remove(chat);
    }

    public void addChatLog(ChatLog chatLog) {
        if (sentChats.contains(chatLog)) {
            throw new RuntimeException("Error: ChatLog already exists in Chat");
        }
        sentChats.add(chatLog);
    }

    public void updateChatLog(ChatLog chatLog, long chatLogId) {
        ChatLog oldLog = findChatLogById(chatLogId) ;
        if (oldLog.equals(null)) {
            throw new RuntimeException("Error: ChatLog with id " + chatLogId + " does not exist in chat.");
        }

        oldLog.editLog(chatLog.getMessage(), oldLog.getUser());
    }

    public void deleteChatLog(ChatLog chatLog, long chatLogId) {
        ChatLog log = findChatLogById(chatLogId);
        if (log.equals(null)) {
            throw new RuntimeException("Error: ChatLog with id " + chatLogId + "does not exist.");
        }

        sentChats.remove(chatLog);
    }

    public List<ChatLog> getSentChats() {
        return sentChats;
    }

    // Helper method to find a chat in the sentChats.
    // Should run in O(n), but if not present, the method should not be called.
    private ChatLog findChatLogById(long chatLogId) {
        for (int i = 0; i < sentChats.size(); i++) {
            if (sentChats.get(i).getId() == chatLogId) {
                return sentChats.get(i);
            }
        }
        return null;
    }
    public void addEvent(Event e) {
        eventList.add(e);
    }

    public void deleteEvent(Event e) {
        eventList.remove(e);
    }

    public void joinEvent(Event e) {
        joinedEvents.add(e);
    }

    public void leaveEvent(Event e) {
        joinedEvents.remove(e);
    }
    public List<Event> getOwnedEvents() {
        return ownedEvents;
    }
    public Long getId() {
        return id;
    }

    public Set<Event> getJoinedEvents() {
        return joinedEvents;
    }

    public Music getPreferencedMusic() {
        return preferencedMusic;
    }

    public void setPreferencedMusic(Music preferencedMusic) {
        this.preferencedMusic = preferencedMusic;
    }

    public void setProfilePicture(ProfilePicture pfp) {
        this.profilePicture = pfp;
    }

    public Set<Friend> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friend> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "" + id + ": " + firstName + " " + lastName;
    }
}