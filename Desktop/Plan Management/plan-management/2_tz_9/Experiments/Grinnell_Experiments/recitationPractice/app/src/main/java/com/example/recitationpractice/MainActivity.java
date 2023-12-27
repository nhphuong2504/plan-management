package com.example.recitationpractice;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

// import com.example.recitationpractice.ui.Main2Activity;

public class MainActivity extends AppCompatActivity {

    private Button actSwitcher;
    private Button displayMessage;
    private Button switchSMS;
    private Button friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actSwitcher = (Button) findViewById(R.id.button);

        actSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SecondaryActivity.class);
                startActivity(intent);
            }
        });

        displayMessage = (Button) findViewById(R.id.button2);

        displayMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),
                        "Here is a Toast Message",
                        Toast.LENGTH_LONG).show();
            }
        });

        switchSMS = (Button) findViewById(R.id.button3);

        switchSMS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SMSActivity.class);
                startActivity(intent);
            }
        });

        friendsList = (Button) findViewById(R.id.friendsList);

        friendsList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FriendsListActivity.class);
                startActivity(intent);
            }
        });

        ImageButton menuNav = (ImageButton) findViewById(R.id.menu);


        menuNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.menu_frag);

                Button activity = (Button) findViewById(R.id.activity);
                Button message = (Button) findViewById(R.id.message);
                Button sms = (Button) findViewById(R.id.sendSMS);
                Button friendsList = (Button) findViewById(R.id.friendsList);

                /*
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.apply(Gravity.LEFT, 200, FILL_PARENT, Gravity.conta);
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;

                window.setAttributes(wlp);
                */

                dialog.show();

            }
        });


        Button menuActivity = (Button) findViewById(R.id.mainActTwo);

        menuActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MenuActivity.class);
                startActivity(intent);
            }
        });


        Button toCalendar = (Button) findViewById(R.id.calActivity);

        toCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        Button toExample = (Button) findViewById(R.id.examButt);

        toExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ExampleClass.class);
                startActivity(intent);
            }
        });


    }

}