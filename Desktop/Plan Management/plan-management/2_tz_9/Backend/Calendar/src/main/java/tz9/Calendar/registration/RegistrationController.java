package tz9.Calendar.registration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import tz9.Calendar.alert.LoginResponse;
import tz9.Calendar.alert.Alert;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserService;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AppUserService appUserService;

    /**
     * Registers a new user.
     *
     * @param request - RegistrationRequest object containing user details.
     * @return Alert object containing a registration confirmation message.
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public Alert register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return new Alert("Thank you for your registration, please confirm your account");
    }

    /**
     * Confirms a user's registration using a token.
     *
     * @param token - Registration confirmation token.
     * @return String message confirming successful registration or an error.
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    /**
     * Authenticates and logs in a user.
     *
     * @param loginRequest - LoginRequest object containing user's email and password.
     * @return ResponseEntity<LoginResponse> object containing user details and an alert message.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        AppUser appUser = appUserService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        Alert alert = new Alert(appUserService.checkUserEnabled(loginRequest.getEmail(), loginRequest.getPassword()));
        LoginResponse response = new LoginResponse(appUser, alert);

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id - User ID to be deleted.
     * @return Alert object containing a delete confirmation message.
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{id}")
    public Alert deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return new Alert("Delete successfully");
    }

    /**
     * Changes a user's role.
     *
     * @param email - User's email.
     * @param password - User's password.
     * @return Alert object containing a message about the change in role.
     */
    @PostMapping("/user/role")
    public Alert changeRole(@RequestParam String email, @RequestParam String password) {
        String result = appUserService.changeRole(email, password);
        return new Alert(result);
    }
}
