package com.example.plannr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plannr.Calendar.CalendarUtils;
//import com.example.plannr.Calendar.EventDescription;
import com.example.plannr.utils.Music;
import com.example.plannr.utils.Student;
import com.example.plannr.databinding.ActivityMainBinding;
import com.example.plannr.utils.UserEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;

/**
 * this activity is responsible for being the home page and connecting all of the apps pages together
 * it also displays a brief overlook of the week ahead along with the next event that you have joined
 * @author Zach R
 */
public class MainActivity extends AppCompatActivity {

    Student loggedInStudent;
    ActivityMainBinding binding;
    View view;
    BottomNavigationView navBar;

    private boolean menuOpen = false;

    private ListView hourListView;
    ImageView banner;

    private static UserEvent nextEvent;


    /**
     * links the java code with the xml code, gets data from backend, inflates teh view, initializes widgets
     * and displays the over look of the week and teh next event that you are a part of
     * @param savedInstanceState object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creates the array of user events from local
//        god.setUpUserEvents();
        //try to get events from server

        if(god.setUP == false && god.userId != 0) {
            god.setUP = true;
            god.allEvents(MainActivity.this);
            god.Events(MainActivity.this);
            god.createdByMe(MainActivity.this);
        }
        //messing with



        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Associates Class with activity_main.xml file --Change to binding for nav bar
        //god.userId = 3;
        setContentView(binding.getRoot());//R.layout.activity_main
        banner = (ImageView) findViewById(R.id.banner_main);
        setUpBanner();
        navBar = findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(R.id.Dashboard);
        navBar.setItemIconTintList(null);
        //Music.startMusic();

        //evil dont uncomment unless absoluetely necessary
        //Music.startMusic();


        initWidget();
        if(god.admin == true){
            findViewById(R.id.admin).setVisibility(View.INVISIBLE);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item ->
        {
            switch(item.getItemId())
            {
                case R.id.Calendar:
                    switchActivity(binding.getRoot(), 0);
                    break;
                case R.id.Chat:
                    switchActivity(binding.getRoot(), 1);
                    break;
                case R.id.Events:
                    switchActivity(binding.getRoot(), 2);
                    break;
                case R.id.Friends:
                    switchActivity(binding.getRoot(), 3);
                    break;
                case R.id.Dashboard:
                    switchActivity(binding.getRoot(), 4);
                    break;
            }

            return true;
        });
        loggedInStudent = new Student();
        displayEvent();
        god.setContext(getApplicationContext());
    }

    /**
     * method links the hourList View with the view in the xml code
     */
    private void initWidget() {
        hourListView = findViewById(R.id.hourListView);
    }


    /**
     * sets teh hour adapter when the activity resumes, to keep data up to date
     */
    @Override
    protected void onResume(){
        super.onResume();
        setHourAdapter();
    }

    /**
     * creates a new hour adapter
     */
    private void setHourAdapter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = LocalDate.now();
            //hourAdapter hAdapter =  new hourAdapter(getApplicationContext(), hourEventList());
            hourListView.setAdapter(new hourAdapter(getApplicationContext(), hourEventList()));
        }
    }

    /**
     * gets a list of the events that take place at a certain hour
     * @return returns a list of events at a certain hour
     */
    private ArrayList<hourEvent> hourEventList() {
        ArrayList<hourEvent> list = new ArrayList<hourEvent>();
        for(int hour = 0; hour < 24; hour++){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalTime time = LocalTime.of(hour, 0);
                ArrayList<UserEvent> events = CalendarUtils.eventsForDateAndTime(time);
                hourEvent hEvent = new hourEvent(time, events);
                if(events.size() != 0){
                    list.add(hEvent);
                }
            }
        }
        return list;
    }

    /**
     * method displays the next upcoming event that you are a part of excluding events that you are
     * the creator for, update and sets teh respecting views
     */
    private void displayEvent(){
        UserEvent nextEvent = findNextEvent();
        //change title
        if(nextEvent != null) {
            TextView textViewToChange = (TextView) findViewById(R.id.event_title);
            textViewToChange.setText(nextEvent.GetTitle());
            //change date and time
            textViewToChange = (TextView) findViewById(R.id.event_date);
            textViewToChange.setText(CalendarUtils.formattedDate(nextEvent.getDateR()) + ": " + CalendarUtils.formattedShortTime(nextEvent.getTimeR()));
            //change event image
            ImageView image = (ImageView) findViewById(R.id.event_image);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (nextEvent.getDateR().equals(LocalDate.now())) {
                    image.setImageResource(R.drawable.eventlistitembackgroundtoday);
                } else {
                    image.setImageResource(R.drawable.eventlistitembackgroundupcoming);
                }
            }
        }
    }

    /**
     * method finds teh next upcoming event that user has joined
     * @return returns teh next upcoming event that the user has joined
     */
    public static UserEvent findNextEvent(){
        ArrayList<UserEvent> possible = new ArrayList<>();
        for(int i = 0; i < god.events.size(); i++){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if((god.events.get(i).getDateR().equals(LocalDate.now()) && god.events.get(i).getTimeR().isAfter(LocalTime.now())) || god.events.get(i).getDateR().isAfter(LocalDate.now())){
                    possible.add(god.events.get(i));
                }
            }
        }
        if(possible.size() == 0){
            nextEvent = null;
            return null;
        }
        CalendarUtils.orderTimeAndDate(possible);
        nextEvent =  possible.get(0);
        return nextEvent;
    }

    /**
     * takes teh user to the login page and logs them out
     * @param view view that the action deals with in this case the button
     */
    public void logOutAction(View view) {
        startActivity(new Intent(this, LoginPageActivity.class));
//        startActivity(new Intent(this, maps.class));
    }

    /**
     * takes teh user to the admin verification page
     * @param view view that the action deals with in this case the button
     */
    public void adminAction(View view){
        startActivity(new Intent(view.getContext(), Admin_activity.class));
    }

    /**
     * takes teh user to the profile and settings page/pages
     * @param view view that the action deals with in this case the button
     */
    public void settingsAction(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    /**
     * method makes buttons visible, "expands" the menu for log out, admin, and settings pages
     * @param view
     */
    public void expand(View view){
        if(menuOpen == false) {
            findViewById(R.id.log).setVisibility(View.VISIBLE);
            findViewById(R.id.prof).setVisibility(View.VISIBLE);
            if(god.admin == false){
                findViewById(R.id.admin).setVisibility(View.VISIBLE);
            }
            menuOpen = true;
        }else{
            findViewById(R.id.log).setVisibility(View.INVISIBLE);
            findViewById(R.id.prof).setVisibility(View.INVISIBLE);
            if(god.admin == false){
                findViewById(R.id.admin).setVisibility(View.INVISIBLE);
            }
            menuOpen = false;
        }
    }

    /**
     * method updates variables and fills in data to an intent which is then called, displaying a
     * popup image of the events data
     * @param view view that the item is part of/that the user is interacting with
     */
    public void popUP(View view){
        god.source = 2;
        god.chosen = true;
        Intent intent = new Intent(MainActivity.this, EventPopupActivity.class);
        intent.putExtra("TITLE", findNextEvent().GetTitle());
        intent.putExtra("DESCRIPTION", findNextEvent().GetDescription());
        intent.putExtra("DATE", findNextEvent().GetDateNoColon());
        intent.putExtra("TIME", findNextEvent().GetTime());
        god.selectedEvent = findNextEvent();
        startActivity(intent);
    }

    /**
     * method that fills in teh data into an object of student
     * @param intent intent that the students data is being added to
     */
    private void putStudentIntent(Intent intent) {
        intent.putExtra("name", loggedInStudent.getName());
        intent.putExtra("email", loggedInStudent.getEmail());
        intent.putExtra("id", loggedInStudent.getId());
        intent.putExtra("dob", "");
        intent.putExtra("age", loggedInStudent.getAge());
    }

    /**
     * method that fills in teh data into an object of student
     * @param intent intent that the students data is being added to
     */
    private void putStudentIntent(Intent intent, Student student) {
        intent.putExtra("name", student.getName());
        intent.putExtra("email", student.getEmail());
        intent.putExtra("id", student.getId());
        intent.putExtra("dob", "");
        intent.putExtra("age", "");
    }

    /**
     * the method switches the activities based on the icon selected from teh bottom of the page on
     * the navigation bar
     * @param view view of the button clicked
     * @param activity activity that you want to go to
     */
    private void switchActivity(View view, int activity) {
        Intent intent;
        switch (activity) {
            case 0:
                intent = new Intent(view.getContext(), CalendarActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(view.getContext(), ChatSelectionActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(view.getContext(), EventsActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(view.getContext(), FriendsListActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void setUpBanner() {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        if(hour < 5)
        {
            banner.setImageResource(R.drawable.f1n3);
        }
        else if(hour < 8)
        {
            banner.setImageResource(R.drawable.f1d1);
        }
        else if(hour < 11)
        {
            banner.setImageResource(R.drawable.f1d2);
        }
        else if(hour < 14)
        {
            banner.setImageResource(R.drawable.f1d3);
        }
        else if(hour < 17)
        {
            banner.setImageResource(R.drawable.f1d4);
        }
        else if(hour < 20)
        {
            banner.setImageResource(R.drawable.f1d5);
        }
        else
        {
            banner.setImageResource(R.drawable.f1n3);
        }
    }

}