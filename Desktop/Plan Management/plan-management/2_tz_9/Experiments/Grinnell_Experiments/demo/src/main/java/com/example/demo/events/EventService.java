package com.example.demo.events;

import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private ArrayList<Event> eventList;

    private EventRepository eventRepository;

    private UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /*
    public EventService () {
        eventList = new ArrayList<Event>();
        eventList.add(new Event("1", "ex Description", 4, 3, 6,20));
        eventList.add(new Event("2", "ex Description", 4, 78, 6,20));
    }*/

    public List<Event> getEvents(){
        return eventRepository.findAll();
    }

    public void postRequest(Event event) {
        Optional<Event> findEventByName = eventRepository.findEventByName(event.getTitle());

        // Check to see if event with same title already exists in DB
        if (findEventByName.isPresent()) {
            throw new RuntimeException("Event Title already Exists");
        }

//        if (!event.getOwner().getName().equals("Jeffrey")) {
//            throw new RuntimeException("What the fuck");
//        }

        eventRepository.save(event);
    }

    public void postRequest(Event event, long userId) {
        Optional<Event> findEventByName = eventRepository.findEventByName(event.getTitle());

        // Check to see if event with same title already exists in DB
        if (findEventByName.isPresent()) {
            throw new RuntimeException("Event Title already Exists");
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("User with that id doesn't exist");
        }

        User user = userRepository.findById(userId).get();
        event.setOwner(user);

        user.addEvent(event);
        userRepository.save(user);
        eventRepository.save(event);
    }

    public Event getEvent(long id) {
        return eventRepository.getReferenceById(id);
    }

    public void deleteRequest(long id) {
        // Check to see if an object is in table
        Optional<Event> findEvent = eventRepository.findById(id);

        // If not, throw exception
        if (findEvent.isEmpty()) {
            throw new RuntimeException("ID is not present in Table");
        }

        // If present, then remove from table
        eventRepository.deleteById(id);
    }

    @Transactional
    public void putRequest(long id, Event event) {
        // Check to see if event with specified ID exists
        Optional<Event> findEvent = eventRepository.findById(id);

        // If not, throw error
        if (findEvent.isEmpty()) {
            throw new RuntimeException("Item with specified ID is not present");
        }


        // Checks to see if there's another event in table with same name
        if (eventRepository.findEventByName(event.getTitle()).isPresent() && eventRepository.findEventByName(event.getTitle()).get().getId() != id) {
            throw new RuntimeException("An event with that name already exists");
        }

        Event oldEvent = eventRepository.findById(id).get();
        oldEvent.updateEvent(event);
    }

    public void deleteAll() {
        eventRepository.deleteAll();
    }

}
