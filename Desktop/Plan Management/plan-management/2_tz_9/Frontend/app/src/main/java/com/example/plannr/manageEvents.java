package com.example.plannr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannr.Calendar.CalendarUtils;
import com.example.plannr.Calendar.EventActivity;
import com.example.plannr.utils.EventAdapter;

/**
 * this class lets you create, edit and delete teh events that you have created
 * @author Zach R
 */
public class manageEvents extends AppCompatActivity implements EventRecyclerInterface{

    EventAdapter adapter;

    /**
     * used link the java with teh xml code as well and connect some of the buttons, is called
     * at the start of the activity, creates a new Event adapter to use
     * @param savedInstanceState object passed to the onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_management);
        findMyCreations();
        CalendarUtils.orderTimeAndDate(god.createdByMe);
        //        god.createdByMe(manageEvents.this);
        RecyclerView recyclerView = findViewById(R.id.eventRecyclerView);
        adapter = new EventAdapter(this, god.createdByMe, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
        god.setContext(getApplicationContext());
    }

    /**
     * method refreshes the data displayed in teh recycler view
     */
    protected void refresh(){
        RecyclerView recyclerView = findViewById(R.id.eventRecyclerView);
        adapter = new EventAdapter(this, god.createdByMe, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * method ensures that event data is up to date, when the activity continues, and notifies the adapter
     * that there might have been changes in teh data that it holds
     */
    @Override
    protected void onResume(){
        super.onResume();
        //god.createdByMe(manageEvents.this);
        adapter.notifyDataSetChanged();
        refresh();
    }

    /**
     * this method takes you to the event creation action
     * @param view view that the action deals with in this case the button
     */
    public void newEventAction(View view){
        startActivity(new Intent(this, EventActivity.class));
    }

    /**
     * this method takes you to the events that you have created
     * @param view view that the action deals with in this case the button
     */
    public void myEventsAction(View view){
        startActivity(new Intent(this, EventsActivity.class));
    }

    /**
     * method updates variables and fills in data to an intent which is then called, displaying a
     * popup image of the events data
     * @param position position of the event/item that is clicked on
     */
    @Override
    public void onItemClick(int position) {
        god.source = 3;
        god.chosen = true;
        Intent intent = new Intent(manageEvents.this, EventPopupActivity.class);
        intent.putExtra("TITLE", god.createdByMe.get(position).GetTitle());
        intent.putExtra("DESCRIPTION", god.createdByMe.get(position).GetDescription());
        intent.putExtra("DATE", god.createdByMe.get(position).GetDateNoColon());
        intent.putExtra("TIME", god.createdByMe.get(position).GetTime());
        god.selectedEvent = god.createdByMe.get(position);
        startActivity(intent);
    }

    /**
     * this method sorts through the array of all the events by user id to find which events are
     * the ones created by you
     */
    private void findMyCreations(){
        //god.createdByMe.clear();
        if(god.admin){
            god.createdByMe = god.allEvents;
        }else {
            for (int i = 0; i < god.events.size(); i++) {
                if (god.events.get(i).getUserID() == god.userId) {
                    god.createdByMe.add(god.events.get(i));
                }
            }
            //god.createdByMe(manageEvents.this);
        }
    }
}
