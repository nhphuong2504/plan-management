package tz9.Calendar.email;

public interface EmailSender {
    void send(String to, String email);
    void sendEventReminder(String to, String email);
}
