package com.example.demo.users;

import com.example.demo.events.Alert;
import com.example.demo.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    UserService userService;

    public UserController() {}

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(value = "/users/add")
    public Alert addUser(@RequestBody User user) {
        userService.addUser(user);
        return new Alert("Post Successful");
    }

    @GetMapping(value = "/users/{id}")
    public List<Event> getOwnedEvents(@PathVariable("id") long id) {
        List<Event> events = userService.getOwnedEvents(id);
        return events;
    }

    @DeleteMapping(value = "/users/deleteAll")
    public void deleteAll() {
        userService.deleteAll();
    }

    @PostMapping(value = "/user/{userId}/joinEvent/{eventId}")
    public Alert joinEvent(@PathVariable("userId") long userId, @PathVariable("eventId") long eventId) {
        userService.joinEvent(userId, eventId);
        return new Alert("Joined Event");
    }

    @GetMapping(value = "/user/{userId}/joinEvent")
    public Set<Event> getEvents(@PathVariable("userId") long userId) {
        return userService.getJoinedEvents(userId);
    }
}
