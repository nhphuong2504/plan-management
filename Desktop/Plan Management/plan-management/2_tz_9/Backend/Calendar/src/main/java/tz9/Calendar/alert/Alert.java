package tz9.Calendar.alert;

public class Alert {
    private String message;

    public Alert() {
        message = "";
    }

    public Alert(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
