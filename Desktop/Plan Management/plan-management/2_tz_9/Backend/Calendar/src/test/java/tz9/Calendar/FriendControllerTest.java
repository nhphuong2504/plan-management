//package tz9.Calendar;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//public class FriendControllerTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void testFriendController() {
//        Long userId = 1L;
//        Long friendId = 3L;
//        String friendEmail = "nhphuong2504@gmail.com";
//
//        // Test getFriends
//        ResponseEntity<List> friendsResponse = restTemplate.getForEntity("/users/" + userId + "/friends", List.class);
//        assertThat(friendsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        // Test addFriendByID
//        ResponseEntity<Void> addFriendByIdResponse = restTemplate.postForEntity("/users/" + userId + "/friends/by-id/" + friendId, null, Void.class);
//        assertThat(addFriendByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        // Test addFriendByEmail
//        ResponseEntity<Void> addFriendByEmailResponse = restTemplate.postForEntity("/users/" + userId + "/friends/by-email/" + friendEmail, null, Void.class);
//        assertThat(addFriendByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        // Test removeFriend
//        restTemplate.delete("/users/" + userId + "/friends/" + friendId);
//        restTemplate.delete("/users/" + userId + "/friends/" + 5);
//        ResponseEntity<List> updatedFriendsResponse = restTemplate.getForEntity("/users/" + userId + "/friends", List.class);
//        assertThat(updatedFriendsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(updatedFriendsResponse.getBody().size()).isEqualTo(friendsResponse.getBody().size());
//
//        // Test getFriendEvents
//        ResponseEntity<List> friendEventsResponse = restTemplate.getForEntity("/users/" + userId + "/friends/events", List.class);
//        assertThat(friendEventsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//}
