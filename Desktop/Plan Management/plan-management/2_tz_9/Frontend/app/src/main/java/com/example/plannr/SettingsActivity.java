package com.example.plannr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.plannr.utils.Chat;
import com.example.plannr.utils.Const;
import com.example.plannr.utils.Music;
import com.example.plannr.utils.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This activity displays user preferences and allows the user to change them.
 * @author Tasman Grinell and Spencer Thiele
 */
public class SettingsActivity extends AppCompatActivity {

    Student loggedInStudent;

    Spinner music_dropdown;

    Spinner visual_dropdown;
    ArrayAdapter<CharSequence> adapter;

    public ArrayList<String> musicOptions;
    public ArrayList<Integer> musicIDs;
    public ArrayList<String> visualOptions;

    JSONObject eventsObject;
    protected RequestQueue reqQueue;
    protected final String TAG = ChatActivity.class.getSimpleName();

    public Switch showOn;
    public Switch showMail;
    public Switch notify;

    /**
     * This function sets up the view for use. It binds views, assigns xml objects, and sets up buttons
     * @param savedInstanceState saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        reqQueue = Volley.newRequestQueue(SettingsActivity.this);
        music_dropdown = findViewById(R.id.music_dropdown);
        visual_dropdown = findViewById(R.id.visual_dropdown);
        musicOptions = new ArrayList<String>();
        musicIDs = new ArrayList<Integer>();
        ImageButton toMenu = (ImageButton) findViewById(R.id.navMenu);
        notify = findViewById(R.id.switch1);
        showMail = findViewById(R.id.switch2);
        showOn = findViewById(R.id.switch3);
        notify.setChecked(god.prefs.notifications);
        showMail.setChecked(god.prefs.showEmail);
        showOn.setChecked(god.prefs.showOnline);

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                god.prefs.notifications = !god.prefs.notifications;
            }
        });

        showMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                god.prefs.showEmail = !god.prefs.showEmail;
            }
        });

        showOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                god.prefs.showOnline = !god.prefs.showOnline;
            }
        });

        /**
         * This sets up a listener for the dropdown menu
         * @param OnItemSelectedListener is instantiated in the parameters
         */
        music_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            /**
             * This function runs when an item is selected. It changes the users preference with a server put request and reloads the music accordingly.
             * @param parent ArrayAdapter for the drop down menu
             * @param view The view the drop down is in
             * @param position The index of the item selected in the dropdown menu array
             * @param id The id of the dropdown menu xml object
             */
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                JsonArrayRequest setMusicPreference = new JsonArrayRequest(Request.Method.PUT, Const.SET_MUSIC_PREFERENCE + god.userId + "/music=" + musicIDs.get(position), null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObj = new JSONObject();
                        for(int i = 0; i < response.length(); i++)
                        {
                            try {
                                jsonObj = response.getJSONObject(i);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Log.d(TAG, response.toString());
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Something went wrong " + error.toString());
                    }
                });

                reqQueue.add(setMusicPreference);
                //Music.changeMusic();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        /**
         * Creates a listener for the menu button click. Loads menu view on click
         * @param OnClickListener a new View.OnClickListener is created in the parameters
         */
        toMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                Student exampleStudent = new Student(1, "ExampleStudent", "example@example", null, null);
                putStudentIntent(intent, exampleStudent);
                startActivity(intent);
            }
        });
        setUpMusic();
        getMusicOptions();
        god.setContext(getApplicationContext());
    }

    /**
     * Sets intent information with user info so that it can be accessed in the next activity loaded
     * @param intent the intent to be loaded next
     */
    private void putStudentIntent(Intent intent, Student student) {
        intent.putExtra("name", student.getName());
        intent.putExtra("email", student.getEmail());
        intent.putExtra("id", student.getId());
        intent.putExtra("dob", "");
        intent.putExtra("age", "");
    }

    /**
     * Sets up the dropdown xml object by initializing and setting its adapter
     */
    public void setUpMusic()
    {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        music_dropdown.setAdapter(adapter);
    }

    /**
     * This function makes a server request for the music options on the server. It the populates musicOptions with the options returned
     */
    public void getMusicOptions()
    {
        JsonArrayRequest getMusicRequest = new JsonArrayRequest(Request.Method.GET, Const.GET_MUSIC_OPTIONS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObj = new JSONObject();

                for(int i = 0; i < response.length(); i++)
                {
                    try {
                        jsonObj = response.getJSONObject(i);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        musicOptions.add((String)jsonObj.get("title"));
                        musicIDs.add((int)jsonObj.get("id"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(":::::" + musicOptions.get(0));
                for(int i = 0; i < musicOptions.size(); i++)
                {
                    adapter.add(musicOptions.get(i));
                }
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Something went wrong " + error.toString());
            }
        });

        reqQueue.add(getMusicRequest);
    }

}
