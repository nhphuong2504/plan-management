package com.example.plannr.Calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannr.R;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * view-holder for the calendar implementation
 * @author Zach R
 */
public class CalendarViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

    public final View parentView;
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;
    private final ArrayList<LocalDate> days;

    /**
     * constructor for the CalendarViewHolder
     * @param itemView item view for the calendar view holder
     * @param onItemListener calendar adapter listener
     * @param days list of days in the calendar view
     */
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.dayOfMonth);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.days = days;
    }

    /**
     * method that runs when the calendar date is clicked on
     * @param view view that is being interacted with
     */
    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }
}
