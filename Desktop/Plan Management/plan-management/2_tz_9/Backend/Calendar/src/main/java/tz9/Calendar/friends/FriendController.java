package tz9.Calendar.friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.events.Event;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;

    /**
     * Gets all friends of a user.
     *
     * @param userId - User ID to get friends from.
     * @return List of AppUser objects representing user's friends.
     */
    @GetMapping
    @Operation(description = "Gets all friends of a user.")
    public ResponseEntity<List<AppUser>> getFriends(@PathVariable("userId") Long userId) {
        List<AppUser> friends = friendService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    /**
     * Adds a friend to the user's friend list by their ID.
     *
     * @param userId - User ID who wants to add the friend.
     * @param friendId - Friend ID to be added.
     * @return ResponseEntity<Void>
     */
    @PostMapping("/by-id/{friendId}")
    @Operation(description = "Adds a friend to the user's friend list by their ID.")
    public ResponseEntity<Void> addFriendByID(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        friendService.addFriendByID(userId, friendId);
        return ResponseEntity.ok().build();
    }

    /**
     * Adds a friend to the user's friend list by their email.
     *
     * @param userId - User ID who wants to add the friend.
     * @param email - Friend email to be added.
     * @return ResponseEntity<Void>
     */
    @PostMapping("/by-email/{friendEmail}")
    @Operation(description = "Adds a friend to the user's friend list by their email.")
    public ResponseEntity<Void> addFriendByEmail(@PathVariable("userId") Long userId, @PathVariable("friendEmail") String email) {
        friendService.addFriendByEmail(userId, email);
        return ResponseEntity.ok().build();
    }

    /**
     * Removes a friend from the user's friend list by their ID.
     *
     * @param userId - User ID who wants to remove the friend.
     * @param friendId - Friend ID to be removed.
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{friendId}")
    @Operation(description = "Removes a friend from the user's friend list by their ID.")
    public ResponseEntity<Void> removeFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        friendService.removeFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    /**
     * Gets all events of a user's friends.
     *
     * @param userId - User ID to get friends' events from.
     * @return List of Event objects representing friends' events.
     */
    @GetMapping("/events")
    @Operation(description = "Gets all events of a user's friends.")
    public ResponseEntity<List<Event>> getFriendEvents(@PathVariable("userId") Long userId) {
        List<Event> friendEvents = friendService.getFriendEvents(userId);
        return ResponseEntity.ok(friendEvents);
    }
}
