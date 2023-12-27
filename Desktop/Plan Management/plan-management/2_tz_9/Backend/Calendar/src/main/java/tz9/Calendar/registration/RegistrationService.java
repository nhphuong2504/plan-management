package tz9.Calendar.registration;


import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserRole;
import tz9.Calendar.appUser.AppUserService;
import tz9.Calendar.email.EmailSender;
import tz9.Calendar.registration.token.ConfirmationToken;
import tz9.Calendar.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        String token = appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
        String link = "http://coms-309-040.class.las.iastate.edu:8080/confirm?token=" + token;
        emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link));
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>PlannR Email Confirmation</title>\n" +
                "    <style>\n" +
                "      /* Style the body and text */\n" +
                "      body {\n" +
                "        background-color: #f8f9fa;\n" +
                "        font-family: Arial, Helvetica, sans-serif;\n" +
                "        color: #333;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "      }\n" +
                "      h1 {\n" +
                "        font-size: 48px;\n" +
                "        text-align: center;\n" +
                "        margin-top: 80px;\n" +
                "        color: #212529;\n" +
                "      }\n" +
                "      p {\n" +
                "        font-size: 24px;\n" +
                "        text-align: center;\n" +
                "        margin-top: 40px;\n" +
                "        color: #6c757d;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1>PlannR Email Confirmation</h1>\n" +
                "    <p>Thank you for confirming your email. Your account is now active.</p>\n" +
                "  </body>\n" +
                "</html>";
    }

    private String buildEmail(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>PlannR - Confirm Your Email</title>\n" +
                "    <style>\n" +
                "      /* Fonts */\n" +
                "      @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                "\n" +
                "      /* Styles */\n" +
                "      body {\n" +
                "        background-color: #f2f2f2;\n" +
                "        font-family: 'Roboto', sans-serif;\n" +
                "      }\n" +
                "      .container {\n" +
                "        background-color: #fff;\n" +
                "        max-width: 600px;\n" +
                "        margin: 0 auto;\n" +
                "        padding: 40px;\n" +
                "        border-radius: 8px;\n" +
                "        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);\n" +
                "      }\n" +
                "      h1 {\n" +
                "        text-align: center;\n" +
                "        color: #2196F3;\n" +
                "        font-weight: 700;\n" +
                "        font-size: 2.5rem;\n" +
                "      }\n" +
                "      hr {\n" +
                "        border: none;\n" +
                "        height: 2px;\n" +
                "        background-color: #2196F3;\n" +
                "        margin: 20px auto;\n" +
                "      }\n" +
                "      h2 {\n" +
                "        text-align: center;\n" +
                "        color: #666;\n" +
                "        font-weight: 500;\n" +
                "        font-size: 1.8rem;\n" +
                "      }\n" +
                "      p {\n" +
                "        text-align: center;\n" +
                "        color: #666;\n" +
                "        font-weight: 400;\n" +
                "        font-size: 1.2rem;\n" +
                "        margin: 10px 0;\n" +
                "      }\n" +
                "      a {\n" +
                "        color: #2196F3;\n" +
                "        text-decoration: none;\n" +
                "        font-weight: 500;\n" +
                "        font-size: 1.2rem;\n" +
                "      }\n" +
                "      .button {\n" +
                "        display: block;\n" +
                "        margin: 0 auto;\n" +
                "        padding: 12px 24px;\n" +
                "        background-color: #2196F3;\n" +
                "        color: #fff;\n" +
                "        text-align: center;\n" +
                "        border: none;\n" +
                "        border-radius: 4px;\n" +
                "        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);\n" +
                "        cursor: pointer;\n" +
                "        font-weight: 500;\n" +
                "        font-size: 1.2rem;\n" +
                "        transition: all 0.2s ease;\n" +
                "      }\n" +
                "      .button:hover {\n" +
                "        background-color: #0d8beb;\n" +
                "        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);\n" +
                "      }\n" +
                "      .warning {\n" +
                "        color: #f44336;\n" +
                "        font-weight: 500;\n" +
                "        font-size: 1.2rem;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"container\">\n" +
                "      <h1>PlannR</h1>\n" +
                "      <hr>\n" +
                "      <h2>Confirm Your Email</h2>\n" +
                "      <p>Dear " + name + ",</p>\n" +
                "      <p>Please click on the button below to confirm your email. This link will expire in 10 minutes.</p>\n" +
                "      <a href=\"" + link + "\" class=\"button\">Confirm Email</a>\n" +
                "      <p class=\"warning\">Please note that if you did not sign up for PlannR, you can ignore this email.</p>\n" +
                "      <p>Best regards,</p>\n" +
                "      <p>The PlannR Team</p>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
    }

}
