//package tz9.Calendar;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import tz9.Calendar.alert.Alert;
//import tz9.Calendar.alert.LoginResponse;
//import tz9.Calendar.appUser.AppUser;
//import tz9.Calendar.appUser.AppUserRepository;
//import tz9.Calendar.events.Event;
//import tz9.Calendar.events.EventRepository;
//import tz9.Calendar.events.RecurrenceType;
//import tz9.Calendar.registration.LoginRequest;
//import tz9.Calendar.registration.RegistrationRequest;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//public class EventControllerTest {
//
//        @Autowired
//        private TestRestTemplate restTemplate;
//        @Autowired
//        private AppUserRepository appUserRepository;
//        @Autowired
//        private EventRepository eventRepository;
//
//        @Test
//        public void testPostEvent() {
//            //register
//            RegistrationRequest registrationRequest = new RegistrationRequest("Hello", "MFK", "testing@gmail.com", "123");
//            ResponseEntity<Alert> registerResponse = restTemplate.postForEntity("/registration", registrationRequest, Alert.class);
//            appUserRepository.enableAppUser("testing@gmail.com");
//
//            //LoginUser
//            LoginRequest loginRequest = new LoginRequest("testing@gmail.com", "123");
//            ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity("/login", loginRequest, LoginResponse.class);
//
//            //Test get all events
//            String url = "/event/getEvents";
//            ResponseEntity<List<Event>> eventResponse = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {});
//            assertEquals(4, eventResponse.getBody().size());
//
//            //Test post event
//            url = "/event/postEvent/event/add/user=" + loginResponse.getBody().getUser().getId();
//            Event event = new Event("Testing",
//                    "xyz",
//                    LocalDate.of(2023, 5, 12),
//                    LocalTime.of(17, 0, 0),
//                    "Library", loginResponse.getBody().getUser(),
//                    RecurrenceType.NONE, 0);
//            Alert response = restTemplate.postForObject(url, event, Alert.class);
//            assertEquals("Post Successful", response.getMessage());
//
//
//            //Test modify event
//            url = "/event/modifyEvent/event=" + eventRepository.findByTitle("Testing").get().getId() + "/user=" + loginResponse.getBody().getUser().getId() + "/";
//
//            event = new Event("Modify",
//                    "xyz",
//                    LocalDate.of(2023, 5, 12),
//                    LocalTime.of(17, 0, 0),
//                    "Library", loginResponse.getBody().getUser(),
//                    RecurrenceType.NONE, 0);
//
//            HttpEntity<Event> requestEntity = new HttpEntity<>(event);
//            ResponseEntity<Alert> modifyEventResponse = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Alert.class);
//            assertEquals(HttpStatus.OK, modifyEventResponse.getStatusCode());
//            assertEquals("Put Successful", modifyEventResponse.getBody().getMessage());
//
//            //Test get certain event
//            url = "/event/getEvents/event=" + eventRepository.findByTitle("Modify").get().getId();
//            modifyEventResponse = restTemplate.exchange(url, HttpMethod.GET, null, Alert.class);
//            assertEquals(HttpStatus.OK, modifyEventResponse.getStatusCode());
//
//            //Test get all event user owns
//            url = "/event/getEvents/ownedEvents/user=" + loginResponse.getBody().getUser().getId();
//            eventResponse = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {});
//            assertEquals(1, eventResponse.getBody().size());
//
//            //Modify event by title
//            url = "/event/modifyEventByTitle/event=" + eventRepository.findByTitle("Modify").get().getTitle()+ "/user=" + loginResponse.getBody().getUser().getId() + "/";
//
//            event = new Event("Update",
//                    "xyz",
//                    LocalDate.of(2023, 5, 12),
//                    LocalTime.of(17, 0, 0),
//                    "Library", loginResponse.getBody().getUser(),
//                    RecurrenceType.NONE, 0);
//
//            requestEntity = new HttpEntity<>(event);
//            modifyEventResponse = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Alert.class);
//            assertEquals(HttpStatus.OK, modifyEventResponse.getStatusCode());
//            assertEquals("Put Successful", modifyEventResponse.getBody().getMessage());
//
//            //Test join event
//            url = "/event/joinEvent/event=" + eventRepository.findByTitle("Update").get().getId() + "/user=" + appUserRepository.findByEmail("keoxebo@gmail.com").get().getId();
//            ResponseEntity<Alert> joinEvent = restTemplate.exchange(url, HttpMethod.POST, null, Alert.class);
//            assertEquals("Successfully joined event.", joinEvent.getBody().getMessage());
//
//            //Test get event user has join
//            url = "/event/getEvents/joinedEvents/user=" + appUserRepository.findByEmail("keoxebo@gmail.com").get().getId();
//            eventResponse = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {});
//            assertEquals(1, eventResponse.getBody().size());
//
//            //Test left event
//            url = "/event/leaveEvent/event="+ eventRepository.findByTitle("Update").get().getId() + "/user=" + appUserRepository.findByEmail("keoxebo@gmail.com").get().getId();
//            ResponseEntity<Alert> leftEvent = restTemplate.exchange(url, HttpMethod.DELETE, null, Alert.class);
//            assertEquals("Successfully left event", leftEvent.getBody().getMessage());
//
//            //Test set event owner
//            url = "/event/setOwner/event=" + eventRepository.findByTitle("Update").get().getId() + "/userId=" + appUserRepository.findByEmail("keoxebo@gmail.com").get().getId();
//            ResponseEntity<Alert> owner = restTemplate.exchange(url, HttpMethod.POST, null, Alert.class);
//            assertEquals(HttpStatus.OK, owner.getStatusCode());
//
//            url = "/event/setOwner/event=" + eventRepository.findByTitle("Update").get().getId() + "/userId=" + appUserRepository.findByEmail("testing@gmail.com").get().getId();
//            owner = restTemplate.exchange(url, HttpMethod.POST, null, Alert.class);
//
//            //Test delete event
//            url = "/event/deleteEventByTitle/event=" + eventRepository.findByTitle("Update").get().getTitle() + "/user=" + loginResponse.getBody().getUser().getId();
//            modifyEventResponse = restTemplate.exchange(url, HttpMethod.DELETE, null, Alert.class);
//            assertEquals("Delete Successful.", modifyEventResponse.getBody().getMessage());
//
//            //DeleteUser
//            AppUser appUser = loginResponse.getBody().getUser();
//            restTemplate.delete("/user/" + appUser.getId());
//            ResponseEntity<Alert> deleteUserResponse = restTemplate.postForEntity("/login", loginRequest, Alert.class);
//            assertThat(deleteUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//            assertThat(deleteUserResponse.getBody().getMessage()).isEqualTo("");
//        }
//    }
//
//
//
