package com.example.plannr.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannr.EventRecyclerInterface;
import com.example.plannr.R;

import java.util.ArrayList;
/**
 * This is an adapter for the recycler view that displays the individual messages in a chat
 * @author Spencer Thiele
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final EventRecyclerInterface eventRecyclerViewInterface;
    Context context;
    ArrayList<ChatSingle> chats;
    ColorStateList leftBubbleColor;

    /**
     * Constructor for ChatAdapter
     * @param context The context the recycler view is in
     * @param chats The ArrayList of ChatSingle objects to be displayed in the recycler view
     * @param eventRecyclerViewInterface An interface for on item click purposes
     */
    public ChatAdapter(Context context, ArrayList<ChatSingle> chats, EventRecyclerInterface eventRecyclerViewInterface)
    {
        this.context = context;
        this.chats = chats;
        this.eventRecyclerViewInterface = eventRecyclerViewInterface;
    }

    /**
     * Creates the ChatViewHolder that will hold a message
     * @param parent The ViewGroup the new View will be added to after its adapter position is set.
     * @param viewType The view type of the new View
     * @return A new ChatViewHolder
     */
    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chat_card, parent, false); //ChatActivity.findViewById(R.id.outer_chat_card)
        return new ChatAdapter.ChatViewHolder(view, eventRecyclerViewInterface);
    }

    /**
     * Sets the values for the xml objects in the cardview from the recycler view ArrayList
     * @param holder The holder that the information will be bound to
     * @param position The position in the recyclerview that the message will appear
     */
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        leftBubbleColor = holder.chatBubbleLeft.getBackgroundTintList();
        holder.chatTextRight.setText(chats.get(position).GetText());
        holder.chatTextLeft.setText(chats.get(position).GetText());
        holder.senderName.setText(chats.get(position).GetSender());
        if(!chats.get(position).isSent())
        {
            holder.chatTextRight.setTextColor(ContextCompat.getColor(context, R.color.hidden));
            holder.chatBubbleRight.setVisibility(View.INVISIBLE);

            holder.chatTextLeft.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.chatBubbleLeft.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.chatTextLeft.setTextColor(ContextCompat.getColor(context, R.color.hidden));
            holder.chatBubbleLeft.setVisibility(View.INVISIBLE);
            holder.cardLayout.removeView(holder.senderName);

            holder.chatTextRight.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.chatBubbleRight.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Gets the number of items in the ArrayList used to populate the recycler view
     * @return Number of items in the recycler view ArrayList
     */
    @Override
    public int getItemCount() {
        return chats.size();
    }

    /**
     * This class defines the xml objects in the card view so that we can change their values
     */
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cardLayout;
        TextView chatTextRight;
        CardView chatBubbleRight;
        TextView chatTextLeft;
        CardView chatBubbleLeft;
        TextView senderName;
        View view;

        /**
         * This is a constructor for an ChatViewHolder that is visually a chat bubble with text
         * @param itemView The ViewGroup the new View will be added to after its adapter position is set.
         * @param eventRecyclerInterface An interface for on item click purposes
         */
        public ChatViewHolder(View itemView, EventRecyclerInterface eventRecyclerInterface)
        {
            super(itemView);
            cardLayout = (ConstraintLayout)itemView.findViewById(R.id.card_layout);
            chatTextRight = (TextView)itemView.findViewById(R.id.card_chat_text_right);
            chatBubbleRight = (CardView)itemView.findViewById(R.id.chat_bubble_right);
            chatTextLeft = (TextView)itemView.findViewById(R.id.card_chat_text_left);
            chatBubbleLeft = (CardView)itemView.findViewById(R.id.chat_bubble_left);
            senderName = (TextView)itemView.findViewById(R.id.name_of_sender);
            view  = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * This function calls the onItemClick function and passes the index of the item that was click
                 * @param view the view that ChatViewHolder is in
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
