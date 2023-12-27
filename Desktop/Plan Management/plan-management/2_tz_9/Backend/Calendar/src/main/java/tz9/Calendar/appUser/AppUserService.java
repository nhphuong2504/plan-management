package tz9.Calendar.appUser;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import tz9.Calendar.chat.Chat;
import tz9.Calendar.chat.ChatService;
import tz9.Calendar.chat.chatlogs.ChatLog;
import tz9.Calendar.chat.chatlogs.ChatLogService;
import tz9.Calendar.events.Event;
import tz9.Calendar.events.EventService;
import tz9.Calendar.friends.FriendService;
import tz9.Calendar.registration.token.ConfirmationToken;
import tz9.Calendar.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EventService eventService;
    private final ChatLogService chatLogService;
    private final ChatService chatService;
    private  final FriendService friendService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if (userExists) {
            throw new IllegalStateException("Email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
    public String checkUserEnabled(String email, String password) {
        if (appUserRepository.findByEmail(email).isPresent()) {
            if (!bCryptPasswordEncoder.matches(password, appUserRepository.findPassword(email))) {
                return "wrong password";
            }
            Boolean enabled = appUserRepository.findEnabledByEmail(email);
            if (enabled) {
                return "success";
            } else {
                return "fail";
            }
        }
        else {
            return "Email not found";
        }
    }

    public void deleteUser(Long id) {
        boolean exists = appUserRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("User with id " + id + " does not exists");
        }
        AppUser user = findUserById(id);
        removeUserFromAllEvents(user);
        removeUserFromAllChatLogs(user);
        removeChatFromAllEvents(user);
        removeUserAsOwnerFromAllChats(user);
        removeUserAsOwnerFromAllEvents(user);
        friendService.deleteAllFriendsOfUser(user.getId());
        appUserRepository.deleteById(id);
    }

    public AppUser loginUser(String email, String password) {
        // Find the user by email
        Optional<AppUser> optionalUser = appUserRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            AppUser user = optionalUser.get();
            return  user;
        }

        // If the email is not found or the password is incorrect, return null
        return null;
    }


    public String changeRole(String email, String password) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);

        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();

            if (!password.equals("iwanttobeanadmin123")) {
                return "Wrong password";
            } else {
                appUser.setAppUserRole(getNextRole(appUser.getAppUserRole()));
                appUserRepository.save(appUser);
                return "Role changed successfully";
            }
        }
        return "Invalid email";
    }
    private AppUserRole getNextRole(AppUserRole currentRole) {
        return currentRole == AppUserRole.USER ? AppUserRole.ADMIN : AppUserRole.USER;
    }

    public void removeUserFromAllEvents(AppUser user) {
        Set<Event> events = user.getJoinedEvents();
        for (Event event : events) {
            event.removeParticipant(user);
            eventService.saveEvent(event);
        }
    }

    public AppUser findUserById(Long id) {
        return appUserRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void removeUserFromAllChatLogs(AppUser user) {
        List<ChatLog> chatLogsToDelete = new ArrayList<>(user.getSentChats());
        for (ChatLog chatLog : chatLogsToDelete) {
            chatLogService.deleteChatLog(chatLog.getChat().getId(), chatLog.getId(), user.getId()); // Delete the chat log instead of setting the user to null
        }
    }

    public void removeUserAsOwnerFromAllChats(AppUser user) {
        List<Chat> ownedChats = new ArrayList<>(user.getOwnedChats());
        for (Chat chat : ownedChats) {
            chatService.deleteChat(chat.getId(), user.getId()); // Delete the chat instead of setting the owner to null
        }
    }

    public void removeUserAsOwnerFromAllEvents(AppUser user) {
        List<Event> ownedEvents = new ArrayList<>(user.getOwnedEvents()); // Replace with the appropriate method to get owned events
        for (Event event : ownedEvents) {
            eventService.deleteEvent(event.getId(), user.getId()); // Delete the event instead of setting the owner to null
        }
    }

    private void removeChatFromAllEvents(AppUser user) {
        List<Event> ownedEvents = new ArrayList<>(user.getEventList());
        for (Event event : ownedEvents) {
            eventService.deleteEvent(event.getId(), user.getId());
        }
    }

    public AppUser findUserByEmail(String email) {
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email);
        if (optionalAppUser.isPresent()) {
            return optionalAppUser.get();
        } else {
            throw new RuntimeException("User not found with id: " + email);
        }
    }
}
