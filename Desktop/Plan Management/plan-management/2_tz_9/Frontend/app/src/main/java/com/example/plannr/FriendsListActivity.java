package com.example.plannr;

import static com.example.plannr.utils.Const.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Calendar;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.plannr.databinding.ActivityFriendslistBinding;
import com.example.plannr.utils.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * This activity is responsible for display a list of the users friends and allowing them to add and remove them.
 * @author Tasman Grinell
 */
public class FriendsListActivity extends AppCompatActivity {

    ActivityFriendslistBinding binding;
    View view;
    BottomNavigationView navBar;

    protected RequestQueue reqQueue;
    protected final String TAG = FriendsListActivity.class.getSimpleName();

//    private static WebSocketClient cc;

    protected Context jsonContext;  // Used as AlertDialog.Builder Context for

    //moved to god to make things hopefully easier
//    private ArrayList<Student> basicList = new ArrayList<Student>();
//
//    protected FriendsRecyclerAdapter listAdapter;

    private Student loggedInStudent;
    ImageView banner;

    /**
     * This function sets up the friends list activity by binding views, setting up the nav bar, initializing the adapter and recyclerview, and setting button functionality
     * @param savedInstanceState saved instance state of the activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                 // Calls super constructor
        god.userId = 1;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityFriendslistBinding.inflate(getLayoutInflater());
        // Associates Class with activity_main.xml file --Change to binding for nav bar
        setContentView(binding.getRoot());//R.layout.activity_main
        navBar = findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(R.id.Friends);
        navBar.setItemIconTintList(null);
        banner = (ImageView) findViewById(R.id.banner_friend);
        setUpBanner();
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
//        websocketInit();
        // Creating RecyclerView to use for friends list
        RecyclerView friendsList = (RecyclerView) findViewById(R.id.friendsListRecycler);
        // Setting LayoutManager with LinearLayout
        friendsList.setLayoutManager(new LinearLayoutManager(this));

        // Creating adapter and linking to RecyclerView
        god.listAdapter = new FriendsRecyclerAdapter(god.basicList, this);
        friendsList.setAdapter(god.listAdapter);

        // Add Button with Onclick for adding a friend
        FloatingActionButton addFriend = (FloatingActionButton) findViewById(R.id.addFriendFloating);

        /**
         * OnClickListener to create dialog prompt for adding friend
         * @param OnClickListener a new View.OnClickListener is created in the parameters
         */
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create Builder for AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(addFriend.getContext());

                builder.setTitle("Add Friend");

                builder.setCancelable(true);

                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View infView = inflater.inflate(R.layout.alertdialog_addfriend, null);
                builder.setView(infView);

                EditText emailText = infView.findViewById(R.id.friendEmail);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = emailText.getText().toString();

                        if (email.equals("")) {
                            email = "example@example.com";
                        }

                        // Try to post, printing Toast for response
                        reqQueue.add(new JsonObjectRequest(Method.POST, ADD_FRIEND_EMAIL + email, null,//sendObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, response.toString());
                                    initialJSONUpdate();
                                }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, error.toString());
                                        initialJSONUpdate();
                                    }
                                }));
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Must create to have context for builders in JSON requests
        jsonContext = this;

        reqQueue = Volley.newRequestQueue(getApplicationContext());

        // Get list of users from database
        initialJSONUpdate();
    }

    /**
     * Initial Update used when opening activity.
     * First testing with a singular object and creating an AlertDialog when there is either a response or error.
     */
    private void initialJSONUpdate() {

        god.basicList.clear();
        god.listAdapter.update(god.basicList);
        // Make Request for array object
        //if (god.userId != 0) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Method.GET, GET_FRIENDS, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());
                    Toast.makeText(jsonContext, "Successfully received!", Toast.LENGTH_LONG).show();
                    // Parse Response if gotten
                    jsonArrayParsing(response);
                    god.listAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Something went Wrong :( - Initial Update"); //error.getMessage()
                }
            });
            getRequestQueue().add(jsonArrayRequest);
        //}
    }


    /**
     * Testing Purposes for JSONObject request
     * @param jsonObject object for parse testing
     */
    private void jsonObjParsing(JSONObject jsonObject) {
        String firstName = "";
        String lastName = "";
        String name = "";
        String email = "";
        int id = 0;
        try {
            firstName = (String) jsonObject.get("firstName");
            lastName = (String) jsonObject.get("lastName");
            name = firstName + " " + lastName;
            email = (String) jsonObject.get("email");
            id = (int) jsonObject.get("id");
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        if (!name.equals("")) {
            god.basicList.add(new Student(name, email, id));
            god.listAdapter.update(god.basicList);
            god.listAdapter.notifyItemInserted(god.basicList.size());
        }
    }

    /**
     * Basic Parsing to add objects to ArrayList of friends
     * @param jsonObject JSON Array of objects to be parsed
     *
     */
    private void jsonArrayParsing(@NonNull JSONArray jsonObject) {
        String name = "";
        int id = -1;
        String email = "";
        for (int i = 0; i < jsonObject.length(); i++) {

            JSONObject elementOfArray = new JSONObject();

            try {
                elementOfArray = jsonObject.getJSONObject(i);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            jsonObjParsing(elementOfArray);
        }
    }

    /**
     * Used for instantiation/getting of Request Queue
     */
    protected RequestQueue getRequestQueue() {
        if (reqQueue == null) {
            reqQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return reqQueue;
    }

    /**
     * Nested FriendsRecyclerAdapter for use in RecyclerView. Helps manage items for the recyclerview
     */
    public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.BasicFriendView> {

        private ArrayList<Student> basicFriendList = new ArrayList<Student>();
        private Context context;

        /**
         * Constructor for Friends Adapter
         * @param context The context the recycler view is in
         * @param basicFriendList The ArrayList of Friend objects to be displayed in the recycler view
         */
        public FriendsRecyclerAdapter(ArrayList<Student> basicFriendList, Context context) {
            this.basicFriendList.clear();
            for (int i = 0; i < basicFriendList.size(); i++) {
                this.basicFriendList.add(basicFriendList.get(i));
//                if(this.basicFriendList.get(i).getOnline() == 1){
//                    view.findViewById(R.id.user_online).setVisibility(View.VISIBLE);
//                }
            }
            this.context = context;
            sortByPending(basicFriendList);
        }

        /**
         * onCreateViewHolder to inflate a unique friend_item layout for use in RecyclerView
         * Determines what each item in the List will be displayed with
         * @param parent The ViewGroup the new View will be added to after its adapter position is set.
         * @param viewType The view type of the new View
         * @return A new BasicFriendView
         */
        @NonNull
        @Override
        public FriendsRecyclerAdapter.BasicFriendView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);

            View friendsView = inflater.inflate(R.layout.friends_item, parent, false);

            FriendsRecyclerAdapter.BasicFriendView viewHolder = new BasicFriendView(friendsView);
            return viewHolder;
        }

        /**
         * Sets the values for the xml objects in the cardview from the recycler view ArrayList
         * @param holder The holder that the information will be bound to
         * @param position The position in the recyclerview that the message will appear
         */
        @Override
        public void onBindViewHolder(@NonNull FriendsRecyclerAdapter.BasicFriendView holder, int position) {
            TextView userText = holder.itemView.findViewById(R.id.userName);
            userText.setText(basicFriendList.get(position).getName());

            //stuff for online websockets and visibility
            ImageView status = holder.itemView.findViewById(R.id.user_online);
            if(god.basicList.get(position).getOnline() == 1){
                status.setVisibility(View.VISIBLE);
            } else {
                status.setVisibility(View.INVISIBLE);
            }

            ImageView avatar = holder.itemView.findViewById(R.id.userAvatar);
            avatar.setImageResource(R.drawable.defaultprofilepic);

            ImageButton confirmButton = (ImageButton) holder.itemView.findViewById(R.id.confirmFriend);
            ImageButton denyButton = (ImageButton) holder.itemView.findViewById(R.id.denyFriend);

            // Check to see if student is confirmed or not.
            if (basicFriendList.get(holder.getAdapterPosition()).isConfirmed() ) {
                confirmButton.setVisibility(View.INVISIBLE);
                denyButton.setVisibility(View.INVISIBLE);
            } else {

                Student student = basicFriendList.get(holder.getAdapterPosition());
                JSONObject friend = new JSONObject();
                try {
                    friend.put("name", student.getName());
                    friend.put("email", student.getEmail());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // Send reject/Confirm on button press
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Make Confirmation to backend passing the student
                        JsonObjectRequest confirmRequest = new JsonObjectRequest(Method.POST, CONFIRM_FRIEND, friend, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                basicFriendList.get(holder.getAdapterPosition()).setConfirmed(true);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, error.getMessage());
                            }
                        });
                        reqQueue.add(confirmRequest);
                    }
                });

                denyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Make Confirmation to backend passing the student
                        JsonObjectRequest denyRequest = new JsonObjectRequest(Method.POST, DENY_FRIEND, friend, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error");
                            }
                        });
                        reqQueue.add(denyRequest);
                    }
                });

            }

            // On Click Listener for Closer pop-up for Friend
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Student clickedStudent = basicFriendList.get(holder.getAdapterPosition());

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    LayoutInflater inf = LayoutInflater.from(v.getContext());
                    View onClickView = inf.inflate(R.layout.friend_onclick, null);

                    builder.setView(onClickView);

                    TextView friendName = onClickView.findViewById(R.id.nameText);
                    TextView friendEmail = onClickView.findViewById(R.id.emailText);
                    TextView friendNumber = onClickView.findViewById(R.id.numberText);

                    friendName.setText(clickedStudent.getName());
                    friendEmail.setText(clickedStudent.getEmail());
                    friendNumber.setText("Invalid Number");

                    builder.setTitle(clickedStudent.getName());
                    ImageView avatar = onClickView.findViewById(R.id.avatar);
                    avatar.setImageResource(R.drawable.defaultprofilepic);

                    ImageButton onClickEdit = (ImageButton) onClickView.findViewById(R.id.onclickEdit);

                    builder.setCancelable(true);

                    // Nested Edit Listener
                    onClickEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                            builder.setTitle("Edit Friend");

                            LayoutInflater inflater = LayoutInflater.from(view.getContext());
                            View editView = inflater.inflate(R.layout.editfriend, null);
                            builder.setView(editView);

                            EditText userText = (EditText) editView.findViewById(R.id.friendName);
                            userText.setText(clickedStudent.getName());

                            EditText emailText = (EditText) editView.findViewById(R.id.friendEmail);
                            emailText.setText(clickedStudent.getEmail());

                            // If positive, want to send PUT request to database
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    clickedStudent.setName(userText.getText().toString());
                                    clickedStudent.setEmail(emailText.getText().toString());

                                    long id = clickedStudent.getId();
                                    String name = clickedStudent.getName();
                                    String email = clickedStudent.getEmail();

                                    notifyItemChanged(holder.getAdapterPosition());

                                    JSONObject putObject = new JSONObject();
                                    try {
                                        putObject.put("name", name);
                                        putObject.put("email", email);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    // URL with unique fields at end of string for proper PUT parameters
                                    String url = JSON_SERVER_REQUEST + "/" + id + "?name=" + name + "&email=" + email;

                                    JsonObjectRequest putRequest = new JsonObjectRequest(Method.PUT, url, putObject,
                                            new Response.Listener() {
                                                @Override
                                                public void onResponse(Object response) {
                                                    Log.d(TAG, response.toString());
                                                    Toast.makeText(view.getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                                    notifyDataSetChanged();
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "Something Went Wrong :( - putRequest"/*error.getMessage()*/);
                                        }
                                    });

                                    userText.setText(name);
                                    emailText.setText(email);

                                    builder.setTitle(name);

                                    getRequestQueue().add(putRequest);
                                    dialogInterface.dismiss();
                                }
                            });

                            // Delete Button for deleting a friend off of a list.
                            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    AlertDialog.Builder confBuilder = new AlertDialog.Builder(view.getContext());

                                    confBuilder.setTitle("Confirm Deletion");

                                    LayoutInflater inflater = LayoutInflater.from(confBuilder.getContext());
                                    View deleteView = inflater.inflate(R.layout.alertdialog_confirmation, null);
                                    confBuilder.setView(deleteView);

                                    confBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            JSONObject deleteObj = new JSONObject();

                                            try {
                                                deleteObj.put("id", clickedStudent.getId());
                                                if (!deleteObj.get("id").equals("")) {

                                                    // Send Delete request with above URL
                                                    JsonObjectRequest deleteRequest = new JsonObjectRequest(Method.DELETE, DENY_FRIEND + clickedStudent.getId(), deleteObj,
                                                            new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            // Need to change response from backend to be able to do this
                                                            Log.d(TAG, response.toString());
                                                            basicFriendList.remove(clickedStudent);
                                                            Toast.makeText(deleteView.getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                                            notifyDataSetChanged();
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            if (error != null) {
                                                                Log.d(TAG, error.toString());
                                                            }
                                                            initialJSONUpdate();
                                                        }
                                                    });
                                                    reqQueue.add(deleteRequest);
                                                }
                                            } catch (Exception e) {
                                                Log.d(TAG, e.getMessage());
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                    confBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog dial = confBuilder.create();
                                    dial.show();
                                }
                            });

                            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });

                    AlertDialog dial = builder.create();
                    dial.show();
                }
            });
        }

        /**
         * Gets the number of items in the ArrayList used to populate the recycler view
         * @return Number of items in the recycler view ArrayList
         */
        @Override
        public int getItemCount() {
            return basicFriendList.size();
        }

        /**
         * update function to ensure accurate ArrayList objects
         */
        public void update(ArrayList<Student> updateList) {
            basicFriendList.clear();
            basicFriendList.addAll(updateList);
            sortByPending(basicFriendList);
        }

        private void sortByPending(ArrayList<Student> basicFriendList) {

        }

        /**
         * This class defines the xml objects in the card view so that we can change their values
         */
        public class BasicFriendView extends RecyclerView.ViewHolder {
            private TextView name;
            private ImageView avatar;

            private ImageView status;

            private View view;
            private ImageButton edit;

            /**
             * This is a constructor for an ChatViewHolder that is visually a chat bubble with text
             * @param itemView The ViewGroup the new View will be added to after its adapter position is set.
             */
            public BasicFriendView(@NonNull View itemView) {
                super(itemView);

                status = (ImageView) itemView.findViewById(R.id.user_online);
                name = (TextView) itemView.findViewById(R.id.userText);
                avatar = (ImageView) itemView.findViewById(R.id.userAvatar);
                avatar.setImageResource(R.drawable.defaultprofilepic);

                view = itemView;
            }
        }
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