package com.example.plannr.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.example.plannr.Calendar.CalendarUtils;
//import com.example.plannr.Calendar.EventDescription;
//import com.example.plannr.EventPopupActivity;
//import com.example.plannr.EventsActivity;

//import com.example.plannr.Calendar.EventDescription;

import com.example.plannr.R;
import com.example.plannr.god;
import com.example.plannr.manageEvents;
import com.example.plannr.utils.UserEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;


/**
 * activity for the creation of userEvents, with the date, time, title and description selected
 * @author Zach R
 */
public class EventActivity extends AppCompatActivity {

    Button timeButton;

    EditText title, description;

   private DatePickerDialog datePickerDialog;
   private Button dateButton;

   LocalDate date;

   LocalTime time;
    int hour, minute;


    //tags
    RadioGroup radioGroup;
    RadioButton radioButton;
    static int tag;

    //repeat
    RadioGroup frequency;
    RadioButton timeFrame;
    Switch repeat;

    NumberPicker numberPicker;
    int occur = 0;
    int number = 0;

    //mappping
    EditText location;
    String address;



    /**
     * initializes the activity layout and connects the buttons for the date and time
     * @param savedInstanceState A Bundle containing data saved from a previous instance of this
     */

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        timeButton = findViewById(R.id.timePicker);
//        initDatePicker();
        dateButton = findViewById(R.id.datePicker);

        //code for tags
        radioGroup = findViewById(R.id.radioGroup);

        //code for repetition
        frequency = findViewById(R.id.frequency);
        //number picker code
        numberPicker = findViewById(R.id.number);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
//        occur = numberPicker.getValue();

        //code for mapping
        location = findViewById(R.id.position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateButton.setText(CalendarUtils.formattedDate(LocalDate.now()));
            timeButton.setText(CalendarUtils.formattedTime(LocalTime.now()));
            date = LocalDate.now();
            time = LocalTime.now();
        }
        title = findViewById(R.id.titleOfEvent);
        description = findViewById(R.id.descriptionOfEvent);

//        god.setContext(getApplicationContext());

        repeat = findViewById(R.id.repeat);
        if(god.editing){
            location.setText(god.selectedEvent.getAddress());
            tag = god.selectedEvent.getTag();
            title.setText(god.selectedEvent.GetTitle());
            title.setEnabled(false);
            //edit what can and cant be changed when editing
            repeat.setEnabled(false);
            description.setText(god.selectedEvent.GetDescription());
            dateButton.setText(CalendarUtils.formattedDate(god.selectedEvent.getDateR()));
            timeButton.setText(CalendarUtils.formattedTime(god.selectedEvent.getTimeR()));
        }

    }

    /**
     * method to select the date fro the event to take place on and displays it, uses a date picker to do this
     * @param view view that this action is triggered on
     */
    public void selectDateAction(View view){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    date = LocalDate.of(year, month, day);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dateButton.setText(CalendarUtils.formattedDate(date));
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    /**
     * method to select the time for teh event to take place at and display that time, this is done using a time picker
     * @param view view that this action is triggered on
     */
    public void selectTimeAction(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                hour = h;
                minute = m;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    time = LocalTime.of(hour, minute, 0);
                }
                timeButton.setText(CalendarUtils.formattedTime(time));
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    /**
     * this method cancels teh event creation and takes you back to the event management page
     * @param view view this action takes place on
     */
    public void cancelEventCreation(View view){
        startActivity(new Intent(this, manageEvents.class));
    }

    /**
     * this method saves and creates teh event, adding it to the local arrays as well as adding it
     * to the data stored in backend, and then takes you back to the eventManagement page
     * @param view this is the view that the action takes place on
     */
    public void saveEventAction(View view){
        if(god.editing){
            god.selectedEvent.setDate(date);
            god.selectedEvent.setTime(time);
            god.selectedEvent.setAddress(location.getText().toString());
            god.selectedEvent.setDescription(description.getText().toString());
            god.selectedEvent.setTag(tag);
            god.editing = false;
            god.editEvent(EventActivity.this, god.selectedEvent);
            startActivity(new Intent(this, manageEvents.class));
        }else {
            if(occur == 0){
                number = 0;
            }else{
                number = numberPicker.getValue();
            }
            if (title.getText().toString().length() == 0 || description.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Ensure all fields are filled", Toast.LENGTH_SHORT).show();
            } else {
                address = location.getText().toString();
                //System.out.println(number);

                UserEvent event = new UserEvent(title.getText().toString(), description.getText().toString(), date, time, god.userId, tag, occur, number, address);
                System.out.println(event.getTag());
                //old code without backend implemented
                god.allEvents.add(event);
                god.createdByMe.add(event);
                god.addEvent(EventActivity.this, event);

                //repetition on front end
                LocalDate temp = date;
                for(int i = 1; i < number; i++){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if(occur == 1){
                            temp = temp.plusDays(1);
                        }else if (occur == 2){
                            temp = temp.plusWeeks(1);
                        }else{
                            temp = temp.plusMonths(1);
                        }
                    }

                    event = new UserEvent(title.getText().toString(), description.getText().toString(), temp, time, god.userId, tag, occur, number, address);
                    //old code without backend implemented
                    god.allEvents.add(event);
                    god.createdByMe.add(event);
                }

                //call method to add event to backend
                startActivity(new Intent(this, manageEvents.class));
            }
        }
    }

    //code for tags
    public void checkButton(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if(radioButton.getText().equals("Outdoor")){
            tag = 1;
        }else if(radioButton.getText().equals("Business")){
            tag = 2;
        } else{
            tag = 0;
        }
    }

    public void checkButton1(View view){
        int radioId = frequency.getCheckedRadioButtonId();
        timeFrame = findViewById(radioId);
        if(timeFrame.getText().equals("Weekly")){
            occur = 2;
        }else if(timeFrame.getText().equals("Monthly")){
            occur = 3;
        }else if(timeFrame.getText().equals("Daily")){
            occur = 1;
        }else{
            occur = 0;
        }
    }

    public void repeatAction(View view){
        repeat = findViewById(R.id.repeat);
        if(repeat.isChecked()){
            occur = 1;
//            number = numberPicker.getValue();
            frequency.setVisibility(View.VISIBLE);
            numberPicker.setVisibility(View.VISIBLE);
        } else{
            frequency.setVisibility(View.INVISIBLE);
            numberPicker.setVisibility(View.INVISIBLE);
            number = 0;
            occur = 0;
        }
    }
}