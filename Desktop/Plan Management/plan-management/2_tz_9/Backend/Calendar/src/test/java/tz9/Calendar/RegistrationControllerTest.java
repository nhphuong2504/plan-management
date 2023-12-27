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
//import tz9.Calendar.alert.Alert;
//import tz9.Calendar.alert.LoginResponse;
//import tz9.Calendar.appUser.AppUser;
//import tz9.Calendar.appUser.AppUserRepository;
//import tz9.Calendar.appUser.AppUserRole;
//import tz9.Calendar.registration.LoginRequest;
//import tz9.Calendar.registration.RegistrationRequest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//public class RegistrationControllerTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//    @Autowired
//    private AppUserRepository appUserRepository;
//
//    @Test
//    public void testRegistrationController() {
//        // Test register
//        RegistrationRequest registrationRequest = new RegistrationRequest("Hello", "MFK", "testing@gmail.com", "123");
//        ResponseEntity<Alert> registerResponse = restTemplate.postForEntity("/registration", registrationRequest, Alert.class);
//        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(registerResponse.getBody().getMessage()).contains("Thank you for your registration");
//
//        //Setting up an email service and token generation.
//        appUserRepository.enableAppUser("testing@gmail.com");
//
//        //Test loginUser
//        LoginRequest loginRequest = new LoginRequest("testing@gmail.com", "123");
//        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity("/login", loginRequest, LoginResponse.class);
//        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(loginResponse.getBody().getAlert().getMessage()).isEqualTo("success");
//
//        //Test changeRole
//        String url = "/user/role?email=" + registrationRequest.getEmail() + "&password=iwanttobeanadmin123";
//        Alert response = restTemplate.postForObject(url, null, Alert.class);
//        assertEquals("Role changed successfully", response.getMessage());
//        assertEquals(AppUserRole.ADMIN, appUserRepository.findByEmail(registrationRequest.getEmail()).get().getAppUserRole());
//
//        url = "/user/role?email=" + registrationRequest.getEmail() + "&password=iwanttobeanadmin";
//        response = restTemplate.postForObject(url, null, Alert.class);
//        assertEquals("Wrong password", response.getMessage());
//        assertEquals(AppUserRole.ADMIN, appUserRepository.findByEmail(registrationRequest.getEmail()).get().getAppUserRole());
//
//
//        String invalidEmail = "nonexistent@example.com";
//        url = "/user/role?email=" + invalidEmail + "&password=iwanttobeanadmin123";
//        response = restTemplate.postForObject(url, null, Alert.class);
//        assertEquals("Invalid email", response.getMessage());
//
//        // Test deleteUser
//        AppUser appUser = loginResponse.getBody().getUser();
//        restTemplate.delete("/user/" + appUser.getId());
//        ResponseEntity<Alert> deleteUserResponse = restTemplate.postForEntity("/login", loginRequest, Alert.class);
//        assertThat(deleteUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(deleteUserResponse.getBody().getMessage()).isEqualTo("");
//    }
//}
//
