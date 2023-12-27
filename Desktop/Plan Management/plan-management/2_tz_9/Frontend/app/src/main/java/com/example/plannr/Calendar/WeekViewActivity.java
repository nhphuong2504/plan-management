package com.example.plannr.Calendar;

import static com.example.plannr.Calendar.CalendarUtils.daysInWeekArray;
import static com.example.plannr.Calendar.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.plannr.CalendarActivity;
import com.example.plannr.EventPopupActivity;
import com.example.plannr.EventRecyclerInterface;
import com.example.plannr.R;
import com.example.plannr.god;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * displays the weekly view of the calender and teh events for each day of the week
 * @author Zach R
 */
public class  WeekViewActivity extends AppCompatActivity implements  CalendarAdapter.OnItemListener, EventRecyclerInterface  {

    private TextView monthText;
    private RecyclerView calendarRecyclerView;

    /**
     * initializes the widgets and sets the week view
     * @param savedInstanceState object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        initWidgets();
        setWeekView();
        god.setContext(getApplicationContext());
    }

    /**
     * gets the data for teh previous week and changes teh view for the past week
     * @param view - view that the action deals with in this case the button
     */
    public void previousWeekAction(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
            setWeekView();
        }
    }

    /**
     * gets the data for teh next week and changes teh view for the next week
     * @param view - view that the action deals with in this case the button
     */
    public void nextWeekAction(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
            setWeekView();
        }
    }

    /**
     * takes you out of the week view and back to the monthly view for the calendar
     * @param view - view that the action deals with in this case the button
     */
    public void backToMonthlyAction(View view){
        startActivity(new Intent(this, CalendarActivity.class));
    }

    /**
     * this method sets teh week view and updates the selected date depending on the conditional value taken
     */
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.monthlyRecyclerview);
        monthText = findViewById(R.id.monthTV);
       // eventListView = findViewById(R.id.eventListView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(god.chosen == false) {
                CalendarUtils.selectedDate = LocalDate.now();
            }
            god.chosen = false;
        }
        setWeekView();
    }


    /**
     * sets the weeks view, creates an array of the days in that week according to the
     * selected date and sets teh overall calendar adapter
     */
    private void setWeekView() {
        monthText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    /**
     * method to update the select date based on the calendar date clicked on and
     * sets the week view
     * @param position position of the date clicked on
     * @param date date of the item clicked on
     */
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
        refresh();
    }


    /**
     * method refreshes the data displayed in the recycler view
     */
    protected void refresh(){
        RecyclerView recyclerView = findViewById(R.id.eventRecyclerView);
        EventAdapter adapter = new EventAdapter(this, CalendarUtils.eventsForDate(CalendarUtils.selectedDate), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * method ensures that event data is up to date, when the activity continues
     */
    @Override
    protected void onResume(){
        super.onResume();
        refresh();
    }

    /**
     * method updates variables and puts data into an intent which is then called, displaying a
     * popup image of the events data
     * @param position position of the event/item that is clicked on
     */
    @Override
    public void onItemClick(int position) {
        god.source = 1;
        god.chosen = true;
        Intent intent = new Intent(WeekViewActivity.this, EventPopupActivity.class);
        intent.putExtra("TITLE", CalendarUtils.eventsForDate(CalendarUtils.selectedDate).get(position).GetTitle());
        intent.putExtra("DESCRIPTION", CalendarUtils.eventsForDate(CalendarUtils.selectedDate).get(position).GetDescription());
        intent.putExtra("DATE", CalendarUtils.eventsForDate(CalendarUtils.selectedDate).get(position).GetDateNoColon());
        intent.putExtra("TIME", CalendarUtils.eventsForDate(CalendarUtils.selectedDate).get(position).GetTime());
        god.selectedEvent = CalendarUtils.eventsForDate(CalendarUtils.selectedDate).get(position);
        startActivity(intent);
    }
}