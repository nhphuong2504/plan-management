package com.example.plannr.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannr.EventRecyclerInterface;
import com.example.plannr.R;

import java.util.ArrayList;

/**
 * This is an adapter for the recycler view that displays the chats a user is in
 * @author Spencer Thiele
 */
public class ChatSelectionAdapter extends RecyclerView.Adapter<ChatSelectionAdapter.ChatSelectViewHolder> {
    private final EventRecyclerInterface eventRecyclerViewInterface;
    Context context;
    ArrayList<Chat> chats;

    /**
     * Constructor for a ChatSelectionAdapter
     * @param context The context the recycler view is in
     * @param chats The ArrayList of ChatSingle objects to be displayed in the recycler view
     * @param eventRecyclerViewInterface An interface for on item click purposes
     */
    public ChatSelectionAdapter(Context context, ArrayList<Chat> chats, EventRecyclerInterface eventRecyclerViewInterface)
    {
        this.context = context;
        this.chats = chats;
        this.eventRecyclerViewInterface = eventRecyclerViewInterface;
    }

    /**
     * Creates the ChatSelectViewHolder that will hold a username
     * @param parent The ViewGroup the new View will be added to after its adapter position is set.
     * @param viewType The view type of the new View
     * @return A new ChatSelectViewHolder
     */
    @NonNull
    @Override
    public ChatSelectionAdapter.ChatSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chatselect_card, parent, false);
        return new ChatSelectionAdapter.ChatSelectViewHolder(view, eventRecyclerViewInterface);
    }

    /**
     * Sets the values for the xml objects in the cardview from the recycler view ArrayList
     * @param holder The holder that the information will be bound to
     * @param position The position in the recyclerview that the message will appear
     */
    @Override
    public void onBindViewHolder(@NonNull ChatSelectionAdapter.ChatSelectViewHolder holder, int position) {
        holder.userID.setText(chats.get(position).GetName());
    }

    /**
     * Gets the number of items in the ArrayList used to populate the recycler view
     * @return Number of items in the recycler view ArrayList
     */
    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ChatSelectViewHolder extends RecyclerView.ViewHolder {
        TextView userID;
        CardView Background;
        View view;

        /**
         * This is a constructor for an ChatSelectViewHolder that is visually a rectangle with the other users name in it
         * @param itemView The ViewGroup the new View will be added to after its adapter position is set.
         * @param eventRecyclerInterface An interface for on item click purposes
         */
        public ChatSelectViewHolder(View itemView, EventRecyclerInterface eventRecyclerInterface)
        {
            super(itemView);
            userID = (TextView)itemView.findViewById(R.id.chat_select_userid);
            Background = (CardView)itemView.findViewById(R.id.chat_select_background);
            view  = itemView;

            /**
             * This function calls the onItemClick function and passes the index of the item that was click
             * @param view the view that ChatSelectViewHolder is in
             */
            itemView.setOnClickListener(new View.OnClickListener() {
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
