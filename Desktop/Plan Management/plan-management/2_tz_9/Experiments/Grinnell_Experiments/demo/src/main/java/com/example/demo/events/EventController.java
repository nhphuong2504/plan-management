package com.example.demo.events;

import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    EventService eventService;

    @Autowired
    UserRepository userRepository;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/events")
    public List<Event> returnString() {
        return eventService.getEvents();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/events/add{id}")
    public Alert postRequest(@RequestBody Event event, @PathVariable("id") long id) {
        eventService.postRequest(event, id);
        return new Alert("Post Successful");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/events/{id}")
    public Alert deleteRequest(@PathVariable("id") long id)  {
        eventService.deleteRequest(id);
        return new Alert("Delete Successful");
    }

    @GetMapping(value = "/events/{id}")
    public Event getEvent(@PathVariable("id") long id) {
        return eventService.getEvent(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/events/{id}")
    public Alert putRequest(@PathVariable("id") long id, @RequestBody Event event) {
        eventService.putRequest(id, event);
        return new Alert ("Put Successful");
    }

    @DeleteMapping(value = "/events/deleteall")
    public void deleteAll() {
        eventService.deleteAll();
    }
}
