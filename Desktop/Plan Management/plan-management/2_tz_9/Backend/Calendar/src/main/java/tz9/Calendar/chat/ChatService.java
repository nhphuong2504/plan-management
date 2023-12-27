package tz9.Calendar.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserRepository;
import tz9.Calendar.chat.chatlogs.ChatLog;
import tz9.Calendar.chat.chatlogs.ChatLogRepository;
import tz9.Calendar.events.Event;

import java.util.List;
import java.util.Set;

@Service
public class ChatService {

    ChatRepository chatRepository;

    AppUserRepository appUserRepository;

    ChatLogRepository chatLogRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, AppUserRepository appUserRepository,
                       ChatLogRepository chatLogRepository) {
        this.chatRepository = chatRepository;
        this.appUserRepository = appUserRepository;
        this.chatLogRepository = chatLogRepository;
    }

    /**
     * Creating a chat using an Owner id and another userId.
     *
     * @param ownerId - ID of owner.
     * @param userId - ID of second user.
     */
    public void createChat(long ownerId, long userId) {
        // Checks valid ID
        userPresentById(ownerId);
        userPresentById(userId);

        // Get Reference by ID if valid
        AppUser owner = appUserRepository.getReferenceById(ownerId);
        AppUser user = appUserRepository.getReferenceById(userId);

        // Create new Chat
        Chat chat = new Chat(owner, user);
        chat.setOwner(owner);

        // Update user references
        owner.joinChat(chat);
        user.joinChat(chat);

        // Save in repository
        chatRepository.save(chat);
        appUserRepository.save(owner);
        appUserRepository.save(user);
    }

    /**
     * Attempts to return a Chat with id 'chatId'
     *
     * @param chatId - Chat to get.
     * @return Chat object corresponding to chatId.
     */
    public Chat getChat(long chatId) {
        checkChatId(chatId);
        return chatRepository.getReferenceById(chatId);
    }

    /**
     * Attempts to return all chats that a user with id 'userId' is in
     *
     * @param userId - User to get.
     * @return All chats a user has joined.
     */
    public Set<Chat> getUserJoinedChats(long userId) {
        userPresentById(userId);
        return appUserRepository.getReferenceById(userId).getJoinedChats();
    }

    /**
     * Attempts to remove a user with id 'userId' from chat with id 'chatId'.  Note:
     * user making request must be the owner of the chat or admin.
     *
     * @param chatId - Chat to modify users of.
     * @param ownerId - user making request.
     * @param userId - User to remove from chat.
     */
    public void removeUser(long chatId, long ownerId, long userId) {
        // Checks valid ID
        checkChatId(chatId);
        userPresentById(ownerId);
        userPresentById(userId);

        // Get references
        Chat chat = chatRepository.getReferenceById(chatId);
        AppUser owner = appUserRepository.getReferenceById(ownerId);

        // Checks if owner is actually owner
        isOwner(chat, owner);

        AppUser user = appUserRepository.getReferenceById(userId);

        // Update References.
        chat.removeUser(user);
        user.leaveChat(chat);

        // Save references
        appUserRepository.save(user);
        chatRepository.save(chat);
    }

    /**
     * Attempts to add a user with id 'userId'.
     *
     * @param chatId - Chat to add user to.
     * @param userId - User to add to chat.
     */
    public void addUser(long chatId, long userId) {
        // Checks valid ID
        checkChatId(chatId);
        userPresentById(userId);

        // Get references
        Chat chat = chatRepository.getReferenceById(chatId);
        AppUser user = appUserRepository.getReferenceById(userId);

        // Add user to chat and add chat to user
        chat.addUser(user);
        user.joinChat(chat);

        // Save References
        chatRepository.save(chat);
        appUserRepository.save(user);
    }

    /**
     * Attempts to change owner of a chat with id 'chatId' from user with id
     * 'ownerId' to a different user with id 'newOwnerId'.
     *
     * @param chatId - Chat to change owner of.
     * @param ownerId - Previous owner.
     * @param newOwnerId - New owner.
     */
    public void changeOwner(long chatId, long ownerId, long newOwnerId) {
        // Checks valid ID
        checkChatId(chatId);
        userPresentById(ownerId);
        userPresentById(newOwnerId);

        // Get References
        Chat chat = chatRepository.getReferenceById(chatId);
        AppUser oldOwner = appUserRepository.getReferenceById(ownerId);

        // Checks if requested id is actually owner.
        isOwner(chat, oldOwner);

        AppUser newOwner = appUserRepository.getReferenceById(newOwnerId);

        // Change chat Owner
        chat.setOwner(newOwner);
        // Remove owned Chat
        oldOwner.deleteOwnedChat(chat);
        // Update NewOwner reference
        newOwner.addOwnedChat(chat);

        // Save references
        chatRepository.save(chat);
        appUserRepository.save(oldOwner);
        appUserRepository.save(newOwner);
    }

    /**
     * Updates all references in a chat with id 'chat' and deletes the chat.  Requested by user with id
     * 'userId'.
     *
     * @param chatId - Chat to delete.
     * @param userId - User making request.
     */
    @Transactional
    public void deleteChat(long chatId, long userId) {
        checkChatId(chatId);
        userPresentById(userId);

        Chat chat = chatRepository.findById(chatId).get();
        AppUser owner = appUserRepository.getReferenceById(userId);

        isOwner(chat, owner);

        chat.setOwner(null);

        for (AppUser user : chat.getUsersInChat()) {
            user.leaveChat(chat);
            appUserRepository.save(user);
        }

        List<ChatLog> chatLogs = chat.getChatLogs();

        for (int i = 0; i < chatLogs.size(); i++) {
            ChatLog chatLog = chatLogs.get(i);
            chatLogs.remove(chatLog);
            chatRepository.save(chat);

            chatLog.setUser(null);
            chatLog.setChat(null);
            chatLogRepository.save(chatLog);
            chatLogRepository.delete(chatLog);
        }

        chat.getUsersInChat().clear();
        chat.getChatLogs().clear();

        chatRepository.save(chat);
        chatRepository.delete(chat);
    }

    /*
     * Helper method to check if a user is owner.
     */
    private void isOwner(Chat chat, AppUser appUser) {
        if (!chat.getChatOwner().equals(appUser))  {
            throw new RuntimeException("Error: This user does not have sufficient permissions.");
        }
    }

    /*
     * Helper method to check if a user with id 'id' is present in repository.
     */
    private void userPresentById(long id) {
        if (!appUserRepository.existsById(id)) {
            throw new RuntimeException("Error: User with id: " + id + " not found.");
        }
    }

    /*
     * Helper method used to find a chat.  Throws exception if not found.
     */
    private void checkChatId(long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new RuntimeException("Error: Chat with id " + chatId + " not found.");
        }
    }
    public Chat saveChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public void setOwner(long chatId, long userId) {
        if (!chatRepository.existsById(chatId)) {
            throw new RuntimeException("Error: Event with id " + chatId + " was not found.");
        }

        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " was not found.");
        }

        Chat chat = chatRepository.getReferenceById(chatId);
        AppUser user = appUserRepository.getReferenceById(userId);

        chat.setOwner(user);
        user.addOwnedChat(chat);

        chatRepository.save(chat);
        appUserRepository.save(user);
    }
}
