package com.example.plannr;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.plannr.Calendar.CalendarUtils;
import com.example.plannr.utils.UserEvent;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is an array adapter that manages teh list of hourEvent objects and displays them
 * @author Zach R
 */
public class hourAdapter extends  ArrayAdapter<hourEvent> {

    /**
     * hourAdapter contractor
     * @param context the context of the application
     * @param hourEvents the list of hourEvent objects to be displayed
     */
    public hourAdapter(@NonNull Context context, List<hourEvent> hourEvents){
        super(context, 0, hourEvents);
    }

    /**
     * Returns the View for a specific position in the list.
     * @param position the position of the item within the adapter's data set
     * @param convertView the old view to reuse, if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the View corresponding to the data at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        hourEvent event = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hourcell, parent, false);
       // TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

        setHour(convertView, event.time);
        setWeek(convertView, event.events);

        return convertView;
    }

    /**
     * Sets the week view for the events in a specific hour.
     * @param convertView the View for the list item
     * @param events the list of UserEvent objects to be displayed
     */
    private void setWeek(View convertView, ArrayList<UserEvent> events) {
        TextView sun =  convertView.findViewById(R.id.sun);
        TextView mon =  convertView.findViewById(R.id.mon);
        TextView tue =  convertView.findViewById(R.id.tue);
        TextView wed =  convertView.findViewById(R.id.wed);
        TextView thur =  convertView.findViewById(R.id.thur);
        TextView fri =  convertView.findViewById(R.id.fri);
        TextView sat =  convertView.findViewById(R.id.sat);

        hideEvent(sun);
        hideEvent(mon);
        hideEvent(tue);
        hideEvent(wed);
        hideEvent(thur);
        hideEvent(fri);
        hideEvent(sat);

        for(int i = 0; i < events.size(); i++){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(events.get(i).getDateR().getDayOfWeek() == DayOfWeek.SUNDAY){
                    setEvent(sun, events.get(i));
                }else if(events.get(i).getDateR().getDayOfWeek() == DayOfWeek.MONDAY){
                    setEvent(mon, events.get(i));
                }else if(events.get(i).getDateR().getDayOfWeek() == DayOfWeek.TUESDAY){
                    setEvent(tue, events.get(i));
                }else if(events.get(i).getDateR().getDayOfWeek() == DayOfWeek.WEDNESDAY){
                    setEvent(wed, events.get(i));
                }else if(events.get(i).getDateR().getDayOfWeek() == DayOfWeek.THURSDAY){
                    setEvent(thur, events.get(i));
                }else if(events.get(i).getDateR().getDayOfWeek() == DayOfWeek.FRIDAY){
                    setEvent(fri, events.get(i));
                }else if(events.get(i).getDateR().getDayOfWeek() == DayOfWeek.SATURDAY){
                    setEvent(sat, events.get(i));
                }
            }
        }
    }

    /**
     * Sets teh event title and make the text View visible using the passed in event information
     * @param textView the view to make visible
     * @param event the event that you want ot display
     */
    private void setEvent(TextView textView, UserEvent event){
        textView.setText(event.GetTitle());
        textView.setVisibility((View.VISIBLE));
    }

    /**
     * this method makes teh given text view invisible
     * @param tv the text view to make invisible
     */
    private void hideEvent(TextView tv) {
        tv.setVisibility(View.INVISIBLE);
    }

    /**
     * this method sets the time for the given convert view using the passed in time
     * @param convertView the convert view to change
     * @param time the time that you want to use to set the convert view
     */
    private void setHour(View convertView, LocalTime time) {
        TextView timeTV =  convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtils.formattedShortTime(time));
    }
}
