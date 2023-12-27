package com.example.plannr;

import com.example.plannr.utils.UserEvent;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * this class represents teh hourly event used for the home screen weekly menu, and
 * contains the time and list of user events at that time
 * @author Zach R
 */
public class hourEvent {
    LocalTime time;
    ArrayList<UserEvent> events;

    /**
     * constructor for a new hour event with given date and time and the events passed in
     * @param time the time of the events occur
     * @param events the list of events for the hour
     */
    public hourEvent(LocalTime time, ArrayList<UserEvent> events) {
        this.time = time;
        this.events = events;
    }

    /**
     * gets the time of the hourEvent
     * @return local time of the hour event
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * sets the time of the hourEvent
     * @param time local time that you want the hour event to be set to
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * gets the list of events in teh hourEvent
     * @return list of events in the our event
     */
    public ArrayList<UserEvent> getEvents() {
        return events;
    }

    /**
     * sets teh events in the hourEvent
     * @param events events for the hour Event to be set to
     */
    public void setEvents(ArrayList<UserEvent> events) {
        this.events = events;
    }
}
