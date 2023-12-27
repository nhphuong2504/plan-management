package com.example.recitationpractice;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;


public class FriendsListActivity extends AppCompatActivity {

    private ListView friends;
    protected FriendListAdapter generalListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_layout);

        ImageButton menuBack = (ImageButton) findViewById(R.id.backMenuFriendsList);

        menuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton addFriend = (ImageButton) findViewById(R.id.addFriendButt);

        friends = (ListView) findViewById(R.id.friendsListView);

        ArrayList<Friend> friendArrayList = new ArrayList<Friend>();

        generalListAdapter = new FriendListAdapter(friends.getContext(), friendArrayList);
        friends.setAdapter(generalListAdapter);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.add_friend);


                EditText nameText = (EditText) dialog.findViewById(R.id.editFriendName);
                EditText numText = (EditText) dialog.findViewById(R.id.enterNumber);

                ImageButton cancelButt =  (ImageButton) dialog.findViewById(R.id.cancel);
                Button addFriendDialog = (Button) dialog.findViewById(R.id.addFriend);

                dialog.show();

                //ImageButton cancelButt = (ImageButton) findViewById(R.id.cancel);
                cancelButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                addFriendDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friendArrayList.add(new Friend(nameText.getText().toString(), numText.getText().toString()));
                        generalListAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });


            }
        });

    }

    protected FriendListAdapter getListAdapter() {
        return generalListAdapter;
    }

}
