package com.example.plannr.Calendar;


import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannr.R;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * adapter for the calendar that is implemented in our app
 * @author Zach R
 */
public class CalendarAdapter extends RecyclerView.Adapter <com.example.plannr.Calendar.CalendarViewHolder> {

    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    /**
     * constructor for the calendar adapter
     * @param days - array of the days to be passed into the adapter
     * @param onItemListener - item listener used with the adapter to check for when days get clicked on
     */
    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener){
        this.days = days;
        this.onItemListener = onItemListener;
    }

    /**
     * creates view holder to display 31 to 28 days, with the day of the month that it is
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View
     * @return a new CalendarViewHolder
     */
    @NonNull
    @Override
    public com.example.plannr.Calendar.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.day, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if(days.size() > 15){
            layoutParams.height = (int)(parent.getHeight() * 0.1666666666666);
        } else{
            layoutParams.height = (int)(parent.getHeight());
        }
        return new com.example.plannr.Calendar.CalendarViewHolder(view, onItemListener, days);
    }

    /**
     * binds the view holder to the data passed in
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull com.example.plannr.Calendar.CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        if(date == null){
            holder.dayOfMonth.setText("");
        } else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            }
            if(date.equals(com.example.plannr.Calendar.CalendarUtils.selectedDate)){
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    /**
     * method to get the size of an array
     * @return the size of the days array
     */
    @Override
    public int getItemCount() {
        return days.size();
    }

    /**
     * Defines an interface with an OnItemClick method.
     */
    public interface OnItemListener{
        /**
         * this method checks for when an item is clicked on
         * @param position - where the item clicked on is, in the days array
         * @param date - the Local date assigned to the item clicked on
         */
        void onItemClick(int position, LocalDate date);
    }
}
