package com.example.plannr;

import static com.example.plannr.utils.Const.JSON_POST_CHAT;
import static com.example.plannr.utils.Const.JSON_SERVER_REQUEST;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import com.example.plannr.databinding.ActivityMainBinding;
import com.example.plannr.utils.ChatAdapter;
import com.example.plannr.utils.ChatSingle;
import com.example.plannr.utils.EventAdapter;
import com.example.plannr.utils.UserEvent;
import com.example.plannr.utils.Const;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

/**
 * This activity corresponds to the view where you see messages from a specific chat. It can only navigate back to the ChatSelectionActivity.
 */
public class ChatActivity extends AppCompatActivity implements EventRecyclerInterface {


    private WebSocketClient cc;
    String chatId;
    ArrayList<ChatSingle> chats = new ArrayList<>();
    RecyclerView recyclerView;
    ActivityChatBinding binding;
    View view;
    TextView otherUserName;
    ChatAdapter adapter;
    JSONObject eventsObject;
    protected RequestQueue reqQueue;
    protected final String TAG = ChatActivity.class.getSimpleName();
    EditText chatInput;

    Button sendButton;
    Button refreshButton;
    Button backButton;

    /**
     * This function sets up the view for use. It binds views, assigns xml objects, initializes adapters, and opens a connection with the server for the chat.
     * @param savedInstanceState saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //god.userId = 3;
        if(getIntent().getStringExtra("CHATID") == null)
        {
            chatId = "102";
        }
        else
        {
            chatId = getIntent().getStringExtra("CHATID");
        }


        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        // Associates Class with activity_main.xml file --Change to binding for nav bar
        setContentView(binding.getRoot());//R.layout.activity_main
        chatInput = findViewById(R.id.chat_input);
        sendButton = findViewById(R.id.send_button);
        refreshButton = findViewById(R.id.chat_refresh_button);
        backButton = findViewById(R.id.chat_back_button);
        otherUserName = findViewById(R.id.other_user_text);
        otherUserName.setText(getIntent().getStringExtra("OTHERUSER"));
        recyclerView = findViewById(R.id.chat_recycler_list);
        eventsObject  = new JSONObject();
        reqQueue = Volley.newRequestQueue(getApplicationContext());
        //setUpChats();
        //getChatsFromServer();
        adapter = new ChatAdapter(this, chats, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        websocketInit();
        god.setContext(getApplicationContext());


        /**
         * Creates a listener for the send button click. Calls sendChat on click
         * @param OnClickListener a new View.OnClickListener is created in the parameters
         */
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(chatInput.getText().toString()).equals(""))
                {
                    sendChatWS();
                }
            }
        });

        /**
         * Creates a listener for the back button click. Returns to ChatSelectionActivity if clicked
         * @param OnClickListener a new View.OnClickListener is created in the parameters
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ChatSelectionActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Creates a listener for the refresh button click. Will be removed once websockets are fully implemented
         * @param OnClickListener a new View.OnClickListener is created in the parameters
         */
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
    }

    /**
     * Populates the chats ArrayList for recycler view testing purposes when the server is unavailable
     */
    public void setUpChats()
    {
        //chats.add(new ChatSingle("First chat"));
        //chats.add(new ChatSingle("Longer chat that will need to be resized with some layout modifications"));
        //chats.add(new ChatSingle("Another mid sized text for reference", false));
        //chats.add(new ChatSingle());
        //chats.add(new ChatSingle("Hey man what's going on", false));
        //chats.add(new ChatSingle("Nothing much I've just got a demo tomorrow and I hope it goes well"));
        //chats.add(new ChatSingle("Well I hope you have enough major contributions bro because otherwise you don't stand a chance", false));
        //chats.add(new ChatSingle("Yeah I should be good by then but I'm gonna have to grind"));
        //chats.add(new ChatSingle("Well stop messaging me and get to work then brooooooooo", false));
    }

    /**
     * Creates and queues a server request for a JSONARRAY with all of the chats and then parses the data and populates ArrayList chats with it
     */
    public void getChatsFromServer()
    {
        JsonArrayRequest getChatLogRequest = new JsonArrayRequest(Request.Method.GET, Const.JSON_GET_CHAT_LOG + chatId, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObj = new JSONObject();
                String chat;
                String user;

                for(int i = 0; i < response.length(); i++)
                {
                    try {
                        jsonObj = response.getJSONObject(i);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        chat = (String)jsonObj.get("message");
                        JSONObject userFromChat = (JSONObject) jsonObj.get("user");
                        int userId = (int)(userFromChat.get("id"));
                        if(userId != god.userId)
                        {
                            //chats.add(new ChatSingle(chat, false));
                        }
                        else
                        {
                            //chats.add(new ChatSingle(chat, true));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.d(TAG, response.toString());
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Something went wrong " + error.toString());
            }
        });

        reqQueue.add(getChatLogRequest);

    }


    public void sendChatWS()
    {
        if(!chatInput.getText().toString().equals(""))
        {
            if(cc == null)
            {
                websocketInit();
            }
            cc.send(chatInput.getText().toString());
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            chatInput.setText("");
        }
    }


    public void sendChat()
    {
        JSONObject sendObj = new JSONObject();
        try {
            sendObj.put("message", chatInput.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest sendNewChat = new JsonObjectRequest(Request.Method.POST, JSON_POST_CHAT + getIntent().getStringExtra("CHATID") + "/user=" + god.userId, sendObj, //still need to add user
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        String messageFromBackend = "";
                        try {
                            messageFromBackend = (String) response.get("message");
                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Something went wrong");
            }
        });

        reqQueue.add(sendNewChat);
        //chats.add(new ChatSingle(chatInput.getText().toString(), true));
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        chatInput.setText("");
    }

    /**
     * Clears the chats ArrayList and repopulates it with the getChatsFromServer function
     */
    public void refresh()
    {
        for(int i = 0; i < chats.size(); i++)
        {
            System.out.println("Refresh: " + chats.get(i).GetText());
        }
        adapter.notifyDataSetChanged();
        //adapter.notifyItemInserted(chats.size() - 1);
    }

    /**
     * Runs when an item in the recycler view is clicked. Currently unimplemented
     * @param position this parameter is passed to the function by the adapter and corresponds to the index of the item in the recycler view
     */
    @Override
    public void onItemClick(int position) {

    }

    public void websocketInit()
    {
        Draft[] drafts = {
                new Draft_6455()
        };
        String w = "ws://coms-309-040.class.las.iastate.edu:8080/websocket/" + "chat/" + getIntent().getStringExtra("CHATID") + "/user/" + god.userId;

        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(w), (Draft) drafts[0]) {

                @Override
                public void onMessage(String response){
                    System.out.println("RESPONSE: " + response);
                    JSONArray messages = new JSONArray();
                    try {
                        messages = new JSONArray(response);
                        System.out.println(messages);
                    } catch (JSONException e) {
                        System.out.println("Array Invalid");
                        throw new RuntimeException(e);
                    }

                    chats.clear();
                    JSONObject message = new JSONObject();
                    String chat = "";
                    String sender = "";
                    int userId = -1;

                    System.out.println("JSONARRAY" + messages);
                    System.out.println("LENGTH: " + messages.length());
                    for(int i = 0; i < messages.length(); i++)
                    {
                        try {
                            message = messages.getJSONObject(i);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            chat = (String)message.get("message");
                            userId = (int)(message.get("sendingID"));
                            sender = (String)message.get("userSending");
                            //System.out.println("CHAT: " + userId + chat);
                        } catch (Exception e) {

                            System.out.println("EXCEPTION HERE");
                            Log.d(TAG, e.getMessage());
                            System.out.println("Message Invalid");
                        }
                        //System.out.println("Deez Message:" + message);
                        if(userId != god.userId)
                        {
                            chats.add(new ChatSingle(chat, false, sender));
                        }
                        else
                        {
                            chats.add(new ChatSingle(chat, true, sender));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                    //System.out.println(handshake.field);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                }
            };
        } catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage().toString());
            e.printStackTrace();
        }
        cc.connect();
        System.out.println("CONNECTED ________________---------");
    }
}

