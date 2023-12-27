package com.example.plannr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * this activity is used for new users to register their data with teh app and to create an account
 * @author Zach R
 */
public class SignupActivity extends AppCompatActivity {

    Button create_account;

    EditText first, last, pswd, repswd, email;

    private String url = "http://coms-309-040.class.las.iastate.edu:8080/registration";

    /**
     * this method connects teh xml with the java code as well as initialize the data inputs in teh
     * xml code. It also establishes on click listeners for when teh user submits their data
     * @param savedInstanceState saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //connects teh button with the ui
        create_account = (Button) findViewById(R.id.create_account);
        first = (EditText) findViewById(R.id.firstName);
        last = (EditText) findViewById(R.id.LastName);
        email = (EditText) findViewById(R.id.email);
        pswd = (EditText) findViewById(R.id.password);
        repswd = (EditText) findViewById(R.id.retypePassword);


        create_account.setOnClickListener(new View.OnClickListener() {
            /**
             * method puts the entered data into an object which is to be sent to backend for
             * the account to be created
             * @param view view of the button clicked
             */
            @Override
            public void onClick(View view) {
                if(pswd.getText().toString().equals(repswd.getText().toString())){

                    //creates a user object on signup
                    JSONObject user = new JSONObject();
                    //Insert data into the user object
                    try {
                        user.put("password", pswd.getText().toString());
                        user.put("firstName", first.getText().toString());
                        user.put("lastName", last.getText().toString());
                        user.put("email", email.getText().toString());
//                        user.put("Level", "admin");
                    } catch(Exception e){
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                    postRequest(user);
                }else{
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * post request method to send data to backend and get a response back letting us know if teh user has successfully created an account
     * @param user object contained teh users data to be sent to backend
     */
    private void postRequest(JSONObject user){
        //makes a request queue
        RequestQueue queue =  Volley.newRequestQueue(SignupActivity.this);
        //makes a new post request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String message = "";
                        try {
                            message = (String) response.get("message");
                        } catch (Exception e) {}
                        //prints the servers response to the sign up request
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(SignupActivity.this, LoginPageActivity.class);
                        startActivity(home);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        //add teh request to the queue
        queue.add(request);
    }


}