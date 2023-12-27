package tz9.Calendar.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tz9.Calendar.appUser.AppUser;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import tz9.Calendar.chat.Chat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "events")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class  Event {

    @SequenceGenerator (name = "event_sequence", sequenceName = "event_sequence", allocationSize = 1)
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator =  "event_sequence")
    private long id;
    private String title;
    private String description;

    // Local Time and Dates used by declaration of frontend
    private LocalDate date;

    private LocalTime time;

    private String location;

    @ManyToMany (fetch = FetchType.EAGER)
    private Set<AppUser> participatingUsers;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    private Chat eventChat;

    //new fields for recurrence
    private RecurrenceType recurrenceType;

    private Integer recurrenceCount;

    // Many Events can be mapped to a single user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ownerId")
    AppUser owner;

    @JoinColumn(name = "tag")
    private int tag;

    // Default constructor for an Event
    public Event() {
        title = "";
        description = "";
        date = LocalDate.of(1, 1, 1);
        time = LocalTime.now();
        location = "";
        recurrenceType = RecurrenceType.NONE;
        recurrenceCount = 0;
        tag = 0;
    }

    /*
     * Note: Times must be posted as xx:xx:xx
     *       Dates must be posted as xxxx-xx-xx
     */

    public Event(String title, String description, LocalDate date, LocalTime time, String location, AppUser owner, int tag, RecurrenceType recurrenceType, Integer recurrenceCount) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.owner = owner;
        this.recurrenceType = recurrenceType;
        this.recurrenceCount = recurrenceCount;
        this.tag = tag;
    }

    public Event(String title, String description, LocalDate date, LocalTime time, String location, AppUser owner) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.owner = owner;
    }   

    public void updateEvent(Event event) {
        setTitle(event.getTitle());
        setDescription(event.getDescription());
        setDate((event.getDate()));
        setTime(event.getTime());
        setLocation(event.getLocation());
        setRecurrenceType(event.getRecurrenceType());
        setRecurrenceCount(event.getRecurrenceCount());
        setTag(event.getTag());
    }

    public void joinEvent(AppUser user) {
        if (!participatingUsers.contains(user)) {
            participatingUsers.add(user);
        } else {
            throw new RuntimeException("Error: User already joined the event.");
        }
    }

    public void removeParticipant(AppUser user) {
        if (!participatingUsers.contains(user)) {
            throw new RuntimeException("Error: User is not participating in this event.");
        }

        participatingUsers.remove(user);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser newOwner) {
        this.owner = newOwner;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Set<AppUser> getParticipatingUsers() {
        return participatingUsers;
    }

    public long getId() {
        return id;
    }

    public Chat getEventChat() {
        return eventChat;
    }

    public void setEventChat(Chat eventChat) {
        this.eventChat = eventChat;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(RecurrenceType recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public Integer getRecurrenceCount() {
        return recurrenceCount;
    }

    public void setRecurrenceCount(Integer recurrenceCount) {
        this.recurrenceCount = recurrenceCount;
    }

    public void setParticipatingUsers(Set<AppUser> participatingUsers) {
        this.participatingUsers = participatingUsers;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", owner=" + owner +
                ", participatingUsers=" + participatingUsers +
                ", eventChat=" + eventChat +
                ", recurrenceType=" + recurrenceType +
                ", recurrenceCount=" + recurrenceCount +
                ", location='" + location + '\'' +
                '}';
    }
}
