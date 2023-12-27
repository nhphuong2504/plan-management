//package tz9.Calendar;
//
//import io.restassured.response.Response;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.junit4.SpringRunner;
//import tz9.Calendar.appUser.AppUser;
//import tz9.Calendar.appUser.AppUserRepository;
//import tz9.Calendar.appUser.AppUserRole;
//import tz9.Calendar.chat.Chat;
//import io.restassured.RestAssured;
//import tz9.Calendar.chat.ChatRepository;
//import tz9.Calendar.chat.chatlogs.ChatLog;
//import tz9.Calendar.events.Event;
//
//import java.util.List;
//
//import static io.restassured.RestAssured.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RunWith(SpringRunner.class)
//public class ChatControllerTest {
//
//    private AppUser owner;
//    private AppUser secondUser;
//    @Autowired
//    private AppUserRepository appUserRepository;
//    @Autowired
//    private ChatRepository chatRepository;
//
//    @LocalServerPort
//    private int port;
//
//    @Before
//    public void setup() {
//        RestAssured.port = port;
//        RestAssured.baseURI = "http://localhost";
//
//        owner = new AppUser("Tasman", "Grinnell", "firstTest@test.org",
//                "This is a password", AppUserRole.USER);
//
//        secondUser = new AppUser("Phuong", "Nguyen", "secondTest@test.org",
//                "AnotherUserPassword", AppUserRole.USER);
//
//    }
//
//    @Test
//    public void testChatInDataBase() {
//        Response response = RestAssured.when().get("chat/getChat/chat=52");
//
//        List<String> usersInChat = response.then().extract().path("usersInChat");
//        List<String> logsInChat = response.then().extract().path("chatLogs");
//        int chatOwnerId = response.then().extract().path("chatOwner.id");
//
//        assertTrue(usersInChat.size() == 0);
//        assertTrue(logsInChat.size() == 0);
//        assertTrue(chatOwnerId == 1);
//
//        // Status for not throwing exception -> 500
//        Response badResponse = RestAssured.when().get("/chat/getChat/chat=0");
//
//        assertTrue(badResponse.getStatusCode() == 500);
//        assertTrue(badResponse.then().extract().path("message").equals("Error: Chat with id 0 not found."));
//    }
//
//    @Test
//    public void testControllerMethods() {
//        // Test getJoinedChats
//
//        Response tryGetJoinedChats = RestAssured.when().get("/chat/getAllChats/user=1");
//
//        assertTrue(tryGetJoinedChats.getStatusCode() == 200);
//
//        List<String> joinedChats = tryGetJoinedChats.then().extract().path("");
//
//        assertTrue(joinedChats.size() == 1);
//
//
//        Response removeUser1 = delete("/chat/removeUser/chat=2/user=1/userDel=4");
//
//        assertTrue(removeUser1.getStatusCode() == 200);
//
//        // Try bad Request -> Should throw exception trying to remove user that isn't part of the chat.
//        removeUser1 = RestAssured.when().delete("/chat/removeUser/chat=52/user=1/userDel=4");
//
//        assertTrue(removeUser1.statusCode() == 500);
//
//        // Test addUser
//
//
//        Response postUser3 = post("/chat/addUser/chat=2/user=4");
//
//        assertTrue(postUser3.getStatusCode() == 200);
//
//        Response badPostUser = post("/chat/addUser/chat=2/user=10");
//
//        assertTrue(badPostUser.getStatusCode() == 500);
//
//        //Test changeOwner
//
//        // First test to see who's owner
//        Response getChat1 = RestAssured.when().get("/chat/getChat/chat=2");
//        int ownerId = getChat1.then().extract().path("chatOwner.id");
//
//        assertTrue(getChat1.getStatusCode() == 200);
//        assertTrue(ownerId == 1);
//
//        ///changeOwner/chat={chatId}/reqUser={reqId}/newOwner={userId}
//        Response changeOwner152 = RestAssured.put("chat/changeOwner/chat=2/reqUser=1/newOwner=4");
//        assertTrue(changeOwner152.getStatusCode() == 200);
//
//        getChat1 = RestAssured.when().get("/chat/getChat/chat=2");
//        ownerId = getChat1.then().extract().path("chatOwner.id");
//
//        assertTrue(getChat1.getStatusCode() == 200);
//        assertTrue(ownerId == 4);
//
//        changeOwner152 = RestAssured.put("chat/changeOwner/chat=2/reqUser=4/newOwner=1");
//        assertTrue(changeOwner152.getStatusCode() == 200);
//    }
//
//    @Test
//    public void testChatMethods() {
//        Chat chat = new Chat(owner, secondUser);
//
////         Initialization logic
//        assertTrue(chat.getChatOwner().getFirstName() == owner.getFirstName());
//        assertTrue(chat.getUsersInChat().contains(secondUser));
//        assertTrue(chat.getChatLogs().size() == 0);
//        assertTrue(chat.getEventChat() == null);
//
//        //AppUser user, String message
//        ChatLog chatLog = new ChatLog(owner, "This is a new message", chat);
//        ChatLog secondUserLog = new ChatLog(secondUser, "Another message", chat);
//        chat.addChatLog(chatLog);
//
//        assertTrue(chat.getChatLogs().size() == 1);
//        assertTrue(chat.getChatLogs().get(0).getUser() == owner && chat.getChatLogs().get(0).getChat() == chat);
//
//        chat.removeChatLog(chatLog);
//
//        assertTrue(chat.getChatLogs().size() == 0);
//
//        chat.addChatLog(chatLog);
//        chat.addChatLog(secondUserLog);
//
//        assertTrue(chat.getChatLogs().size() == 2);
//        assertTrue(chat.getChatLogs().get(0).getUser() == owner && chat.getChatLogs().get(0).getChat() == chat);
//        assertTrue(chat.getChatLogs().get(1).getUser() == secondUser && chat.getChatLogs().get(1).getChat() == chat);
//
//        chat.removeUsersLogs(owner);
//
//        assertTrue(chat.getChatLogs().size() == 1);
//        assertTrue(chat.getChatLogs().get(0).getUser() == secondUser && chat.getChatLogs().get(0).getChat() == chat);
//
//        AppUser newUser = new AppUser("Z", "Rapoza", "thirdTest@test.org",
//                "AnotherThirdUserPassword", AppUserRole.USER);
//
//        chat.addUser(newUser);
//
//        assertTrue(chat.getUsersInChat().size() == 3);
//        assertTrue(chat.getUsersInChat().get(0).getFirstName().equals("Tasman") &&
//                chat.getUsersInChat().get(1).getFirstName().equals("Phuong") &&
//                chat.getUsersInChat().get(2).getFirstName().equals("Z"));
//    }
//
//    @Test
//    public void testChatAddDelete() {
//        Response getAllChats = get("/chat/getAllChats/user=1");
//        assertTrue(getAllChats.statusCode() == 200);
//
//        List<String> initialNumChats = getAllChats.then().extract().path("");
//        int initialSize = initialNumChats.size();
//
//        // POST CHAT
//        Response postChat = post("/chat/createChat/owner=1/member=3");
//        assertTrue(postChat.getStatusCode() == 200);
//
//        // DELETE CHAT
//        Response deleteChat = delete("/chat/delete/chat=" +  chatRepository.findAll().get(chatRepository.findAll().size() - 1).getId() + "/user=1");
//
//        getAllChats = get("/chat/getAllChats/user=1");
//        assertTrue(getAllChats.statusCode() == 200);
//    }
//
//    @Test
//    public void testChatConstructors() {
//        Event event = new Event();
//
//        Chat chat = new Chat(owner, secondUser, event);
//
//        assertTrue(chat.getUsersInChat().size() == 2);
//        assertTrue(chat.getEventChat() == event);
//        assertTrue(chat.getChatOwner() == owner);
//    }
//}
