package com.example.plannr;
import  static java.lang.Integer.max;
import static java.lang.Integer.min;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.Preference;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.plannr.utils.Const;
import com.example.plannr.utils.EventAdapter;
import com.example.plannr.utils.Preferences;
//import com.example.plannr.utils.EventAdapter;
import com.example.plannr.utils.Student;
import com.example.plannr.utils.UserEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Map;

import java.util.Collections;


/**
 * this class is used a class to contain static variable to pass between activities and update
 * as methods run
 * @author Zach R
 */
public class god {
    public static boolean chosen;

    public static Preferences prefs;

    public static String email;
    public static String userName;
    public static String dateOfBirth;

    public static String password;

    public static UserEvent selectedEvent;

    public static int source;

    public static boolean editing;

    public static boolean setUP = false;

    public static int userId = 0;

    public static int tag = 3;

    public static ArrayList<UserEvent> top = new ArrayList<>();

    //determines wether the events page displays your personal event list of the public event list
    public static int personal = 0;

    //user email1
    //psw:iwanttobeanadmin123
    public static boolean admin = false;

    public static Student student;

    public static ArrayList<UserEvent> events = new ArrayList<UserEvent>();

    public static ArrayList<UserEvent> allEvents = new ArrayList<UserEvent>();

    public static ArrayList<UserEvent> createdByMe = new ArrayList<UserEvent>();

    private static Context god;

    //brough over from friends list to make things easier i hope
    public static ArrayList<Student> basicList = new ArrayList<Student>();

    public static FriendsListActivity.FriendsRecyclerAdapter listAdapter;

    //websockets
    private static WebSocketClient cc;
    ArrayList<Student> chats = new ArrayList<>();

    static String m ="";

    /*
     * Check with zach to make sure this is fine.
     */
    public static void websocketInit()
    {
        Draft[] drafts = {
                new Draft_6455()
        };
//        String w = "ws://coms-309-040.class.las.iastate.edu:8080/websocket/" + "chat/" + getIntent().getStringExtra("CHATID") + "/user/" + god.userId;
        //get url from backend

        String w = "ws://coms-309-040.class.las.iastate.edu:8080/websocket/login/" + userId;

        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(w), (Draft) drafts[0]) {

                public void onMessage(JSONObject response) throws JSONException {
                    String temp = (String) response.get("sender");
                    Log.d("MESSAGE", "help");
                    String user = (String) response.get("sender");
                    String display;
                    System.out.println("RESPONSE: " + response.get("sender"));
                    for(int i = 0; i < basicList.size(); i++){
                        if(user.equals(basicList.get(i).getName())){
                            if(response.get("type").equals("ONLINE")){
                                display = user + "is now online";
                            } else{
                                display = user + "is now offline";
                            }
                            basicList.get(i).updateStatus();
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                }

                @Override
                public void onMessage(String s) {
                    Log.d("MESSAGE", s);
                    m = "";
                    for(int i = 0; i < basicList.size(); i++){
                        if(s.equals(basicList.get(i).getName())){
                            if(basicList.get(i).getOnline() == 0){
                                m  = s + " is now online";
                            } else{
                                m  = s + " is now offline";
                            }
                            basicList.get(i).updateStatus();
                        }
                    }

                    new Thread(new Runnable() {
                        public void run() {
                            // Call Looper.prepare() to prepare the thread for message handling
                            Looper.prepare();

                            // Show the toast message
                            Toast.makeText(god, m, Toast.LENGTH_SHORT).show();

                            // Call Looper.loop() to start the message loop for this thread
                            Looper.loop();
                        }
                    }).start();

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
    }


//    public static void setUpUserEvents()
//    {
//        if(setUP == false) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                allEvents.add(new UserEvent("Team Meeting", "meet with team", LocalDate.now(), LocalTime.now().plusHours(1), 1));
//                allEvents.add(new UserEvent("Run", "go for a run", LocalDate.now().plusDays(1), LocalTime.now(), 2));
//                allEvents.add(new UserEvent("FLY", "get on a plane", LocalDate.now().plusDays(1), LocalTime.now().minusHours(6), 1));
//                allEvents.add(new UserEvent("Skip", "sleep in", LocalDate.now().plusDays(2), LocalTime.now().minusHours(4), 2));
//                allEvents.add(new UserEvent("See", "get glasses", LocalDate.now().plusDays(2), LocalTime.now().minusHours(8), 2));
//                allEvents.add(new UserEvent("help", "cry", LocalDate.now().minusDays(1), LocalTime.now().minusHours(10), 2));
//                allEvents.add(new UserEvent("Cry", "ask people for guidance", LocalDate.now().plusDays(3), LocalTime.now().minusHours(5), 1));
//                events.add(new UserEvent("Team Meeting", "meet with team", LocalDate.now(), LocalTime.now().plusHours(1), 1));
//                events.add(new UserEvent("Run", "go for a run", LocalDate.now().plusDays(1), LocalTime.now(), 2));
//                events.add(new UserEvent("FLY", "get on a plane", LocalDate.now().plusDays(1), LocalTime.now().minusHours(6), 1));
//                events.add(new UserEvent("Skip", "sleep in", LocalDate.now().plusDays(2), LocalTime.now().minusHours(4), 2));
//                events.add(new UserEvent("See", "get glasses", LocalDate.now().plusDays(2), LocalTime.now().minusHours(8), 2));
//
//            }
//            setUP = true;
//        }
//    }
    public static void setContext(Context context){
        god = context;
    }

    //method to sort and get the top 10 events
    public static void poll(){
        ArrayList<UserEvent> temp = new ArrayList<>();
        for(int i = 0; i < allEvents.size(); i ++){
            temp.add(allEvents.get(i));
        }
        for(int i = 0; i < temp.size() - 1; i++){
            int min_idx = i;
            for(int j = i + 1; j < temp.size(); j++){
                if(temp.get(j).getSize() > temp.get(min_idx).getSize()){
                    min_idx = j;
                }
            }
            Collections.swap(temp, i, min_idx);
        }
        top.clear();
        for(int i = 0; i < min(10, temp.size()); i++){
            top.add(temp.get(i));
        }
    }


    /**
     * this method gets all the created events from the server in backend and stores them in an array
     * @param god the context variable for the class
     */

    public static void allEvents(Context god){
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(god);

        allEvents.clear();

        //makes a new object request using the post method
        JsonArrayRequest request = new JsonArrayRequest(Const.allEvents , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    //gets the servers resposne to the login request
                    for(int i = 0; i < response.length(); i++){
                        JSONObject obj = (JSONObject) response.get(i);
//                        allEvents.add(obj);
                        int size = response.length();
                        String date = (String) obj.get("date");
                        String time = (String) obj.get("time");
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDate l = LocalDate.parse(date);
                            LocalTime d = LocalTime.parse(time);
                            if (obj.get("owner") != null) {
                                JSONObject owner = (JSONObject) obj.get("owner");
                                JSONArray attends = (JSONArray) obj.get("participatingUsers");
                                allEvents.add(new UserEvent((String) obj.get("title"), (String) obj.get("description"), l, d, (int) owner.get("id"),(int) obj.get("tag"), (String) obj.get("recurrenceType"), (int) obj.get("recurrenceCount"), (String) obj.get("location"),(int) obj.get("id"), attends.length()));
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage() + "help");
                }
                Toast.makeText(god, "Successfully loaded events  :-)", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(god, "Error Loading Events ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue
        queue.add(request);
    }

    /**
     * this method gets the events that the user is part of from the backend server and stores them
     * in an array list
     * @param god the context variable for the class
     */
    public static void Events(Context god){
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(god);

        events.clear();

        //makes a new object request using the post method
        JsonArrayRequest request = new JsonArrayRequest(Const.personalEvents, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    //gets the servers resposne to the login request
                    for(int i = 0; i < response.length(); i++){
                        JSONObject obj = (JSONObject) response.get(i);
                        String date = (String) obj.get("date");
                        String time = (String) obj.get("time");
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDate l = LocalDate.parse(date);
                            LocalTime d = LocalTime.parse(time);
                            if (obj.get("owner") != null) {
                                JSONObject owner = (JSONObject) obj.get("owner");
                                events.add(new UserEvent((String) obj.get("title"), (String) obj.get("description"), l, d, (int) owner.get("id"), (int) obj.get("tag"), (String) obj.get("recurrenceType"), (int) obj.get("recurrenceCount"), (String) obj.get("location"),(int) obj.get("id")));
                            }
                        }
                    }
                    //Toast.makeText(god, "Successfully loaded events  :-)", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    System.out.println(e);
                }

//                Toast.makeText(god, "Successfully loaded events  :-)", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(god, "Error Loading Events ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue
        queue.add(request);
    }

    /**
     * this method gets all the events that the user has created an stores them in backend
     * @param god the context variable for the class
     */
    public static void createdByMe(Context god){
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(god);

        createdByMe.clear();

        //makes a new object request using the post method
        JsonArrayRequest request = new JsonArrayRequest(Const.myEvents, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    //gets the servers resposne to the login request
                    for(int i = 0; i < response.length(); i++){
                        JSONObject obj = (JSONObject) response.get(i);
                        String date = (String) obj.get("date");
                        String time = (String) obj.get("time");
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDate l = LocalDate.parse(date);
                            LocalTime d = LocalTime.parse(time);
                            if (obj.get("owner") != null) {
                                JSONObject owner = (JSONObject) obj.get("owner");
                                createdByMe.add(new UserEvent((String) obj.get("title"), (String) obj.get("description"), l, d, (int) owner.get("id"), (int) obj.get("tag"), (String) obj.get("recurrenceType"), (int) obj.get("recurrenceCount"), (String) obj.get("location"),(int) obj.get("id")));
                            }
                        }
                    }
                } catch (Exception e) {}

                //Toast.makeText(god, "Successfully loaded events  :-)", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(god, "Error Loading Events ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue
        queue.add(request);
    }

    /**
     * this method sends the newly created event in frontend to backend where it can be stored and
     * made visible to the other users.
     * @param god the context variable for the class
     * @param event the event that you want to send to backend
     */
    public static void addEvent(Context god, UserEvent event){
        JSONObject newEvent = new JSONObject();
        try{
            //Fills the jsonObject
            newEvent.put("title", event.GetTitle());
            newEvent.put("description", event.GetDescription());
            newEvent.put("date", event.getDateR());
            newEvent.put("time", event.getTimeR());
            newEvent.put("userID", userId);
            newEvent.put("tag", event.getTag());
            newEvent.put("recurrenceType", event.getFrequency());
            newEvent.put("recurrenceCount", event.getRepetitions());
            newEvent.put("location", event.getAddress());
        } catch(Exception e){
            Toast.makeText(god, "Error filling object", Toast.LENGTH_SHORT).show();
        }
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(god);
        //makes a new object request using the post method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Const.addEvent, newEvent, new Response.Listener<JSONObject>() {
            //parses through the response
            @Override
            public void onResponse(JSONObject response) {
                String message = "";
                try {
                    //gets the servers response to the login request
                    message = (String) response.get("message");
                } catch (Exception e) {}

                //check to see if login is a success and outputs a corresponding message
                if(message.equals("Post Successful")){
                    Toast.makeText(god, "Event Created", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(god, "Failed to create event", Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue
        queue.add(request);
    }

    /**
     * this method deletes an event from the server in backend, removing it from the
     * list of all events that are displayed to other users
     * @param god the context variable for the class
     * @param event the event that you want to delete from backend
     */
    public static void deleteEvent(Context god, UserEvent event){
        JSONObject newEvent = new JSONObject();
        try{
            //Fills the jsonObject
            newEvent.put("eventID", event.getEventID());
        } catch(Exception e){
            Toast.makeText(god, "Error filling object", Toast.LENGTH_SHORT).show();
        }
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(god);
        //makes a new object request using the post method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, Const.deleteEvent  + selectedEvent.eventID + "/user=" + userId, newEvent, new Response.Listener<JSONObject>() {
            //parses through the response
            @Override
            public void onResponse(JSONObject response) {
                String message = "";
                try {
                    //gets the servers resposne to the login request
                    message = (String) response.get("message");
                } catch (Exception e) {}

                //check to see if login is a success and outputs a corresponding message
                if(message.equals("Delete Successful")){
                    if (createdByMe.contains(event)) {
                        createdByMe.remove(event);
                    }

                    if (allEvents.contains(event)) {
                        allEvents.remove(event);
                    }

                    if (events.contains(event)) {
                        events.remove(event);
                    }
                    Toast.makeText(god, "Event deleted", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(god, "Failed to delete event", Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue
        queue.add(request);
    }

    /**
     * this methods add teh specified event the users list of joined events on backend
     * @param god the context variable for the class
     * @param event the event that you want to join
     */
    public static void joinEvent(Context god, UserEvent event){
        JSONObject newEvent = new JSONObject();
        try{
            //Fills the jsonObject
            newEvent.put("eventID", event.getEventID());
            newEvent.put("userID", userId);
        } catch(Exception e){
            Toast.makeText(god, "Error filling object", Toast.LENGTH_SHORT).show();
        }
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(god);
        //makes a new object request using the post method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Const.joinEvent + selectedEvent.eventID + "/user=" + userId, newEvent, new Response.Listener<JSONObject>() {
            //parses through the response
            @Override
            public void onResponse(JSONObject response) {
                String message = "";
                try {
                    //gets the servers resposne to the login request
                    message = (String) response.get("message");
                } catch (Exception e) {}

                //check to see if login is a success and outputs a corresponding message
                if(message.equals("success")){
                    //Toast.makeText(god, "Event joined", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(god, "Failed to join event", Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue
        queue.add(request);
    }

    /**
     * this methods removes the event from teh users list of joined events
     * @param god the context variable for the class
     * @param event the event that you want to leave
     */
    public static void leaveEvent(Context god, UserEvent event){
        JSONObject newEvent = new JSONObject();
        try{
            //Fills the jsonObject
            newEvent.put("eventID", event.getEventID());
            newEvent.put("userID", userId);
        } catch(Exception e){
            Toast.makeText(god, "Error filling object", Toast.LENGTH_SHORT).show();
        }
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(god);
        //makes a new object request using the post method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, Const.leaveEvent + selectedEvent.eventID + "/user=" + userId, newEvent, new Response.Listener<JSONObject>() {
            //parses through the response
            @Override
            public void onResponse(JSONObject response) {
                String message = "";
                try {
                    //gets the servers resposne to the login request
                    message = (String) response.get("message");
                } catch (Exception e) {}

                //check to see if login is a success and outputs a corresponding message
                if(message.equals("success")){
                    //Toast.makeText(god, "Event left", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(god, "Failed to leave event", Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue
        queue.add(request);
    }

    public static void editEvent(Context god, UserEvent event){
        JSONObject newEvent = new JSONObject();
        try{
            //Fills the jsonObject
            newEvent.put("title", event.GetTitle());
            newEvent.put("description", event.GetDescription());
            newEvent.put("date", event.getDateR());
            newEvent.put("time", event.getTimeR());
            newEvent.put("userID", userId);
            newEvent.put("tag", tag);
            newEvent.put("recurrenceType", event.getFrequency());
            newEvent.put("recurrenceCount", event.getRepetitions());
            newEvent.put("location", event.getAddress());
        } catch(Exception e){
            Toast.makeText(god, "Error filling object", Toast.LENGTH_SHORT).show();
        }
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(god);
        //makes a new object request using the post method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, Const.editEvent + userId + "/event=" + event.GetTitle(), newEvent, new Response.Listener<JSONObject>() {
            //parses through the response

            @Override
            public void onResponse(JSONObject response) {
                String message = "";
                try {
                    //gets the servers resposne to the login request
                    message = (String) response.get("message");
                } catch (Exception e) {}

                //check to see if login is a success and outputs a corresponding message
                if(message.equals("Put Successful")){
                    Toast.makeText(god, "Event updated", Toast.LENGTH_SHORT).show();
                }else{
                    System.out.println(message);
                    Toast.makeText(god, "shucks", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(god, "Failed to edit event", Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue
        queue.add(request);
    }
}
