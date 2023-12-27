package com.example.plannr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * the class is used for people to login in after the have booted up the app
 * @author Zach R
 */
public class LoginPageActivity extends AppCompatActivity {

    Button login, signup;
    EditText email, password;

    //String url = "https://7134f69b-64d8-44ef-8729-0a05374daa36.mock.pstmn.io/zach_123";
    String url = "http://coms-309-040.class.las.iastate.edu:8080/login";

//    String url = "http://10.0.2.2:8080/login";

    /**
     * called when teh activity starts up, connects teh xml file with teh java code
     * as well as links the buttons with their views
     * @param savedInstanceState teh previously saved data for the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //connects the buttons with the ui
        login = (Button) findViewById(R.id.login_button);
        signup = (Button) findViewById(R.id.sign_up);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        //sets up an onclick listener tof the login buttons
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creates a new JsonObject
                JSONObject login = new JSONObject();
                try{
                    //Fills the jsonObject
                    login.put("email", email.getText().toString());
                    login.put("password", password.getText().toString());
                } catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error filling object", Toast.LENGTH_SHORT).show();
                }
                //attempts to login
                login(login);
            }
        });

        // Set up a click listener for the signup button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //takes you to the registration page
                Intent createAccount = new Intent(LoginPageActivity.this, SignupActivity.class);
                startActivity(createAccount);
            }
        });
    }

    /**
     * makes a login request to the server on backend passing the object that contains the
     * users entered data
     * @param login The JSONObject containing the user's email and password.
     */
    private void login(JSONObject login){
        //makes a request queue
        RequestQueue queue = Volley.newRequestQueue(LoginPageActivity.this);

        //makes a new object request using the post method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, login, new Response.Listener<JSONObject>() {
            //parses through the response
            @Override
            public void onResponse(JSONObject response) {
                String message = "";
                try {
                    //gets the servers resposne to the login request
                    message = (String) ((JSONObject)response.get("alert")).get("message");
                    //should get the logged users id

                    //JSONObject user = (JSONObject)response.get("user");
                    god.userId = (int) ((JSONObject)response.get("user")).get("id");

                    Log.d("TAG", message + " " + god.userId);
                } catch (Exception e) {}

                //check to see if login is a success and outputs a corresponding message
                if(message.equals("success")){
                    Intent home = new Intent(LoginPageActivity.this, MainActivity.class);
                    startActivity(home);
                    Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                    //should make websocket connection work awesome
                    god.websocketInit();
                    god.password = password.getText().toString();
                    god.email = email.getText().toString();
                } else if (message.equals(("fail"))) {

                    Toast.makeText(getApplicationContext(), "Account has not confirmed", Toast.LENGTH_SHORT).show();
                } else {
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
