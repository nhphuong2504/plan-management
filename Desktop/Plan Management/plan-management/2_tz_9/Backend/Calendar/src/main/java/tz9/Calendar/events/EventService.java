package tz9.Calendar.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserRepository;
import tz9.Calendar.appUser.AppUserRole;
import tz9.Calendar.chat.Chat;
import tz9.Calendar.chat.ChatRepository;
import tz9.Calendar.chat.chatlogs.ChatLog;
import tz9.Calendar.chat.chatlogs.ChatLogRepository;
import tz9.Calendar.email.EmailService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


@Service
public class EventService {

    EventRepository eventRepository;

    AppUserRepository appUserRepository;

    ChatRepository chatRepository;

    ChatLogRepository chatLogRepository;

    private static final Logger logger = Logger.getLogger(EventService.class.getName());

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private EmailService emailService;

    @Autowired
    public EventService(EventRepository eventRepository, AppUserRepository appUserRepository, ChatRepository chatRepository, ChatLogRepository chatLogRepository) {
        this.eventRepository = eventRepository;
        this.appUserRepository = appUserRepository;
        this.chatRepository = chatRepository;
        this.chatLogRepository = chatLogRepository;
    }

    /**
     * Gets all events.
     *
     * @return List of All Events.
     */
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    /**
     * Gets a certain event of id 'eventId'.
     *
     * @param eventId - ID of an event to get.
     * @return An event with id 'eventId'.
     */
    public Event getEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Error: Event with id: " + eventId + "does not exist.");
        }

        return eventRepository.getReferenceById(eventId);
    }

    /**
     * Gets the events that a user is attending.
     *
     * @param userId - ID of user whose events to get.
     * @return Events a user is attending.
     */
    public Set<Event> getUserEvents(long userId) {
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id: " + userId + " could not be found.");
        }
        return appUserRepository.getReferenceById(userId).getJoinedEvents();
    }

    /**
     * Gets the events a user owns.
     *
     * @param userId - A user's id to get owned events
     * @return Owned Events of a user.
     */
    public List<Event> getOwnedEvents(long userId) {
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id: " + userId + " could not be found.");
        }
        return appUserRepository.getReferenceById(userId).getEventList();
    }

    /**
     * Posts an event with owner of id 'userId'.
     *
     * @param event - An event to be posted.
     * @param userId - User who is posting the event.
     */
    public void postEvent(Event event, long userId) {
        // Search for event in db
        Optional<Event> findEvent = eventRepository.findByTitle(event.getTitle());

        // If present, throw error
        if (findEvent.isPresent()) {
            throw new RuntimeException("Error: Event with same title exists");
        }

        if (appUserRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("Error: User with that ID does not exist");
        }

        AppUser owner = appUserRepository.findById(userId).get();

        event.setOwner(owner);
        owner.addEvent(event);
        eventRepository.save(event);
        appUserRepository.save(owner);

        Chat chat = new Chat(owner, event);
        owner.joinChat(chat);
        event.setEventChat(chat);
        chat.setOwner(owner);
        chatRepository.save(chat);

        Event recurringEvent = event;

        switch (event.getRecurrenceType()) {
            case DAILY:
                for (int i = 1; i < event.getRecurrenceCount(); i++) {
                    recurringEvent = new Event(event.getTitle(), event.getDescription(), event.getDate().plusDays(i), event.getTime(), event.getLocation(), event.getOwner(), event.getTag(), event.getRecurrenceType(), event.getRecurrenceCount());
                    recurringEvent.setOwner(owner);
                    owner.addEvent(recurringEvent);
                    eventRepository.save(recurringEvent);
                    appUserRepository.save(owner);

                    Chat recurringChat = new Chat(owner, recurringEvent);
                    owner.joinChat(recurringChat);
                    recurringEvent.setEventChat(recurringChat);
                    recurringChat.setOwner(owner);
                    chatRepository.save(recurringChat);

                }
                break;
            case WEEKLY:
                for (int i = 1; i < event.getRecurrenceCount(); i++) {
                    recurringEvent = new Event(event.getTitle(), event.getDescription(), event.getDate().plusWeeks(i), event.getTime(), event.getLocation(), event.getOwner(), event.getTag(), event.getRecurrenceType(), event.getRecurrenceCount());
                    recurringEvent.setOwner(owner);
                    owner.addEvent(recurringEvent);
                    eventRepository.save(recurringEvent);
                    appUserRepository.save(owner);

                    Chat recurringChat = new Chat(owner, recurringEvent);
                    owner.joinChat(recurringChat);
                    recurringEvent.setEventChat(recurringChat);
                    recurringChat.setOwner(owner);
                    chatRepository.save(recurringChat);

                }
                break;
            case MONTHLY:
                for (int i = 1; i < event.getRecurrenceCount(); i++) {
                    recurringEvent = new Event(event.getTitle(), event.getDescription(), event.getDate().plusMonths(i), event.getTime(), event.getLocation(), event.getOwner(), event.getTag(), event.getRecurrenceType(), event.getRecurrenceCount());
                    recurringEvent.setOwner(owner);
                    owner.addEvent(recurringEvent);
                    eventRepository.save(recurringEvent);
                    appUserRepository.save(owner);

                    Chat recurringChat = new Chat(owner, recurringEvent);
                    owner.joinChat(recurringChat);
                    recurringEvent.setEventChat(recurringChat);
                    recurringChat.setOwner(owner);
                    chatRepository.save(recurringChat);

                }
                break;
            default:
                break;
        }
    }

    /**
     * Attempts to delete an event with id 'eventId'.  Checks to see if userId is the owner of the event or if userId is admin.
     *
     * @param eventId - Event to Delete.
     * @param userId - User attempting to delete event.
     */
    public void deleteEvent(long eventId, long userId) {
        // Checks to see if the event exists, if the user exists, and if the user id passed is owner of the event.
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Error: Event with id: " + eventId + "does not exist.");
        }

        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id: " + userId + " could not be found.");
        }

        AppUser user = appUserRepository.getReferenceById(userId);
        Event event = eventRepository.getReferenceById(eventId);

        if (!event.getOwner().equals(user) && user.getAppUserRole() != AppUserRole.ADMIN) {
            throw new RuntimeException("Error: You do not have sufficient permissions to delete this event");
        }

        deleteEvent(event);
    }

    /**
     * Attempts to find and delete an event by its title.
     *
     * @param eventTitle - Title of event to delete.
     * @param userId - User attempting delete.
     */
    public void deleteEventByTitle(String eventTitle, long userId) {
        List<Event> findAllByTitle = eventRepository.findAllByTitle(eventTitle);

        if (findAllByTitle.size() != 1) {
            throw new IllegalArgumentException("Error: The request is unable to be processed.");
        }

        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id: " + userId + " could not be found.");
        }

        AppUser user = appUserRepository.getReferenceById(userId);
        Event event = findAllByTitle.get(0);

        if (!event.getOwner().equals(user) && user.getAppUserRole() != AppUserRole.ADMIN) {
            throw new RuntimeException("Error: You do not have sufficient permissions to delete this event");
        }

        deleteEvent(event);
    }

    /**
     * Modifies an existing event of id 'id' to match the input parameter Event event.
     *
     * @param eventId - Event to modify.
     * @param userId - User attempting modification.
     * @param event - New event to replace former event.
     */
    public void putEvent(long eventId, long userId, Event event) {
        // If not, throw exception
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Error: Event not found.");
        }

        // Check if event with same title exists in DB
        // if not, throw exception
        List<Event> findAllByTitle = eventRepository.findAllByTitle(event.getTitle());

        for (Event e : findAllByTitle) {
            if (e.getId() != eventId) {
                throw new IllegalArgumentException("Error: An event with that title already Exists");
            }
        }

        Event modEvent = eventRepository.getReferenceById(eventId);
        AppUser modEventOwner = modEvent.getOwner();

        // Checks to see if user Id passed is the same as the owner's id
        if (modEventOwner.getId() != userId && appUserRepository.getReferenceById(userId).getAppUserRole() != AppUserRole.ADMIN) {
            throw new RuntimeException("Error: You do not have sufficient permissions to modify this event.");
        }
        event.setParticipatingUsers(modEvent.getParticipatingUsers());
        editEvent(modEvent, event);
    }

    /**
     * Modifies an existing event with title 'eventTitle' to match input parameter event.
     *
     * @param eventTitle - Title of event to modify.
     * @param userId - User attempting modification.
     * @param event - Event to replace former event.
     */
    public void putEventByTitle(String eventTitle, long userId, Event event) {

        List<Event> findAllByTitle = eventRepository.findAllByTitle(eventTitle);

        if (findAllByTitle.size() != 1) {
            throw new IllegalArgumentException("Error: The request is unable to be processed.");
        }

        Event modEvent = findAllByTitle.get(0);
        AppUser modEventOwner = modEvent.getOwner();

        // Checks to see if user Id passed is the same as the owner's id
        // or if user is Admin
        if (modEventOwner.getId() != userId && appUserRepository.getReferenceById(userId).getAppUserRole() != AppUserRole.ADMIN) {
            throw new RuntimeException("Error: You do not have sufficient permissions to modify this event.");
        }
        event.setParticipatingUsers(modEvent.getParticipatingUsers());
        editEvent(modEvent, event);
    }

    /**
     * Attempts to have a user with id userId join an event with id eventId.
     *
     * @param userId - User to join event.
     * @param eventId - Event that a user is attempting to join.
     */
    public void joinEvent(long userId, long eventId) {
        // Try to get User and Event as Optionals
        Optional<AppUser> optionalUser = appUserRepository.findById(userId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        // Check if userId is valid
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Error: User with id " + userId + " was not found.");
        }

        // Check if eventId is valid
        if (optionalEvent.isEmpty()) {
            throw new RuntimeException("Error: Event with id " + eventId + " was not found.");
        }

        // If checks are passed, have user join event and event add user to Set.
        AppUser user = optionalUser.get();
        Event event = optionalEvent.get();
        Chat eventChat = event.getEventChat();

        user.joinEvent(event);
        event.joinEvent(user);
        if (eventChat != null) {
            eventChat.addUser(user);
        } else {
            eventChat = new Chat(event.getOwner(), event);
            chatRepository.save(eventChat);
            event.setEventChat(eventChat);
        }
        user.joinChat(eventChat);
        appUserRepository.save(user);
        eventRepository.save(event);
    }

    /**
     * Removes a user from participating in an event.
     *
     * @param eventId - Event to leave.
     * @param userId - User attempting to leave event.
     */
    public void leaveEvent(long eventId, long userId) {
        // Check valid IDs
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Error: Event with id " + eventId + " was not found.");
        }

        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " was not found.");
        }

        // Get Objects from optional
        Event event = eventRepository.getReferenceById(eventId);
        AppUser user = appUserRepository.getReferenceById(userId);
        Chat eventChat = event.getEventChat();

        // if Valid, check if user is in event
        if (!event.getParticipatingUsers().contains(user)) {
            throw new RuntimeException("Error: User is not participating in event with id " + eventId);
        }

        // If checks are passed, remove user from event and vice versa
        event.removeParticipant(user);
        user.leaveEvent(event);

        if (eventChat != null) {
            eventChat.removeUsersLogs(user);
            eventChat.removeUser(user);
            user.leaveChat(eventChat);
            chatRepository.save(eventChat);
        }


        eventRepository.save(event);
        appUserRepository.save(user);
    }

    /**
     * Sets owner of event with id 'eventId' to user with id 'userId'.
     *
     * @param eventId - Event to change owner of.
     * @param userId - User who will be new owner.
     */
    public void setOwner(long eventId, long userId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Error: Event with id " + eventId + " was not found.");
        }

        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " was not found.");
        }

        Event event = eventRepository.getReferenceById(eventId);
        AppUser user = appUserRepository.getReferenceById(userId);

        event.setOwner(user);
        user.addEvent(event);

        eventRepository.save(event);
        appUserRepository.save(user);
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    /*
     * Helper event for deleting an event.
     */
    private void deleteEvent(Event event) {
        // Else Delete
        event.setOwner(null);

        // Need to save users IMMEDIATELY after modifying
        for (AppUser u : event.getParticipatingUsers()) {
            u.leaveEvent(event);
            appUserRepository.save(u);
        }

        Chat chat = event.getEventChat();

        if (chat != null) {
            AppUser chatOwner = chat.getChatOwner();

            chatOwner.deleteOwnedChat(chat);
            chat.setOwner(null);
            for (AppUser user : chat.getUsersInChat()) {
                user.leaveChat(chat);
                appUserRepository.save(user);
            }

            for (ChatLog chatLog : chat.getChatLogs()) {
                chatLog.unbindLog();
                chatLogRepository.save(chatLog);
                chatLogRepository.delete(chatLog);
            }

            chat.getUsersInChat().clear();
            chat.getChatLogs().clear();
            event.setEventChat(null);
            chatRepository.save(chat);
            eventRepository.save(event);
            chatRepository.delete(chat);
        }

        event.getParticipatingUsers().clear();

        eventRepository.save(event);
        appUserRepository.saveAllAndFlush(appUserRepository.findAll());

        // Finally Delete
        eventRepository.delete(event);
    }

    /*
     * Helper method to edit an Event
     */
    @Transactional
    private void editEvent(Event oldEvent, Event newEvent) {
        oldEvent.updateEvent(newEvent);
        eventRepository.save(oldEvent);         // Saves old event after updating

        if (newEvent.getParticipatingUsers() != null) {
            for (AppUser user : newEvent.getParticipatingUsers()) {
                emailService.sendEventUpdateNotification(user.getEmail(), "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Event Reminder</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            font-size: 16px;\n" +
                        "            line-height: 1.5;\n" +
                        "            color: #333;\n" +
                        "            background-color: #f5f5f5;\n" +
                        "            margin: 0;\n" +
                        "            padding: 30px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .container {\n" +
                        "            background-color: #fff;\n" +
                        "            padding: 30px;\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 0 auto;\n" +
                        "            border-radius: 5px;\n" +
                        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                        "        }\n" +
                        "\n" +
                        "        h1 {\n" +
                        "            font-size: 28px;\n" +
                        "            color: #2c3e50;\n" +
                        "            margin-bottom: 20px;\n" +
                        "            border-bottom: 3px solid #3498db;\n" +
                        "            display: inline-block;\n" +
                        "            padding-bottom: 5px;\n" +
                        "        }\n" +
                        "\n" +
                        "        p {\n" +
                        "            margin-bottom: 10px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .event-details {\n" +
                        "            background-color: #f5f5f5;\n" +
                        "            padding: 15px;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 5px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .event-details ul {\n" +
                        "            list-style-type: none;\n" +
                        "            padding: 0;\n" +
                        "            margin: 0;\n" +
                        "        }\n" +
                        "\n" +
                        "        .event-details li {\n" +
                        "            margin-bottom: 5px;\n" +
                        "            color: #2c3e50;\n" +
                        "        }\n" +
                        "\n" +
                        "        .event-details strong {\n" +
                        "            color: #3498db;\n" +
                        "        }\n" +
                        "\n" +
                        "        .footer {\n" +
                        "            font-size: 14px;\n" +
                        "            text-align: center;\n" +
                        "            color: #999;\n" +
                        "            margin-top: 20px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <h1>Event Update</h1>\n" +
                        "        <p>Dear " + user.getFirstName() + ",</p>\n" +
                        "        <p>The event <strong>" + newEvent.getTitle() + "</strong> has been updated. Please review the new details below:</p>\n" +
                        "        <div class=\"event-details\">\n" +
                        "            <ul>\n" +
                        "                <li><strong>Title:</strong> "+ newEvent.getTitle() + "</li>\n" +
                        "                <li><strong>Description:</strong> "+ newEvent.getDescription() + "</li>\n" +
                        "                <li><strong>Date:</strong> " + newEvent.getDate() + "</li>\n" +
                        "                <li><strong>Time:</strong> " + newEvent.getTime() + "</li>\n" +
                        "                <li><strong>Location:</strong> "+ newEvent.getLocation() + "</li>\n" +
                        "            </ul>\n" +
                        "        </div>\n" +
                        "        <p>See you there!</p>\n" +
                        "        <p>Best regards,</p>\n" +
                        "        <p>The PlannR Team</p>\n" +
                        "    </div>\n" +
                        "    <div class=\"footer\">\n" +
                        "        This email was sent by PlannR. Please do not reply to this message.\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>\n");
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *") // This cron expression runs every minute
    public void deleteExpiredEvents() {
        LocalDateTime expiryDateTime = LocalDateTime.now().minus(30, ChronoUnit.MINUTES);
        LocalDate expiryDate = expiryDateTime.toLocalDate();
        LocalTime expiryTime = expiryDateTime.toLocalTime();

        List<Event> expiredEvents = eventRepository.findExpiredEvents(expiryDate, expiryTime);

        for (Event event : expiredEvents) {
            deleteEvent(event.getId(), event.getOwner().getId());
        }
    }
}
