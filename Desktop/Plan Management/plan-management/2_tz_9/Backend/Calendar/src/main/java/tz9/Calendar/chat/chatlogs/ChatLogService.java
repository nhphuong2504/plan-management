package tz9.Calendar.chat.chatlogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserRepository;
import tz9.Calendar.appUser.AppUserRole;
import tz9.Calendar.chat.Chat;
import tz9.Calendar.chat.ChatRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ChatLogService {

    // Repositories that we will need to access
    ChatLogRepository chatLogRepository;

    ChatRepository chatRepository;

    AppUserRepository appUserRepository;

    /*
     * Autowired Constructor
     */
    @Autowired
    public ChatLogService(ChatLogRepository chatLogRepository,
                            ChatRepository chatRepository,
                            AppUserRepository appUserRepository) {
        this.chatLogRepository = chatLogRepository;
        this.appUserRepository = appUserRepository;
        this.chatRepository = chatRepository;
    }

    /**
     * Attempts to get Chat Logs from a chat with id 'chatId'.
     *
     * @param chatId - Chat to get logs from.
     * @return List of ChatLogs posted to a chat.
     */
    public List<ChatLog> getChatLogs(long chatId) {
        // Check valid id
        if (!chatRepository.existsById(chatId)) {
            throw new RuntimeException("Error: Chat with id " + chatId + "does not exist.");
        }
        return chatRepository.getReferenceById(chatId).getChatLogs();
    }

    /**
     * Gets the logs a user has sent.
     *
     * @param userId - User to get logs from.
     * @return All logs that a user has sent, not restricted to a single chat.
     */
    public List<ChatLog> getUserSentLogs(long userId) {
        if (appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " does not exist.");
        }

        return appUserRepository.getReferenceById(userId).getSentChats();
    }

    /**
     * Attempts to add a ChatLog object to a chat with id 'chatId'.  Sets AppUser with id
     * 'userId' as user who sent log.
     *
     * @param chatLog - ChatLog to post to a chat.
     * @param chatId - Chat to post log to.
     * @param userId User posting the chatlog.
     */
    public void postChatLog(ChatLog chatLog, long chatId, long userId) {
        // Check valid Ids
        if (!chatRepository.existsById(chatId)) {
            throw new RuntimeException("Error: Chat with id " + chatId + "does not exist.");
        }

        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + "does not exist.");
        }

        // Get AppUser and Chat by Id
        AppUser appUser = appUserRepository.getReferenceById(userId);
        Chat chat = chatRepository.getReferenceById(chatId);

        // Set foreign references
        chatLog.setUser(appUser);
        chatLog.setChat(chat);
        appUser.addChatLog(chatLog);
        chat.addChatLog(chatLog);

        // Save updated Entities
        appUserRepository.save(appUser);
        chatLogRepository.save(chatLog);
        chatRepository.save(chat);
    }

    /**
     * Edits a chatLog of id 'chatLogId' present in chat ofid 'chatId', overwriting with chatLog.  Requested
     * by user with id 'userId'.
     *
     * @param chatLog - ChatLog to overwrite with.
     * @param chatId - Chat hosting chatLog.
     * @param chatLogId - Chatlog to be overwritten
     * @param userId - User making edit request.
     */
    @Transactional
    public void editChatLog(ChatLog chatLog, long chatId, long chatLogId, long userId) {
        //Perform checks
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " does not exist.");
        }

        if (!chatRepository.existsById(chatId)) {
            throw new RuntimeException("Error: Chat with id " + chatId + " does not exist.");
        }

        if (!chatLogRepository.existsById(chatLogId)) {
            throw new RuntimeException("Error: ChatLog with id " + chatLogId + " does not exist.");
        }

        // Get References
        Chat chat = chatRepository.getReferenceById(chatId);
        ChatLog chatLogReposReference = chatLogRepository.getReferenceById(chatLogId);

        // Check if log is in chat
        if (!chat.getChatLogs().contains(chatLogReposReference)) {
            throw new RuntimeException("Error: ChatLog with id " + chatLog + " is not present in this chat.");
        }

        AppUser appUser = appUserRepository.getReferenceById(userId);

        // Check if user requesting is user who sent chat
        if (!chatLogReposReference.getUser().equals(appUser)) {
            throw new RuntimeException("Error: Passed user is not the sender of the chat.");
        }

        // Edit objects
        chatLogReposReference.editLog(chatLog.getMessage(), appUser);   // Updates chatLog reference
        appUser.updateChatLog(chatLogReposReference, chatLogId);        // Needs to also be updated in the AppUser
            // sentChats List.  Passes chatLogId to find the original chatLog.22


        // Save updated objects
        chatLogRepository.save(chatLogReposReference);
        appUserRepository.save(appUser);
    }

    /**
     * Attempts to delete a chatLog.
     *
     * @param chatId - Chat to delete log from
     * @param chatLogId - ID of log to delete.
     * @param userId - User making request.
     */
    public void deleteChatLog(long chatId, long chatLogId, long userId) {
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " does not exist.");
        }

        if (!chatRepository.existsById(chatId)) {
            throw new RuntimeException("Error: Chat with id " + chatId + " does not exist.");
        }

        if (!chatLogRepository.existsById(chatLogId)) {
            throw new RuntimeException("Error: ChatLog with id " + chatLogId + " does not exist.");
        }

        ChatLog chatLog = chatLogRepository.getReferenceById(chatLogId);
        AppUser owner = appUserRepository.getReferenceById(userId);
        Chat chat = chatRepository.getReferenceById(chatId);

        if (chatLog.getUser().getId() != userId && !chat.getChatOwner().equals(owner) && owner.getAppUserRole() != AppUserRole.ADMIN) {
            throw new RuntimeException("Error: This user does not have sufficient permissions.");
        }

        // Remove chatLog from chat
        chat.removeChatLog(chatLog);

        // Unbind the chatLog from sender
        if (owner.getId() == userId) {
            owner.deleteChatLog(chatLog, chatLogId);
        } else {
            AppUser user = chatLog.getUser();
            user.deleteChatLog(chatLog, chatLogId);
            appUserRepository.save(user);
        }

        // Unbind user from chatLog
        chatLog.unbindLog();

        chatRepository.save(chat);
        appUserRepository.save(owner);

        chatLogRepository.save(chatLog);
        chatLogRepository.delete(chatLog);
    }

    public ChatLog saveChatLog(ChatLog chatLog) {
        return chatLogRepository.save(chatLog);
    }
}
