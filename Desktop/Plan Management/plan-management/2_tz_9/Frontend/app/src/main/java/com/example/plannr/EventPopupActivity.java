package com.example.plannr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;


import com.example.plannr.Calendar.CalendarUtils;
import com.example.plannr.Calendar.EventActivity;

import com.example.plannr.Calendar.WeekViewActivity;
import com.example.plannr.utils.UserEvent;

/**
 * this class displays the events detailed information when and event is selected.
 * @author Zach R and Spencer T
 */
public class EventPopupActivity extends AppCompatActivity {

    TextView Title;

    ToggleButton join;
    TextView Description;
    TextView Date;
    TextView Time;

    TextView tag;

    TextView repeat;
    Button backButton;

    //mapping
    Button directions;

    TextView where;


    /**
     * this method is ran on activity start up. It connects the xml file with the java code, and
     * assigns buttons and views with their xml counterparts. It also runs logic to determine what page
     * to return back to when the popup is closed.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventpopup);
        Title = findViewById(R.id.eventPopupTitle);
        Title.setTextSize(360/god.selectedEvent.GetTitle().length());
        Description = findViewById(R.id.eventPopupDescription);
        Date = findViewById(R.id.eventPopupDate);
        Time = findViewById(R.id.eventPopupTime);
        tag = findViewById(R.id.eventTag);
        repeat = findViewById(R.id.repeats);
        directions = findViewById(R.id.directions);
        where = findViewById(R.id.address);

        if(getIntent().getStringExtra("TITLE").length() > 12)
        {
            Title.setText(god.selectedEvent.GetTitle().substring(0, 12) + "...");
            Title.setTextSize(360/15);
        }
        else
        {
            Title.setText(god.selectedEvent.GetTitle());
        }
        Description.setText(god.selectedEvent.GetDescription());
        Date.setText(god.selectedEvent.GetDateNoColon());
        Time.setText(god.selectedEvent.GetTime());

        Button deleteButton = (Button) findViewById(R.id.deleteEvent);
        Button editButton = (Button) findViewById(R.id.editEvent);
        ToggleButton btn = (ToggleButton) findViewById(R.id.joinEvent);
        if(RSVPed(god.selectedEvent)){
            btn.setChecked(true);
            directions.setVisibility(View.VISIBLE);
        }
        if(god.selectedEvent.getUserID() == god.userId || god.admin == true){
            btn.setVisibility(View.INVISIBLE);
        }
        if(god.source == 3)
        {
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
        }else{
            deleteButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.INVISIBLE);
        }

        //code for implementing tags
        if(god.selectedEvent.getTag() == 0){
            tag.setText("Type: Social");
        }else if(god.selectedEvent.getTag() == 1){
            tag.setText("Type: Outdoor");
        }else{
            tag.setText("Type: Business");
        }

        if(god.selectedEvent.getFrequency() == 1){
            repeat.setVisibility(View.VISIBLE);
            repeat.setText("Repeats: Daily");
        }else if(god.selectedEvent.getFrequency() == 2){
            repeat.setVisibility(View.VISIBLE);
            repeat.setText("Repeats: Weekly");
        }else if(god.selectedEvent.getFrequency() == 3){
            repeat.setVisibility(View.VISIBLE);
            repeat.setText("Repeats: Monthly");
        }else{
            repeat.setVisibility(View.INVISIBLE);
        }

        where.setText("Loc:" + god.selectedEvent.getAddress());
        backButton = (Button)findViewById(R.id.eventPopupButton);

        /**
         * Creates a listener for the back button click. Returns the user to the view they came from when clicked
         * @param OnClickListener a new View.OnClickListener is created in the parameters
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(god.source == 1){
                    intent = new Intent(view.getContext(), WeekViewActivity.class);
                }else if(god.source == 2){
                    intent = new Intent(view.getContext(), MainActivity.class);
                }else if(god.source == 3){
                    intent = new Intent(view.getContext(), manageEvents.class);
                }else{
                    intent = new Intent(view.getContext(), EventsActivity.class);
                }
                startActivity(intent);
            }
        });
        god.setContext(getApplicationContext());
    }

    /**
     * this method is used when an event creator or administrator wants to remove a public event.
     * It sends a request to backend, which deletes the event
     * @param view view that the button is on
     */
    public void deleteEventAction(View view){
        deleteEvent(god.selectedEvent);
        startActivity(new Intent(view.getContext(), manageEvents.class));
    }


    public void editEventAction(View view){
        //deleteEvent(god.selectedEvent);
        god.editing = true;
        startActivity(new Intent(view.getContext(), EventActivity.class));
    }


    /**
     * this method is used when a user wants to join or leave an event. It adds/removes the user
     * based on whether they are already part of the event or not
     * @param view view that the button is on
     */

    public void RSVPStatusAction(View view) {
        if(RSVPed(god.selectedEvent)){
            leaveEvent(god.selectedEvent);
            Toast.makeText(getApplicationContext(), "How dare you leave!!!!", Toast.LENGTH_SHORT).show();
        }else{
            joinEvent(god.selectedEvent);
            Toast.makeText(getApplicationContext(), "Happy to have you join us", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * this method is used to see whether a user has already joined an event
     * @param event event that is displayed
     */
    private boolean RSVPed(UserEvent event){
        for(int i = 0; i < god.events.size(); i++){
//            if(god.selectedEvent.equals(god.events.get(i))){
            if(event.GetTitle().equals(god.events.get(i).GetTitle()) && event.GetDescription().equals(god.events.get(i).GetDescription())){
                return true;
            }
        }
        return false;
    }

    /**
     * this method is used when a user wants to leave an event. It removes the event from the
     * users list
     * @param event the user is leaving
     */
    private void leaveEvent(UserEvent event){
        //need method to leave event
        god.leaveEvent(EventPopupActivity.this, event);
//        god.Events(EventPopupActivity.this);
         //code without backend
        for(int i = 0; i < god.events.size(); i++){
            if(event.GetTitle().equals(god.events.get(i).GetTitle()) && event.GetDescription().equals(god.events.get(i).GetDescription())){
                god.events.remove(i);
            }
        }
    }

    /**
     * this method is used when a user wants to delete an event. It sends a request to backend to
     * delete the event
     * @param event the user is deleting
     */
    private void deleteEvent(UserEvent event){
        if(event.getUserID() == god.userId || god.admin == true){
            for(int i = 0; i < god.allEvents.size(); i++){
                if(event.GetTitle().equals(god.allEvents.get(i).GetTitle()) && event.GetDescription().equals(god.allEvents.get(i).GetDescription())){
                    god.allEvents.remove(i);
                }
            }
            for(int i = 0; i < god.events.size(); i++){
                if(event.GetTitle().equals(god.events.get(i).GetTitle()) && event.GetDescription().equals(god.events.get(i).GetDescription())){
                    god.events.remove(i);
                }
            }
            for(int i = 0; i < god.createdByMe.size(); i++){
                if(event.GetTitle().equals(god.createdByMe.get(i).GetTitle()) && event.GetDescription().equals(god.createdByMe.get(i).GetDescription())){
                    god.createdByMe.remove(i);
                }
            }
//            test code without backend
//            god.events.clear();
//            god.allEvents.clear();
//            god.createdByMe.clear();

            god.deleteEvent(EventPopupActivity.this, event);

//            god.Events(EventPopupActivity.this);
//            god.createdByMe(EventPopupActivity.this);
//            god.allEvents(EventPopupActivity.this);
        }
    }

    /**
     * this method is used when a user wants to join. It adds the event to the users list of joined
     * events
     * @param event the user is joining
     */
    private void joinEvent(UserEvent event){
        //need method to join event
        god.joinEvent(EventPopupActivity.this, event);
//        god.events.clear();
//        god.Events(EventPopupActivity.this);
        //test code without backed
        god.events.add(event);
    }

    public void getDirections(View view){
        startActivity(new Intent(view.getContext(), maps.class));
    }
}
