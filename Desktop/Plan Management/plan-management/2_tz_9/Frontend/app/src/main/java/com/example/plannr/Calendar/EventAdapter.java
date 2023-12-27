package com.example.plannr.Calendar;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.plannr.Calendar.EventDescription;
import com.example.plannr.EventRecyclerInterface;
import com.example.plannr.R;
import com.example.plannr.utils.UserEvent;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * class creates and binds view for the events in the calendar
 * @author Zach R
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>{
    Context context;
    ArrayList<UserEvent> events;

    private final EventRecyclerInterface eventRecyclerInterface;


    /**
     * eventAdapter constructor
     * @param context context to be used for the adapter
     * @param events array of events to be displayed
     * @param eventRecyclerInterface interface for item click listener
     */
    public EventAdapter(Context context, ArrayList<UserEvent> events, EventRecyclerInterface eventRecyclerInterface){
        this.context = context;
        this.events = events;
        this.eventRecyclerInterface = eventRecyclerInterface;
    }

    /**
     * creates a new viewHolder for the recycler view
     * @param parent View group to which to add the new view
     * @param viewType an int to determine the viw type
     * @return a new view-holder
     */
    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new EventAdapter.MyViewHolder(view, eventRecyclerInterface);
    }

    /**
     * binds a view-holder with data
     * @param holder view-holder to bind
     * @param position position of data in the array
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.eventTitle.setText(events.get(position).GetTitle());
        holder.eventTime.setText(events.get(position).GetTime());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(CalendarUtils.selectedDate.isBefore(LocalDate.now())){
                holder.eventImage.setImageResource(R.drawable.eventlistitembackgroundpast);
            }
            else if(CalendarUtils.selectedDate.equals(LocalDate.now())) {
                holder.eventImage.setImageResource(R.drawable.eventlistitembackgroundtoday);
            }else{
                holder.eventImage.setImageResource(R.drawable.eventlistitembackgroundupcoming);
            }
        }
    }

    /**
     * method returns the size of the events array
     * @return arrays size
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * class that is used to make a custom ViewHolder for the RecyclerView.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView eventTitle, eventDate, eventTime;
        ImageView eventImage;

        /**
         * constructor for a new MyViewHolder
         * @param itemView view that the view holder will hold
         * @param eventRecyclerInterface interface used to handle when events are clicked on
         */
        public MyViewHolder(@NonNull View itemView, EventRecyclerInterface eventRecyclerInterface) {
            super(itemView);

            eventTitle = (TextView)itemView.findViewById(R.id.card_event_title);
            eventTime = (TextView)itemView.findViewById(R.id.card_event_date);
            eventImage = (ImageView)itemView.findViewById(R.id.card_event_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * method to get the position of the item clicked on in teh view
                 * @param view - view that is being interacted with
                 */
                @Override
                public void onClick(View view) {
                    if(eventRecyclerInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            eventRecyclerInterface.onItemClick(pos);
                        }
                    }
                }
            });


        }
    }

}


