package tz9.Calendar.chat;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import tz9.Calendar.alert.Alert;

import java.util.Set;

@RestController
@RequestMapping("/chat")
public class ChatController {

    ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Creates a chat object with two users, passed as IDs.  UserOne is taken as chat Owner.
     *
     * @param ownerId - User creating chat.
     * @param memID - Required second member when creating a chat.
     * @return Confirmation message.
     */
    @PostMapping(value = "/createChat/owner={idOne}/member={memID}")
    @Operation(description = "Creates a chat object with two users, passed as IDs.  UserOne is taken as chat Owner.")
    public Alert createChat(@PathVariable("idOne") long ownerId, @PathVariable("memID") long memID) {
        chatService.createChat(ownerId, memID);
        return new Alert("Chat successfully created!");
    }

    /**
     * Gets a chat with id 'id'.
     *
     * @param chatId - Chat to get
     * @return Chat with ID 'chatId'
     */
    @GetMapping(value = "/getChat/chat={id}")
    @Operation(description = "Gets a chat with id 'id'.")
    public Chat getChat(@PathVariable("id") long chatId) {
        return chatService.getChat(chatId);
    }

    /**
     * Returns all chats that a user is in.
     *
     * @param userId - User to get chats of.
     * @return All chats a user is in.
     */
    @GetMapping (value = "/getAllChats/user={userId}")
    @Operation(description = "Returns all chats that a user is in.")
    public Set<Chat> getjoinedChats(@PathVariable("userId") long userId) {
        return chatService.getUserJoinedChats(userId);
    }

    /**
     * Attempts to remove a user with id 'userId' from chat with id 'chatId'.  Requested by
     * user with id 'reqId' (Necessary to determine if owner).
     *
     * @param chatId - Chat to remove user from.
     * @param reqId - User making request.
     * @param userId - User to remove.
     * @return Confirmation Message.
     */
    @DeleteMapping(value = "/removeUser/chat={chatId}/user={reqId}/userDel={userId}")
    @Operation(description = "Attempts to remove a user with id 'userId' from chat with id 'chatId'.  Requested by user with id 'reqId' (Necessary to determine if owner).")
    public Alert removeUser(@PathVariable("chatId") long chatId, @PathVariable("reqId") long reqId,
                            @PathVariable("userId") long userId) {
        chatService.removeUser(chatId, reqId, userId);
        return new Alert("Removal Successful");
    }

    /**
     * Adds a user with ID 'userId' to a chat with id 'chatId'.
     *
     * @param chatId - Chat to add a user to.
     * @param userId - User to add to chat.
     * @return Confirmation Message.
     */
    @PostMapping(value = "/addUser/chat={chatId}/user={userId}")
    @Operation(description = "Adds a user with ID 'userId' to a chat with id 'chatId'.")
    public Alert addUserToChat(@PathVariable("chatId") long chatId, @PathVariable("userId") long userId) {
        chatService.addUser(chatId, userId);
        return new Alert("User successfully added.");
    }

    /**
     * Attempts to change the owner of a chat to user with id 'userId'.
     *
     * @param chatId - Chat to change owner of.
     * @param reqId - User making request.
     * @param userId - User who will be the new owner.
     * @return Confirmation message.
     */
    @PutMapping(value = "/changeOwner/chat={chatId}/reqUser={reqId}/newOwner={userId}")
    @Operation(description = "Attempts to change the owner of a chat to user with id 'userId'.")
    public Alert changeOwner(@PathVariable("chatId") long chatId, @PathVariable("reqId") long reqId,
                             @PathVariable("userId") long userId) {
        chatService.changeOwner(chatId, reqId, userId);
        return new Alert("Owner Successfully changed.");
    }

    /**
     * Attempts to delete chat with id 'chatId', requested by user with id 'userId'.
     *
     * @param chatId - Chat to delete.
     * @param userId - User making request.
     * @return Confirmation Message.
     */
    @DeleteMapping(value = "/delete/chat={chatId}/user={userId}")
    @Operation(description = "Attempts to delete chat with id 'chatId', requested by user with id 'userId'.")
    public Alert deleteChat(@PathVariable("chatId") long chatId, @PathVariable("userId") long userId) {
        chatService.deleteChat(chatId, userId);
        return new Alert("Delete Successful");
    }

    @PostMapping(value = "/setOwner/chat={chatId}/user={userId}")
    public Alert setOwnnerChat(@PathVariable("chatId") long chatId, @PathVariable("userId") long userId) {
        chatService.setOwner(chatId, userId);
        return new Alert("Set Successful");
    }
}
