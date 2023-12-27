package com.example.plannr;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.plannr.databinding.ActivityChatBinding;
import com.example.plannr.databinding.ActivityChatselectionBinding;
import com.example.plannr.databinding.ActivityMainBinding;
import com.example.plannr.utils.Chat;
import com.example.plannr.utils.ChatAdapter;
import com.example.plannr.utils.ChatSelectionAdapter;
import com.example.plannr.utils.ChatSingle;
import com.example.plannr.utils.Const;
import com.example.plannr.utils.EventAdapter;
import com.example.plannr.utils.UserEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This activity is one of the main 5 that can be navigated to through the nav bar. It displays a list of all the chats the user is in.
 * Each chat can be clicked on taking you to the ChatActivity with that chats data.
 * @author Spencer Thiele
 */
public class ChatSelectionActivity extends AppCompatActivity implements EventRecyclerInterface {
    ArrayList<Chat> chats = new ArrayList<>();
    ActivityChatselectionBinding binding;
    ChatSelectionAdapter adapter;
    View view;
    BottomNavigationView navBar;
    JSONObject eventsObject;
    protected RequestQueue reqQueue;
    ImageView banner;
    protected final String TAG = ChatActivity.class.getSimpleName();

    public static int addedChat = 0;

    /**
     * This onCreate prepares the view by binding, setting up navbar functionality, getting the chats a user is in from the server, and initializing the adapter and recycler view
     * @param savedInstanceState saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //god.userId = 3; //HERE FOR TESTING

        super.onCreate(savedInstanceState);
        binding = ActivityChatselectionBinding.inflate(getLayoutInflater());
        // Associates Class with activity_main.xml file --Change to binding for nav bar
        setContentView(binding.getRoot());//R.layout.activity_main

        //Animation Stuff
        banner = (ImageView) findViewById(R.id.banner);
        /*banner.setBackgroundResource(R.drawable.banner_afternoon_animation);
        AnimationDrawable bannerAnimate = (AnimationDrawable) banner.getBackground();
        bannerAnimate.start();*/
        //---------------

        navBar = findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(R.id.Chat);
        navBar.setItemIconTintList(null);
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
        Button createChatButton = findViewById(R.id.create_chat_button);
        EditText idEntry = findViewById(R.id.chatselect_id_input);
        RecyclerView recyclerView = findViewById(R.id.chatselection_recycler_list);
        eventsObject  = new JSONObject();
        reqQueue = Volley.newRequestQueue(getApplicationContext());
        setUpChatListServer();
        adapter = new ChatSelectionAdapter(this, chats, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(idEntry.getText().toString()).equals(""))
                {
                    addedChat++;
                    try {
                        int id = Integer.parseInt(idEntry.getText().toString());
                        createNewChat(id);
                        idEntry.setText("");
                        chats.clear();
                        setUpChatListServer();
                    }
                    catch (Exception e)
                    {
                        System.out.println("ID not a valid integer cast: " + e);
                    }

                }
            }
        });

        //god.setContext(getApplicationContext());
        setUpBanner();
    }

    /**
     * function used for testing recycler view without server requests.
     */
    public void setUpChatList()
    {
        chats.add(new Chat(1, "Zach"));
        chats.add(new Chat(2, "Tas"));
        chats.add(new Chat(3, "Phu"));
    }

    /**
     * This function makes a server request for a JSONArray with objects that contain chat ids and usernames of the user in a chat
     */
    public void setUpChatListServer()
    {
        System.out.println(Const.JSON_GET_CHATS_LIST + god.userId);
        JsonArrayRequest getChatListRequest = new JsonArrayRequest(Request.Method.GET, Const.JSON_GET_CHATS_LIST + god.userId, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObj = new JSONObject();
                int chatID;
                String user;

                for(int i = 0; i < response.length(); i++)
                {
                    try {
                        jsonObj = response.getJSONObject(i);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        chatID = (int)jsonObj.get("id");
                        JSONArray usersInChat = (JSONArray)jsonObj.get("usersInChat");
                        JSONObject userObj = new JSONObject();
                        if(usersInChat.length() == 2)
                        {
                            for(int j = 0; j < usersInChat.length(); j++)
                            {
                                userObj = usersInChat.getJSONObject(j);
                                if((int)userObj.get("id") != god.userId)
                                {
                                    user = (String)userObj.get("firstName") + " ";
                                    user += (String)userObj.get("lastName");
                                    chats.add(new Chat(chatID, user));
                                    break;
                                }
                            }
                        }
                        else
                        {
                            user = "";
                            for(int j = 0; j < usersInChat.length(); j++)
                            {
                                userObj = usersInChat.getJSONObject(j);
                                if((int)userObj.get("id") != god.userId)
                                {
                                    user += ((String)userObj.get("firstName")).substring(0,1);
                                    user += ((String)userObj.get("lastName")).substring(0,1) + " ";

                                }
                                if(user.length() >= 9)
                                {
                                    break;
                                }
                            }
                            user = user.substring(0, user.length()-1);
                            if(usersInChat.length() > 3)
                            {
                                user += "+";
                            }
                            chats.add(new Chat(chatID, user));
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.d(TAG, response.toString());
                adapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Something went wrong " + error.toString());
            }
        });

        reqQueue.add(getChatListRequest);
    }

    public void createNewChat(int memberID)
    {
        JsonObjectRequest createChatReq = new JsonObjectRequest(Request.Method.POST, Const.CREATE_CHAT + god.userId + "/member=" + memberID, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Something went wrong " + error.toString());
            }
        });
        reqQueue.add(createChatReq);
    }

    /**
     * This function is used by the navbar and it takes the user to the activity they click on the nav bar
     * @param view the view the user is in
     * @param activity the integer equivalent of the activity the user wants to go to
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

    /**
     * This function takes the user to the chat corresponding to the chat they clicked on. It sends some data to the next activity for server request purposes
     * @param position the index of the chat they clicked on in the recycler view. Auto passed by adapter
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ChatSelectionActivity.this, ChatActivity.class);
        intent.putExtra("CHATID", "" + chats.get(position).GetID());
        intent.putExtra("OTHERUSER", chats.get(position).GetName());
        startActivity(intent);
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
