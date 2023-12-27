package com.example.demo.users;

import com.example.demo.events.Alert;
import com.example.demo.events.Event;
import com.example.demo.events.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;

@Service
public class UserService {


    UserRepository userRepository;

    EventRepository eventRepository;

    @Autowired
    public UserService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new RuntimeException("User with that name already exists");
        }

        userRepository.save(user);
    }

    public List<Event> getOwnedEvents(long userId) {
        return userRepository.findById(userId).get().getOwnedEvents();
    }

    public void deleteAll () {
        userRepository.deleteAll();
    }

    public void joinEvent(long userId, long eventId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("Could not find user with id " + userId);
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new RuntimeException("Could not find event with id " + eventId);
        }

        Event event = eventRepository.getReferenceById(eventId);
        User user = userRepository.getReferenceById(userId);

        event.joinEvent(user);
        user.joinEvent(event);

        eventRepository.save(event);
        userRepository.save(user);
    }

    public Set<Event> getJoinedEvents(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("Could not find user with that id");
        }

        return userRepository.getReferenceById(userId).getJoinedEvents();
    }

}
