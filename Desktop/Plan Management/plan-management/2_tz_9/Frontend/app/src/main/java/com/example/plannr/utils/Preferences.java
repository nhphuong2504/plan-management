package com.example.plannr.utils;

public class Preferences {
    public boolean notifications;
    public boolean showOnline;
    public boolean showEmail;

    public Preferences(boolean note, boolean online, boolean email)
    {
        notifications = note;
        showOnline = online;
        showEmail = email;
    }
}
