package tz9.Calendar.chat.chatlogs;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import tz9.Calendar.alert.Alert;

import java.util.List;

@RestController
public class ChatLogController {

    private ChatLogService chatLogService;

    public ChatLogController(ChatLogService chatLogService) {
        this.chatLogService = chatLogService;
    }

    /**
     * Gets all chatLogs in a chat.
     *
     * @param chatId - Chat to get chatLogs from
     * @return List of ChatLogs present in a chat.
     */
    @GetMapping (value = "/chat/getLogs/chat={chatId}")
    @Operation(description = "Gets all chatLogs in a chat.")
    public List<ChatLog> getChatLogs(@PathVariable("chatId") long chatId) {
        return chatLogService.getChatLogs(chatId);
    }

    /**
     * Gets a specified user's sent chats.
     *
     * @param userId - User to get sent chats of.
     * @return All of a user's sent chats.
     */
    @GetMapping (value = "/chat/getSentLogs/user={userId}")
    @Operation(description = "Gets a specified user's sent chats.")
    public List<ChatLog> getSentChats(@PathVariable("userId") long userId) {
        return chatLogService.getUserSentLogs(userId);
    }

    /**
     * Posts a chatLog to a chat.
     *
     * @param chatLog - ChatLog object to post.
     * @param chatId - Chat to post log to.
     * @param userId - User posting log.
     * @return Confirmation message.
     */
    @PostMapping (value = "/chat/postChatLog/chat={chatId}/user={userId}")
    @Operation(description = "Posts a chatLog to a chat.")
    public Alert postChatLog(@RequestBody ChatLog chatLog, @PathVariable("chatId") long chatId, @PathVariable("userId") long userId) {
        chatLogService.postChatLog(chatLog, chatId, userId);
        return new Alert("Successfully posted.");
    }

    /**
     * Deletes a chatLog from a Chat.
     *
     * @param chatId - Chat to Delete log from.
     * @param chatLogId - ChatLog to delete.
     * @param userId - User making delete request.
     * @return Confirmation message.
     */
    @DeleteMapping (value = "/chat/deleteLog/chat={chatId}/user={userId}/chatLog={chatLogId}")
    @Operation(description = "Deletes a chatLog from a Chat.")
    public Alert deleteChatLog(@PathVariable("chatId") long chatId, @PathVariable("chatLogId") long chatLogId,
                               @PathVariable("userId") long userId) {
        chatLogService.deleteChatLog(chatId, chatLogId, userId);
        return new Alert("Delete Successful.");
    }

    /**
     * Edits a chatLog in a chat object.
     *
     * @param chatLog - ChatLog to overwrite a chatlog to.
     * @param chatId - Chat hosting chatLog that is going to be edited.
     * @param chatLogId - ChatLog to be changed.
     * @param userId - User making request.
     * @return Confirmation Message.
     */
    @PutMapping (value = "/chat/editChatLog/chat={chatId}/user={userId}/chatLog={chatLogId}")
    @Operation(description = "Edits a chatLog in a chat object.")
    public Alert putChatLog(@RequestBody ChatLog chatLog, @PathVariable("chatId") long chatId,
                            @PathVariable("chatLogId") long chatLogId, @PathVariable("userId") long userId) {
        chatLogService.editChatLog(chatLog, chatId, chatLogId, userId);
        return new Alert("Put successful.");
    }
}
