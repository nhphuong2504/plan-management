package com.example.plannr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plannr.utils.Preferences;
import com.example.plannr.utils.Student;
/**
 * This activity displays the users profile information
 * @author Tasman Grinell
 */
public class ProfileActivity extends AppCompatActivity {

    Student loggedInStudent;

    /**
     * This onCreate prepares the view by binding, setting up buttons, and plugging information into xml objects
     * @param savedInstanceState saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        godInit();

        ImageButton mainMenuButton = (ImageButton) findViewById(R.id.mainActivity);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsActivity);
        settingsButton.setBackgroundTintList(null);

        /**
         * Creates a listener for the main menu button click. Returns to mainActivity if clicked
         * @param OnClickListener a new View.OnClickListener is created in the parameters
         */
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                putStudentIntent(intent);

                startActivity(intent);
            }
        });

        /**
         * Creates a listener for the settings button click. Takes user to settings view if clicked
         * @param OnClickListener a new View.OnClickListener is created in the parameters
         */
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        TextView name = (TextView) findViewById(R.id.username);
        name.setText(god.userName);

        TextView dob = (TextView) findViewById(R.id.dateOfBirth);
        dob.setText(god.dateOfBirth);

        TextView email = (TextView) findViewById(R.id.email);
        email.setText(god.email);
        if(!nullField(loggedInStudent.getEmail())) {
            email.setText(loggedInStudent.getEmail());
        } else {
            email.setText("null");
        }
        god.setContext(getApplicationContext());
    }

    /**
     * Sets intent information with user info so that it can be accessed in the next activity loaded
     * @param intent the intent to be loaded next
     */
    private void putStudentIntent(Intent intent) {
        intent.putExtra("name", loggedInStudent.getName());
        intent.putExtra("email", god.email);
        intent.putExtra("id", loggedInStudent.getId());
        intent.putExtra("dob", "");
        intent.putExtra("age", loggedInStudent.getAge());
    }

    /**
     * Handles null string functionality
     * @param str the string being checked
     */
    private boolean nullField(String str) {
        return str.equals("");
    }

    /**
     * Handles null integer functionality
     * @param field the integer being checked
     */
    private boolean nullField(int field) {
        return field == -1;
    }

    public void godInit()
    {
        god.userId = 1;
        god.userName = "zach rapoza";
        god.email = "zrapoza@iastate.edu";
        god.dateOfBirth = "5/1/2023";
        if(god.prefs == null)
        {
            god.prefs = new Preferences(false, false, false);
        }
    }


}
