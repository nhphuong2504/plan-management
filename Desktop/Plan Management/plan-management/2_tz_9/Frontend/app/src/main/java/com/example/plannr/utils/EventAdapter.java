package com.example.plannr.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannr.EventRecyclerInterface;
import com.example.plannr.R;

import java.util.ArrayList;

/**
 * This is an adapter for the recycler view that displays the events a user is in
 * @author Spencer Thiele
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final EventRecyclerInterface eventRecyclerViewInterface;
    Context context;
    ArrayList<UserEvent> userEvents;

    /**
     * Constructor for EventAdapter
     * @param context The context the recycler view is in
     * @param userEvents The ArrayList of UserEvent objects to be displayed in the recycler view
     * @param eventRecyclerViewInterface An interface for on item click purposes
     */
    public EventAdapter(Context context, ArrayList<UserEvent> userEvents, EventRecyclerInterface eventRecyclerViewInterface)
    {
        this.context = context;
        this.userEvents = userEvents;
        this.eventRecyclerViewInterface = eventRecyclerViewInterface;
    }

    /**
     * Creates the EventViewHolder that will hold event details
     * @param parent The ViewGroup the new View will be added to after its adapter position is set.
     * @param viewType The view type of the new View
     * @return A new EventViewHolder
     */
    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new EventAdapter.EventViewHolder(view, eventRecyclerViewInterface);
    }

    /**
     * Sets the values for the xml objects in the cardview from the recycler view ArrayList
     * @param holder The holder that the information will be bound to
     * @param position The position in the recyclerview that the message will appear
     */
    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        holder.eventTitle.setText(userEvents.get(position).GetTitle());
        holder.eventDate.setText(userEvents.get(position).GetDate());
        if(userEvents.get(position).isPast())
        {
            holder.eventImage.setImageResource(R.drawable.eventlistitembackgroundpast);
        }
        else if (userEvents.get(position).isToday())
        {
            holder.eventImage.setImageResource(R.drawable.eventlistitembackgroundtoday);
        }
        else
        {
            holder.eventImage.setImageResource(R.drawable.eventlistitembackgroundupcoming);
        }

    }

    /**
     * Gets the number of items in the ArrayList used to populate the recycler view
     * @return Number of items in the recycler view ArrayList
     */
    @Override
    public int getItemCount() {
        return userEvents.size();
    }

    /**
     * This class defines the xml objects in the card view so that we can change their values
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public ImageView eventImage;
        public TextView eventTitle;
        public TextView eventDate;
        View view;

        /**
         * This is a constructor for an EventViewHolder that is visually a rounded rectangle with event information
         * @param itemView The ViewGroup the new View will be added to after its adapter position is set.
         * @param eventRecyclerInterface An interface for on item click purposes
         */
        public EventViewHolder(View itemView, EventRecyclerInterface eventRecyclerInterface)
        {
            super(itemView);
            eventImage = (ImageView)itemView.findViewById(R.id.card_event_image);
            eventTitle = (TextView)itemView.findViewById(R.id.card_event_title);
            eventDate = (TextView)itemView.findViewById(R.id.card_event_date);
            view  = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * This function calls the onItemClick function and passes the index of the item that was click
                 * @param view the view that EventViewHolder is in
                 */
                @Override
                public void onClick(View view) {
                    if(eventRecyclerInterface != null)
                    {
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION)
                        {
                            eventRecyclerInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}