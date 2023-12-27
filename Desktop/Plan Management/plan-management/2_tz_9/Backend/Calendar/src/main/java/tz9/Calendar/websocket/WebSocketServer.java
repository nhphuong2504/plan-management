package tz9.Calendar.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserRepository;
import tz9.Calendar.appUser.AppUserService;
import tz9.Calendar.chat.Chat;
import tz9.Calendar.chat.ChatRepository;
import tz9.Calendar.chat.chatlogs.ChatLog;
import tz9.Calendar.chat.chatlogs.ChatLogRepository;
import tz9.Calendar.friends.Friend;
import tz9.Calendar.friends.FriendService;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

// TODO Will need to be changed
@ServerEndpoint(value = "/websocket/chat/{chatId}/user/{userId}",
        encoders = {MessageEncoder.class, MessageArrayEncoder.class})
@Controller
public class WebSocketServer extends AbstractWebSocketMessageBrokerConfigurer {

    private Chat currentChat;

    private static ChatRepository chatRepository;

    private static ChatLogRepository chatLogRepository;

    private static AppUserRepository appUserRepository;

    private static Map<Session, String> sessionUserMap = new Hashtable<>();
    private static Map<String, Session> userSessionMap = new Hashtable<>();

    private static Map<Long, ArrayList<Session>> chatSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    // Is this necessary in this class?
    private static AppUserService appUserService;


    @Autowired
    public void setRepositories(ChatRepository chatRepo, AppUserRepository appRepo, ChatLogRepository chatLogRepo) {
        chatRepository = chatRepo;
        appUserRepository = appRepo;
        chatLogRepository = chatLogRepo;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") long chatId, @PathParam("userId") long userId)
            throws IllegalArgumentException, EncodeException, IOException {
        logger.info("Entered Open");

        if (!chatRepository.existsById(chatId)) {
            logger.info("Error: Invalid chatId");
            throw new IllegalArgumentException("Error: Invalid chatId.");
        }

        if (!appUserRepository.existsById(userId)) {
            logger.info("Error: Invalid userId");
            throw new IllegalArgumentException("Error: Invalid userId.");
        }

        Optional<Chat> currChat = chatRepository.findById(chatId);
        currentChat = currChat.get();

        Optional<AppUser> optUser = appUserRepository.findById(userId);
        AppUser user = optUser.get();

        sessionUserMap.put(session, user.getEmail());
        userSessionMap.put(user.getEmail(), session);

        // If there isn't an arrayList of sessions mapped by the chat, create new list and associate with the currentChat
        if (chatSessionMap.get(currentChat.getId()) == null) {
            ArrayList<Session> chatSessions = new ArrayList<>();
            chatSessions.add(session);

            chatSessionMap.put(currentChat.getId(), chatSessions);

        } else {
            // Else there is an arrayList, need to add the session to the list.
            chatSessionMap.get(currentChat.getId()).add(session);
        }

        List<ChatLog> chatLogsSend = currentChat.getChatLogs();
        List<Message> logHistory = new ArrayList<Message>();

        if (chatLogsSend.size() != 0) {
            // Send user all Chat Logs
            for (ChatLog c : chatLogsSend) {
                logHistory.add(new Message(c.getUser().getFirstName() + " " + c.getUser().getLastName(),
                        c.getMessage(),
                        c.getUser().getId()));
            }
            userSessionMap.get(user.getEmail()).getBasicRemote().sendObject(logHistory);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        String email = sessionUserMap.get(session);

        Optional<AppUser> sender = appUserRepository.findByEmail(email);

        if (sender.isEmpty()) {
            throw new IllegalArgumentException("Error: Invalid Sending User.");
        }

        AppUser senderUser = sender.get();
        ChatLog chatLog = new ChatLog(senderUser, message, currentChat);
        ArrayList<Message> m = new ArrayList<>();

        refresh(currentChat);
        List<ChatLog> chatLogsSend = currentChat.getChatLogs();
        List<Message> logHistory = new ArrayList<Message>();

        if (chatLogsSend.size() != 0) {
            // Send user all Chat Logs
            for (ChatLog c : chatLogsSend) {
                logHistory.add(new Message(c.getUser().getFirstName() + " " + c.getUser().getLastName(),
                        c.getMessage(),
                        c.getUser().getId()));
            }
            logHistory.add(new Message(senderUser.getFullName(), message, senderUser.getId()));
            sendToAll(logHistory);
        }
        chatLogRepository.save(chatLog);
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("Entering Close");

        String email = sessionUserMap.get(session);
        sessionUserMap.remove(session);
        userSessionMap.remove(email);
        chatSessionMap.get(currentChat.getId()).remove(session);

        logger.info(sessionUserMap.toString());
        logger.info(userSessionMap.toString());
        logger.info(chatSessionMap.toString());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("whoops");
        logger.info(throwable.toString());
        try {
            session.getBasicRemote().sendText(throwable.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendToAll(Message chatLog) {
        for (Session s : chatSessionMap.get(currentChat.getId())) {
            try {
                s.getBasicRemote().sendObject(chatLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToAll(List<Message> chatLog) {
        for (Session s : chatSessionMap.get(currentChat.getId())) {
            try {
                s.getBasicRemote().sendObject(chatLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    private void sendUserOnlineStatus(AppUser user) {
        // Get the list of user's friends
        Set<Friend> friendEntities = user.getFriends();

        // Prepare the online status message
        Message onlineStatusMessage = new Message(user.getFullName(), user.getId(), MessageType.ONLINE);

        // Send online status message to online friends only
        for (Friend friendEntity : friendEntities) {
            Long friendId = friendEntity.getFriendId();
            AppUser friend = appUserService.findUserById(friendId); // Assuming you have a method to load AppUser by ID in your AppUserService
            Session friendSession = userSessionMap.get(friend.getEmail());
            if (friendSession != null) {
                try {
                    friendSession.getBasicRemote().sendObject(onlineStatusMessage);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    private void refresh(Chat currentChat) {
        long id = currentChat.getId();
        this.currentChat = chatRepository.findById(id).get();
    }
    @Autowired
    public void setAppUserService(AppUserService appUserService) {
        WebSocketServer.appUserService = appUserService;
    }

}
