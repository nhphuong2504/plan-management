package com.example.plannr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannr.Calendar.EventActivity;
import com.example.plannr.databinding.ActivityEventsBinding;
import com.example.plannr.utils.EventAdapter;
import com.example.plannr.utils.UserEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * this events activity class is used to display the lists of events that teh user wants to see
 * and managing the events page of the application
 * @author Zach R and Spencer
 */
public class EventsActivity extends AppCompatActivity implements EventRecyclerInterface {

    ArrayList<UserEvent> eventList = new ArrayList<UserEvent>();
    ActivityEventsBinding binding;

    RadioGroup radioGroup;
    RadioButton radioButton;

    Button btn;
    View view;
    BottomNavigationView navBar;
    ImageView banner;

    /**
     * This method is called when the activity is created. It initializes the activity, sets the
     * layout, and populates the list of events. It also sets up the bottom navigation bar and
     * associates the navigation bar with the appropriate activities.
     * @param savedInstanceState This bundle contains data from the previously destroyed activity
     */
    @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            binding = ActivityEventsBinding.inflate(getLayoutInflater());
            // Associates Class with activity_main.xml file --Change to binding for nav bar
            setContentView(binding.getRoot());//R.layout.activity_main
            navBar = findViewById(R.id.bottomNavigationView);
            navBar.setSelectedItemId(R.id.Events);
            navBar.setItemIconTintList(null);
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
                //eventsObject  = new JSONObject();
                return true;
            });
            banner = (ImageView) findViewById(R.id.banner);
            setUpBanner();
            RecyclerView recyclerView = findViewById(R.id.eventRecyclerView);

            EventAdapter adapter;
            btn = (Button) findViewById(R.id.mine);
            if(god.personal == 1){
                btn.setText("Personal");
                eventList = god.events;
            }
            else if(god.personal == 2){
                btn.setText("Top");
                //testing for basic layout
                god.poll();
                eventList = god.top;
            }
            else {
                btn.setText("Public");
                eventList = god.allEvents;
            }
            //code for tags
            radioGroup = findViewById(R.id.radioGroup);
            if(god.tag != 3){
                if(god.tag == 0){
                    radioGroup.check(R.id.social);
                }else if(god.tag == 1){
                    radioGroup.check(R.id.outdoor);
                }else{
                    radioGroup.check(R.id.business);
                }
                ArrayList<UserEvent> temp = new ArrayList<>();
                for(int i = 0; i < eventList.size(); i++){
                    System.out.println(eventList.get(i).getTag());
                    if(eventList.get(i).getTag() == god.tag){
                        temp.add(eventList.get(i));
                    }
                }
                eventList = temp;

            }
            sortEvents(eventList);
            adapter = new EventAdapter(this, eventList, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    /**
     * this method is used to update the list of events that is being displayed
     * @param updateList the new list to display
     */
    public void updateList(ArrayList<UserEvent> updateList) {
        eventList.clear();
        eventList.addAll(updateList);
        sortEvents(eventList);
    }

    /**
     * this method switches which events the user is looking at
     * @param view the view that the button is dealing with
     */
    public void swapEventsAction(View view) {
        if (god.personal == 1) {
            god.personal = 2;
        } else if (god.personal == 2){
            god.personal = 0;
        }else{
            god.personal = 1;
        }
        startActivity(new Intent(this, EventsActivity.class));
    }


    public void checkButton(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if(radioButton.getText().equals("Outdoor")){
            god.tag = 1;
        }else if(radioButton.getText().equals("Business")){
            god.tag = 2;
        }else if(radioButton.getText().equals("Social")){
            god.tag = 0;
        } else{
            god.tag = 3;
        }
        startActivity(new Intent(this, EventsActivity.class));
    }



    /**
     * this method takes teh user to the event management page
     * @param view the view that the button is dealing with
     */

    public void manageEventsAction(View view) {
        startActivity(new Intent(this, manageEvents.class));
    }

    /**
     * this method sorts the array list that is passed in
     * @param list the array to be sorted
     */
    private void sortEvents(ArrayList<UserEvent> list)
    {
        if(list.size() != 0) {
            //Sort
            UserEvent temp;
            int replaceIndex;
            for (int i = 0; i < list.size() - 1; i++) {
                temp = list.get(i);
                replaceIndex = i;
                for (int j = i + 1; j < list.size(); j++) {
                    if (list.get(j).isEarlier(temp) == -1) {
                        temp = list.get(j);
                        replaceIndex = j;
                    }
                }
                list.set(replaceIndex, list.get(i));
                list.set(i, temp);
            }

            //Put today's events at top
            if (list.get(0).isToday()) {
                return; //If the first event is today then nothing else needs to be done
            }

            UserEvent temp2;
            replaceIndex = 0;
            int notPast = 0;
            int notUpcoming = 0;

            for (int i = list.size() - 1; i >= 0; i--) {
                if (!list.get(i).isPast()) {
                    notPast = i;
                    break;
                }
            }

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isToday() || list.get(i).isPast()) {
                    notUpcoming = i;
                    break;
                }
            }

            if (notUpcoming > notPast) {
                return; //No events today
            }


            for (int i = notUpcoming; i <= notPast; i++) {
                temp = list.get(i);
                for (int j = i; j > replaceIndex; j--) {
                    list.set(j, list.get(j - 1));
                }
                list.set(replaceIndex, temp);
                replaceIndex++;
            }
        }
    }

    /**
     * the method switches the activity's based on the icon selected from teh bottom of the page on
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

    /**
     * this method interacts with teh recycler view to get what item is clicked on due to users input,
     * and then sets teh months view
     * @param position - where the item clicked on is, in the days array
     */
    public void onItemClick(int position) {
        god.source = 0;
        UserEvent temp = eventList.get(position);
        god.selectedEvent = temp;
        Intent intent = new Intent(EventsActivity.this, EventPopupActivity.class);
        intent.putExtra("TITLE", eventList.get(position).GetTitle());
        intent.putExtra("DESCRIPTION", eventList.get(position).GetDescription());
        intent.putExtra("DATE", eventList.get(position).GetDateNoColon());
        intent.putExtra("TIME", eventList.get(position).GetTime());
        startActivity(intent);
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
