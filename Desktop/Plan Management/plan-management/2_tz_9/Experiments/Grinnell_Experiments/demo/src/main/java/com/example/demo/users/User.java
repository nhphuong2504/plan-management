package com.example.demo.users;


import com.example.demo.events.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @GeneratedValue
    @Id
    long id;

    private String name;

    @OneToMany(mappedBy = "owner",
            cascade = CascadeType.PERSIST)
    @JsonIgnore
    List<Event> ownedEvents;

    @ManyToMany
    @JoinTable(name = "users_join_events",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id"))
    @JsonIgnore
    private Set<Event> joinedEvents;

    public User() {
        name = "";
    }

    public User(String name) {
        this.name = name;
        ownedEvents = new ArrayList<Event>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEvent(Event e) {
        ownedEvents.add(e);
    }

    public List<Event> getOwnedEvents() {
        return ownedEvents;
    }

    public void joinEvent(Event e) {
        joinedEvents.add(e);
    }

    public Set<Event> getJoinedEvents() {
        return joinedEvents;
    }

    @Override
    public String toString() {
        return "User : " + name;
    }

}
