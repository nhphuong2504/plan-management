package com.example.plannr;

import static com.example.plannr.Calendar.CalendarUtils.daysInMonthArray;
import static com.example.plannr.Calendar.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannr.Calendar.CalendarAdapter;
import com.example.plannr.Calendar.CalendarUtils;
//import com.example.plannr.Calendar.WeekViewActivity;
import com.example.plannr.Calendar.*;
import com.example.plannr.databinding.ActivityCalendarBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * this class displays the monthly calendar view with that months respective dates and lets teh user
 * navigate by clicking on those dates
 * @author Zach R
 */
public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthText;
    private RecyclerView calendarRecyclerView;

    ActivityCalendarBinding binding;
    View view;
    BottomNavigationView navBar;
    ImageView banner;

    /**
     * connects the java code with the xml layout, also sets the nab bar and inflated teh layout
     * @param savedInstanceState saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        // Associates Class with activity_main.xml file --Change to binding for nav bar
        setContentView(binding.getRoot());//R.layout.activity_main
        navBar = findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(R.id.Calendar);
        navBar.setItemIconTintList(null);
        banner = (ImageView) findViewById(R.id.banner_cal);
        setUpBanner();
        initWidgets();

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
        god.setContext(getApplicationContext());
    }

    /**
     * this method sets the months views, and update the selected date at teh start to today's date
     * initializes the calendarRecyclerView
     */
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.monthlyRecyclerview);
        monthText = findViewById(R.id.monthTV);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = LocalDate.now();
        }
        setMonthView();
    }

    /**
     * sets teh recycler view to display data for teh current month based on the selected date
     * creates a new adapter with the dates of teh days in the month
     */
    private void setMonthView() {

        monthText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }


    /**
     * sets teh months view and displays data for the previous month
     * @param view view of the button clicked
     */
    public void previousMonthAction(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
            setMonthView();
        }
    }

    /**
     * sets teh months view and displays data for the next month
     * @param view view of the button clicked
     */
    public void nextMonthAction(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
            setMonthView();
        }
    }

    /**
     * this method interacts with the recycler view to get what item is clicked on due to users input,
     * and then sets the months view
     * @param position - where the item clicked on is, in the days array
     * @param date - the Local date assigned to the item clicked on
     */
    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date != null) {
            CalendarUtils.selectedDate = date;
            god.chosen = true;
            setMonthView();
            weeklyAction(view);
        }
    }

    /**
     * this method takes you to the weekly calendar activity
     * @param view view of the button clicked
     */
    public void weeklyAction(View view) {
        startActivity(new Intent(this, WeekViewActivity.class));
    }

    /**
     * the method switches the activities based on the icon selected from teh bottom of the page on
     * the navigation bar
     * @param view view of the button clicked
     * @param activity activity that you want to go to
     */
    private void switchActivity(View view, int activity)
    {
        Intent intent;
        switch(activity)
        {
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
