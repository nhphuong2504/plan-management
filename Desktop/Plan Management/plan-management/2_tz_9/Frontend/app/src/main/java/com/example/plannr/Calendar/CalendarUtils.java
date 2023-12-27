package com.example.plannr.Calendar;

import android.os.Build;

import com.example.plannr.god;
import com.example.plannr.utils.UserEvent;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *  * adapter for the calendar that is implemented in our app
 *  * @author Zach R
 */
public class CalendarUtils {
    public static LocalDate selectedDate;


    /**
     * Orders a list of UserEvents based on their, from earliest to latest.
     * @param events the list of UserEvents to be ordered
     */
    public static void order(ArrayList<UserEvent> events){
        for(int i = 1; i < events.size(); i ++){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(events.get(i).getTimeR().isBefore(events.get(i - 1).getTimeR())){
                    UserEvent temp = events.get(i);
                    events.set(i, events.get(i - 1));
                    events.set(i - 1, temp);
                }
            }
        }
    }

    /**
     * Formats a LocalDate to a human readable date
     * @param date the LocalDate to be formatted
     * @return a string representation of the date in "MM/dd/yyyy" format
     */
    public static String formattedDate(LocalDate date) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return date.format(format);
        }
        return null;
    }

    /**
     * Formats a LocalTime to human readable format, with an am/pm indicator
     * @param time the LocalTime to be formatted
     * @return a string representation of the time in "hh:mm a" format
     */
    public static String formattedTime(LocalTime time) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("hh:mm a");
            return time.format(format);
        }
        return null;
    }

    /**
     * Formats a LocalTime into a human readable format
     * @param time the LocalTime to be formatted
     * @return a string representation of the time in "HH:mm" format
     */
    public static String formattedShortTime(LocalTime time) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
            return time.format(format);
        }
        return null;
    }

    /**
     * Returns an ArrayList of LocalDate for teh days in a month
     * @param date the LocalDate for which to get the days in its month
     * @return an ArrayList of LocalDate objects representing the days in the specified month
     */
    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            YearMonth yearMonth = YearMonth.from(date);
            int daysInMonth = yearMonth.lengthOfMonth();

            LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
            int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

            for(int i = 1; i <= 42; i++){
                if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                    daysInMonthArray.add(null);
                }else{
                    // daysInMonthArray.add(String.valueOf(i + dayOfWeek));
                    daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(),i - dayOfWeek));
                }
            }
            return daysInMonthArray;
        }
        return null;
    }

    /**
     * Returns an ArrayList of LocalDates representing the days in the week of the given selectedDate.
     * @param selectedDate the day of the week currently selected
     * @return returns an array list of the dates representing the days of the week for teh given date
     */
    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate){
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate endDate = current.plusWeeks(1);
            while(current.isBefore(endDate)){
                days.add(current);
                current = current.plusDays(1);
            }
        }
        return days;
    }


    /**
     * given a selected date, gets the sunday for that week
     * @param current the day of the week you want to find Sunday for
     * @return the sunday of the week
     */
    private static LocalDate sundayForDate(LocalDate current) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate oneWeekAgo = current.minusWeeks(1);
            while(current.isAfter(oneWeekAgo)){
                if(current.getDayOfWeek() == DayOfWeek.SUNDAY){
                    return current;
                } else{
                    current = current.minusDays(1);
                }
            }
            return null;
        }
        return null;
    }


    /**
     * gets all the events for a week
     * @return the array list of the weeks events.
     */
    public static ArrayList<UserEvent> weeklyEvents(){
        ArrayList<UserEvent> events = new ArrayList<>();
        LocalDate current = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            current = sundayForDate(LocalDate.now());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate endDate = current.plusWeeks(1);
            for(int i = 0; i < god.events.size(); i++){
                if((god.events.get(i).getDateR().isAfter(current) || god.events.get(i).getDateR().equals(current)) && (god.events.get(i).getDateR().isBefore(endDate) || god.events.get(i).getDateR().equals(current))){
                    events.add(god.events.get((i)));
                }
            }
        }
        return events;
    }

    /**
     * gets the events at a specific time and date
     * @param time the time for events
     * @return an array of events at that time
     */
    public static ArrayList<UserEvent> eventsForDateAndTime(LocalTime time){
        CalendarUtils.order(weeklyEvents());
        ArrayList<UserEvent> daysEvents = new ArrayList<UserEvent>();
        for(UserEvent event : weeklyEvents()){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int eventHour = event.getTime().getHour();
                int cellHour = time.getHour();
                if(eventHour == cellHour){
                    daysEvents.add(event);
                }
            }
        }
        return daysEvents;
    }

    /**
     * takes in a date and gets the month and year for it
     * @param date date to get the month and year from
     * @return formatted month day and year for the date passed in.
     */
    public static String monthYearFromDate(LocalDate date){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
            return date.format(formatter);
        }
        return null;
    }

    /**
     * gets the events that occur on a selected date
     * @param date the selected ate
     * @return an array list of the events on teh selected date
     */
    public static ArrayList<UserEvent> eventsForDate(LocalDate date){
        CalendarUtils.order(god.events);
        ArrayList<UserEvent> daysEvents = new ArrayList<UserEvent>();
        for(UserEvent event : god.events){
            if(event.getDateR().equals(date)){
                daysEvents.add(event);
            }
        }
        return daysEvents;
    }

    /**
     * Orders the events in the array by date and time
     * @param events array to be ordered
     */
    public static void orderTimeAndDate(ArrayList<UserEvent> events){
        for(int i = 1; i < events.size(); i ++){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(events.get(i).getDateR().isBefore(events.get(i-1).getDateR())){
                    UserEvent temp = events.get(i);
                    events.set(i, events.get(i - 1));
                    events.set(i - 1, temp);
                } else if(events.get(i).getDateR().equals(events.get(i-1).getDateR())){
                    if(events.get(i).getTimeR().isBefore(events.get(i - 1).getTimeR())){
                        UserEvent temp = events.get(i);
                        events.set(i, events.get(i - 1));
                        events.set(i - 1, temp);
                    }
                }
            }
        }
    }
}
