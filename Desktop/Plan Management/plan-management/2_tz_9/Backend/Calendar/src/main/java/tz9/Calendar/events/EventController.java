package tz9.Calendar.events;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import tz9.Calendar.alert.Alert;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/event")
public class EventController {

    EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Gets all events present in repository.
     *
     * @return All events in repository.
     */
    @GetMapping(value = "/getEvents")
    @Operation(description = "Gets all events present in repository.")
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    /**
     * Gets a certain event in the repository by id.
     *
     * @param eventId - ID of event to get.
     * @return Event with id eventId.
     */
    @GetMapping(value = "/getEvents/event={eventId}")
    @Operation(description = "Gets a certain event in the repository by id.")
    public Event getEvent(@PathVariable("eventId") long eventId) {
        return eventService.getEvent(eventId);
    }

    /**
     * Gets all events a user has joined.
     *
     * @param userId - User to get events from.
     * @return All event a user is participating in.
     */
    @GetMapping(value = "/getEvents/joinedEvents/user={userId}")
    @Operation(description = "Gets all events a user has joined.")
    public Set<Event> getUserEvents(@PathVariable("userId") long userId) {
        return eventService.getUserEvents(userId);
    }

    /**
     * Gets all events a user owns.
     *
     * @param userId - User to get owned events from.
     * @return All event a user owns.
     */
    @GetMapping(value = "/getEvents/ownedEvents/user={userId}")
    @Operation(description = "Gets all events a user owns.")
    public List<Event> getOwnedEvents(@PathVariable("userId") long userId) {
        return eventService.getOwnedEvents(userId);
    }

    /**
     * Posts an event of body 'event' passing user with id 'id' as owner.
     *
     * @param event - Event to post.
     * @param userId - User that is posting an event.
     * @return Message saying post is successful.
     */
    @PostMapping(value = "/postEvent/event/add/user={userId}")
    @Operation(description = "Posts an event of body 'event' passing user with id 'id' as owner.")
    public Alert postEvent(@RequestBody Event event, @PathVariable("userId") long userId) {
        eventService.postEvent(event, userId);
        return new Alert("Post Successful");
    }

    /**
     * Overwrites event with id 'eventId' with requestBody Event.  Will fail if user with id 'userId' is not owner.
     *
     * @param eventId - Event to overwrite.
     * @param userId - User attempting modification.
     * @param event - New event to be saved.
     * @return Message confirming success.
     */
    @PostMapping(value = "/modifyEvent/event={eventId}/user={userId}/")
    @Operation(description = "Overwrites event with id 'eventId' with requestBody Event.  Will fail if user with id 'userId' is not owner.")
    public Alert putEvent(@PathVariable("eventId") long eventId, @PathVariable("userId") long userId, @RequestBody Event event) {
        eventService.putEvent(eventId, userId, event);
        return new Alert("Put Successful");
    }

    /**
     * Modifies Event by title.
     *
     * @param eventTitle - Title of event to overwrite.
     * @param userId - User making change.
     * @param event - Event to overwrite.
     * @return Confirmation message.
     */
    @PutMapping(value = "/modifyEventByTitle/user={userId}/event={eventTitle}")
    @Operation(description = "Modifies Event by title.")
    public Alert putEvent(@PathVariable("eventTitle") String eventTitle, @PathVariable("userId") long userId, @RequestBody Event event) {
        eventService.putEventByTitle(eventTitle, userId, event);
        return new Alert("Put Successful");
    }

    /**
     * User with id 'userId' attempts to delete event with id 'eventId'.  Will fail if user is not owner.
     *
     * @param eventId - Event to delete.
     * @param userId - User attempting delete.
     * @return Confirmation message.
     */
    @DeleteMapping(value = "/deleteEvent/event={eventId}/user={userId}")
    @Operation(description = "User with id 'userId' attempts to delete event with id 'eventId'.  Will fail if user is not owner.")
    public Alert deleteEvent(@PathVariable("eventId") long eventId, @PathVariable("userId") long userId) {
        eventService.deleteEvent(eventId, userId);
        return new Alert("Delete Successful");
    }

    /**
     * Deletes event by its title.
     *
     * @param eventTitle - Title of event to delete.
     * @param userId - User making delete request.
     * @return Confirmation message.
     */
    @DeleteMapping(value = "/deleteEventByTitle/event={eventTitle}/user={userId}")
    @Operation(description = "Deletes event by its title.")
    public Alert deleteEvent(@PathVariable("eventTitle") String eventTitle, @PathVariable("userId") long userId) {
        eventService.deleteEventByTitle(eventTitle, userId);
        return new Alert("Delete Successful.");
    }

    /**
     * Attempts to add User with id 'userId' to participating list in event with id 'eventId'.
     *
     * @param eventId - Event to join.
     * @param userId - User attempting to join an event.
     * @return Confirmation message.
     */
    @PostMapping (value = "/joinEvent/event={eventId}/user={userId}")
    @Operation(description = "Attempts to add User with id 'userId' to participating list in event with id 'eventId'.")
    public Alert joinEventAsUser(@PathVariable("eventId") long eventId, @PathVariable("userId") long userId) {
        eventService.joinEvent(userId, eventId);
        return new Alert("Successfully joined event.");
    }

    /**
     * Removes a user with id = {userId} from event with id = {eventId}.
     *
     * @param eventId -  Event to leave.
     * @param userId - User attempting to leave event.
     * @return Confirmation message.
     */
    @DeleteMapping (value = "/leaveEvent/event={eventId}/user={userId}")
    @Operation(description = "Removes a user with id = {userId} from event with id = {eventId}.")
    public Alert leaveEvent(@PathVariable("eventId") long eventId, @PathVariable("userId") long userId) {
        eventService.leaveEvent(eventId, userId);
        return new Alert("Successfully left event");
    }

    /**
     * Sets owner of event of id 'eventId' to AppUser with id 'userId'.
     *
     * @param eventId Event to set owner of.
     * @param userId - User who will be new owner.
     */
    @PostMapping (value = "/setOwner/event={eventId}/userId={userId}")
    @Operation(description = "Sets owner of event of id 'eventId' to AppUser with id 'userId'.")
    public void setOwner(@PathVariable("eventId") long eventId, @PathVariable("userId") long userId) {
        eventService.setOwner(eventId, userId);
    }

}
