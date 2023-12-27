package com.example.demo.events;


import com.example.demo.users.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue
    private long id;

    private String title;
    private String description;

    // Date and Time the event will be held -> formatted like {month, day, hr, min}
    private int month;
    private int day;
    private int hour;
    private int minute;

    @ManyToOne (cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ownerId")
    private User owner;

    @ManyToMany(mappedBy = "joinedEvents")
    private Set<User> participUsers;

    public Event(String title, String description, int month, int day, int hour, int minute) {
        this.title = title;
        this.description = description;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public Event(String title, String description, int month, int day, int hour, int minute, User owner) {
        this.title = title;
        this.description = description;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.owner = owner;
    }

    // Default Constructor
    public Event() {
        title = "";
        description = "";
        month = -1;
        day = -1;
        hour = -1;
        minute = -1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public User getOwner() {
        return owner;
    }

    public void joinEvent(User user) {
        if (participUsers.contains(user)) {
            throw new RuntimeException("User is already participating");
        }

        participUsers.add(user);
    }

    // If any parameters of event are null/-1/empty string, do not change values
    public void updateEvent(Event event) {
        if (event.getTitle() != "") {
            this.title = event.getTitle();
        }

        if (event.getDescription() != "") {
            this.description = event.getDescription();
        }

        if (event.getMonth() != -1) {
            this.month = event.getMonth();
        }

        if (event.getDay() != -1) {
            this.day = event.getDay();
        }
        if (event.getHour() != -1) {
            this.hour = event.getHour();
        }

        if (event.getMinute() != -1) {
            this.minute = event.getMinute();
        }
    }

    @Override
    public String toString() {
        return "Event: " + title + ", Description: " + description + ", Date: " + month + ", " + day + " - " + hour + ":" + minute;
    }
}
