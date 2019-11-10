package com.eis0.library_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.eis0.smslibrary.Peer;

import java.util.ArrayList;

public class NetworkDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_demo);
        Button btn = findViewById(R.id.button);
        EditText t1 = findViewById(R.id.editText);
        ListView AddressesList = findViewById(R.id.ListView);

        //list containing user's number inside the network
        final ArrayList<Peer> UserList = new ArrayList<Peer>();

        //adapter array that will provide AddressesList's information
        final ArrayAdapter<Peer> adapter = new ArrayAdapter<Peer> ( this,android.R.layout.simple_list_item_1, UserList );
        AddressesList.setAdapter(adapter); //insert information to AddressesList

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
               //TODO dictionary
            }
        });

    }
}
