package tz9.Calendar.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserRepository;
import tz9.Calendar.appUser.AppUserService;
import tz9.Calendar.friends.Friend;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@ServerEndpoint(value = "/websocket/login/{userId}", encoders = {MessageEncoder.class})
@Controller
public class WebSocketOnline {

    private static AppUserRepository appUserRepository;

    private static Map<Session, String> sessionUserMap = new Hashtable<>();

    private static Map<String, Session> userSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(WebSocketOnline.class);

    private static AppUserService appUserService;

    @Autowired
    public void setupRepositories(AppUserRepository appUserRepository) {
        WebSocketOnline.appUserRepository = appUserRepository;
    }

    @OnOpen
    public void onOpen(@PathParam("userId") long userId, Session session) {
        logger.info("Entered Open");

        if (!appUserRepository.existsById(userId)) {
            logger.info("Error: Invalid userId");
            throw new IllegalArgumentException("Error: Invalid userId.");
        }

        Optional<AppUser> optUser = appUserRepository.findById(userId);
        AppUser user = optUser.get();

        // Add user's session to sessionUserMap and userSessionMap
        sessionUserMap.put(session, user.getEmail());
        userSessionMap.put(user.getEmail(), session);

        sendUserOnlineStatus(user);
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("Entered Close");

        // Get the user's email
        String email = sessionUserMap.get(session);

        // Load the user from the repository
        AppUser user = appUserService.findUserByEmail(email);

        // Send the user's offline status
        sendUserOnlineStatus(user);

        // Remove the user from sessionUserMap and userSessionMap
        sessionUserMap.remove(session);
        userSessionMap.remove(email);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("Entered OnError");
    }

    private void sendUserOnlineStatus(AppUser user) {
        // Get the list of user's friends
        Set<Friend> friendEntities = user.getFriends();

        // Prepare the online status message
        String onlineStatusMessage = user.getFullName();

        // Send online status message to online friends only
        for (Friend friendEntity : friendEntities) {
            Long friendId = friendEntity.getFriendId();
            AppUser friend = appUserService.findUserById(friendId);
            Session friendSession = userSessionMap.get(friend.getEmail());
            if (friendSession != null) {
                try {
                    friendSession.getBasicRemote().sendText(onlineStatusMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Autowired
    public void setAppUserService(AppUserService appUserService) {
        WebSocketOnline.appUserService = appUserService;
    }

}
