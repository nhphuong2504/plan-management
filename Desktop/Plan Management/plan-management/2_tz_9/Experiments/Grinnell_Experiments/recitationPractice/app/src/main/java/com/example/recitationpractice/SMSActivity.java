package com.example.recitationpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SMSActivity extends AppCompatActivity {

    private ImageButton backButt;
    private TextView sms;
    private EditText messageBox;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.sms_layout);

        backButt = (ImageButton) findViewById(R.id.imageButton2);

        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        sms = (TextView) findViewById(R.id.textView2);

        messageBox = (android.widget.EditText) findViewById(R.id.messageBox);

    }
}
