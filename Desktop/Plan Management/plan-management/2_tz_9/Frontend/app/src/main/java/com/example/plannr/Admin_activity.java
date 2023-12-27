package com.example.plannr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.plannr.utils.Const;

import org.json.JSONObject;

/**
 * activity used when you want to get you account admin privledges/rights on teh app
 * @author Zach R
 */
public class Admin_activity extends AppCompatActivity {

    EditText id;
    EditText password;

    /**
     * used link the java with teh xml code as well and connect some of the buttons, is called
     * at the start of the activity
     * @param savedInstanceState object passed to the onCreate method
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        id = findViewById(R.id.adminID);
        password = findViewById(R.id.adminPassword);
        god.setContext(getApplicationContext());
    }

    /**
     * method that takes the user back to hte home page assuming that eh
     * user was not able to get admin rights(did not have the admin password)
     * @param view view of the button clicked
     */
    public void returnAction(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * method that sends a request and recieves a response from backend to determine wehter teh
     * info entered was correct for admin privledges, if so gives them admin rights and then takes
     * them back to the home page
     * @param view view of the button clicked
     */
    public void verifyAction(View view){
        JSONObject admin = new JSONObject();
        try{
            //Fills the jsonObject
            admin.put("email", id.getText().toString());
            admin.put("password", password.getText().toString());
        } catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error filling object", Toast.LENGTH_SHORT).show();
        }
        //attempts to verify account
        verify(admin);
    }

    /**
     * volley post request method to send and get info back from backend to detmerine if credentials are correct
     * @param admin object containing the credentials entered
     */
    private void verify(JSONObject admin){
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(Admin_activity.this);

        //need to modify URL
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Const.charge1URL + id.getText().toString()+ "&password=" + password.getText().toString(), admin, new Response.Listener<JSONObject>() {
            //parses through the response
            @Override
            public void onResponse(JSONObject response) {
                String message = "";
                try {
                    //gets the servers resposne to the login request
                    message = (String) response.get("message");
                } catch (Exception e) {}

                //check to see if the user has been verified
                if(message.equals("Role changed successfully")){
                    god.admin = true;
                    Intent home = new Intent(Admin_activity.this, MainActivity.class);
                    startActivity(home);
                    Toast.makeText(getApplicationContext(), "Successfully paid $9", Toast.LENGTH_SHORT).show();
                } else {
                    god.admin = false;
                    Toast.makeText(getApplicationContext(), "Incorrect username/password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue
        queue.add(request);
    }
}
