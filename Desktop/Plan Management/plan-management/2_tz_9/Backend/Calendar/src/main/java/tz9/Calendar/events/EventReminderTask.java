package tz9.Calendar.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tz9.Calendar.events.Event;
import tz9.Calendar.events.EventRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class EventReminderTask {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailSender emailSender;

    @Scheduled(fixedRate = 60000) // Checks every minute
    public void sendEventReminders() {
        List<Event> events = eventRepository.findAll();

        events.stream()
                .filter(event -> {
                    LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getTime());
                    return eventDateTime.minusMinutes(30).isBefore(LocalDateTime.now()) && eventDateTime.minusMinutes(29).isAfter(LocalDateTime.now());
                })
                .forEach(event -> event.getParticipatingUsers().forEach(user -> {
                    String emailContent = String.format("<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <title>Event Reminder</title>\n" +
                            "    <style>\n" +
                            "        body {\n" +
                            "            font-family: Arial, sans-serif;\n" +
                            "            font-size: 16px;\n" +
                            "            line-height: 1.5;\n" +
                            "            color: #333;\n" +
                            "            background-color: #f5f5f5;\n" +
                            "            margin: 0;\n" +
                            "            padding: 30px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .container {\n" +
                            "            background-color: #fff;\n" +
                            "            padding: 30px;\n" +
                            "            max-width: 600px;\n" +
                            "            margin: 0 auto;\n" +
                            "            border-radius: 5px;\n" +
                            "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                            "        }\n" +
                            "\n" +
                            "        h1 {\n" +
                            "            font-size: 28px;\n" +
                            "            color: #2c3e50;\n" +
                            "            margin-bottom: 20px;\n" +
                            "            border-bottom: 3px solid #3498db;\n" +
                            "            display: inline-block;\n" +
                            "            padding-bottom: 5px;\n" +
                            "        }\n" +
                            "\n" +
                            "        p {\n" +
                            "            margin-bottom: 10px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .event-details {\n" +
                            "            background-color: #f5f5f5;\n" +
                            "            padding: 15px;\n" +
                            "            margin: 20px 0;\n" +
                            "            border-radius: 5px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .event-details ul {\n" +
                            "            list-style-type: none;\n" +
                            "            padding: 0;\n" +
                            "            margin: 0;\n" +
                            "        }\n" +
                            "\n" +
                            "        .event-details li {\n" +
                            "            margin-bottom: 5px;\n" +
                            "            color: #2c3e50;\n" +
                            "        }\n" +
                            "\n" +
                            "        .event-details strong {\n" +
                            "            color: #3498db;\n" +
                            "        }\n" +
                            "\n" +
                            "        .footer {\n" +
                            "            font-size: 14px;\n" +
                            "            text-align: center;\n" +
                            "            color: #999;\n" +
                            "            margin-top: 20px;\n" +
                            "        }\n" +
                            "    </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "    <div class=\"container\">\n" +
                            "        <h1>Event Reminder</h1>\n" +
                            "        <p>Dear " + user.getFirstName() + ",</p>\n" +
                            "        <p>This is a friendly reminder that the event <strong>" + event.getTitle() + "</strong> will start in 30 minutes. Please make sure to be on time.</p>\n" +
                            "        <div class=\"event-details\">\n" +
                            "            <ul>\n" +
                            "                <li><strong>Title:</strong> "+ event.getTitle() + "</li>\n" +
                            "                <li><strong>Description:</strong> "+ event.getDescription() + "</li>\n" +
                            "                <li><strong>Date:</strong> " + event.getDate() + "</li>\n" +
                            "                <li><strong>Time:</strong> " + event.getTime() + "</li>\n" +
                            "                <li><strong>Location:</strong> "+ event.getLocation() + "</li>\n" +
                            "            </ul>\n" +
                            "        </div>\n" +
                            "        <p>See you there!</p>\n" +
                            "        <p>Best regards,</p>\n" +
                            "        <p>The PlannR Team</p>\n" +
                            "    </div>\n" +
                            "    <div class=\"footer\">\n" +
                            "        This email was sent by PlannR. Please do not reply to this message.\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>\n", event.getTitle());
                    emailSender.sendEventReminder(user.getEmail(), emailContent);
                }));
    }
}
