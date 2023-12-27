package tz9.Calendar.friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserRepository;
import tz9.Calendar.events.Event;
import tz9.Calendar.events.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private EventRepository eventRepository;

    public void addFriendByID(Long userId, Long friendId) {
        // Check if users exist
        appUserRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        appUserRepository.findById(friendId).orElseThrow(() -> new IllegalArgumentException("Invalid friend ID: " + friendId));

        // Create a new Friend instance and save it
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friendRepository.save(friend);

        friend = new Friend();
        friend.setUserId(friendId);
        friend.setFriendId(userId);
        friendRepository.save(friend);
    }

    public void addFriendByEmail(Long userId, String FriendEmail) {
        // Check if users exist
        appUserRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        appUserRepository.findByEmail(FriendEmail).orElseThrow(() -> new IllegalArgumentException("Invalid friend email: " + FriendEmail));

        // Create a new Friend instance and save it
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(appUserRepository.findUserIdByEmail(FriendEmail));
        friendRepository.save(friend);

        friend = new Friend();
        friend.setUserId(appUserRepository.findUserIdByEmail(FriendEmail));
        friend.setFriendId(userId);
        friendRepository.save(friend);
    }
    public void removeFriend(Long userId, Long friendId) {
        friendRepository.deleteById(new FriendId(userId, friendId));
        friendRepository.deleteById(new FriendId(friendId, userId));
    }

    public void deleteAllFriendsOfUser(Long userId) {
        List<Friend> friends = friendRepository.findAllByUserIdOrFriendId(userId, userId);
        friendRepository.deleteAll(friends);
    }

    public List<AppUser> getFriends(Long userId) {

        if (appUserRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }

        List<Friend> friends = friendRepository.findByUserId(userId);
        List<AppUser> userFriends = new ArrayList<AppUser>();

        for (Friend friend : friends) {
            userFriends.add(appUserRepository.getReferenceById(friend.getFriendId()));
        }
        return userFriends;
    }

    public List<Event> getFriendEvents(Long userId) {
        List<AppUser> friends = getFriends(userId);
        List<Event> friendEvents = new ArrayList<>();
        friends.forEach(friend -> friendEvents.addAll(eventRepository.findAllByOwner(friend)));
        return friendEvents;
    }


}

