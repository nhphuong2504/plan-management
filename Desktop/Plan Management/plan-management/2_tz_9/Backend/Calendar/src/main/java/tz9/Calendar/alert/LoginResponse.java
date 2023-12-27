package tz9.Calendar.alert;

import tz9.Calendar.appUser.AppUser;

public class LoginResponse {
    private AppUser user;
    private Alert alert;

    public LoginResponse(AppUser user, Alert alert) {
        this.user = user;
        this.alert = alert;
    }

    // Getters and setters for 'user' and 'alert' fields

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }
}

