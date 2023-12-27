package com.example.recitationpractice;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends ArrayAdapter<Friend> {

    protected ArrayList<Friend> friendArrayList;

    public FriendListAdapter(Context context, ArrayList<Friend> objects) {
        super(context, R.layout.friend_view, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Friend friend = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_view, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView num = (TextView) convertView.findViewById(R.id.number);
        ImageButton editButt = (ImageButton) convertView.findViewById(R.id.editButt);

        name.setText(friend.getName());
        num.setText(friend.getNumber());

        editButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.edit_butt);

                Button confirmButton = (Button) dialog.findViewById(R.id.confButt);
                ImageButton exitButton = (ImageButton) dialog.findViewById(R.id.cancel);

                TextView nameEdit = (TextView) dialog.findViewById(R.id.nameEdit);
                TextView numEdit = (TextView) dialog.findViewById(R.id.numEdit);

                nameEdit.setText(friend.getName());
                numEdit.setText(friend.getNumber());

                dialog.show();

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friend.setName(nameEdit.getText().toString());
                        friend.setNumber(numEdit.getText().toString());
                        dialog.dismiss();

                        notifyDataSetChanged();
                    }
                });

                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });



        convertView.refreshDrawableState();

        return convertView;

    }

}
