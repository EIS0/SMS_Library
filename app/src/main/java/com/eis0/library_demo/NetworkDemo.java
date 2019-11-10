package com.eis0.library_demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eis0.smslibrary.SMSPeer;
import com.example.webdictionary.NetworkConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NetworkDemo extends AppCompatActivity {


    private static final String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };
    private EditText t1;
    private List<SMSPeer> UserList = new ArrayList<>();
    private ListView AddressesList;
    ArrayAdapter<SMSPeer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_demo);
        Button btn = findViewById(R.id.button);
        t1 = findViewById(R.id.editText);
        AddressesList = findViewById(R.id.ListView);

        requestPermissions();
        Timer timer = new Timer();
        timer.schedule(new UpdateList(this), 0,500);
        //list containing user's number inside the network
        UserList = Arrays.asList(NetworkConnection.getInstance(this, null).getOnlinePeers());
        //adapter array that will provide AddressesList's information
        adapter = new ArrayAdapter<> ( this,android.R.layout.simple_list_item_1, UserList );
        AddressesList.setAdapter(adapter); //insert information to AddressesList
    }

    /**
     * Function called every 500ms to update the view
     */
    public void updateList(){
        UserList = Arrays.asList(NetworkConnection.getInstance(this, null).getOnlinePeers());
        Log.d("Demo", "updating list " + UserList.toString());
        adapter.notifyDataSetChanged();
    }

    /**
     * Class implementing a timer working on the same Thread of the UI
     */
    class UpdateList extends TimerTask {
        private NetworkDemo demo;

        public UpdateList(NetworkDemo demo){
            this.demo = demo;
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    demo.updateList();
                }
            });
        }
    };

    /**
     * Requests permissions for the library/app to work if not granted
     */
    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
    }

    /**
     * Function called when the button gets clicked:
     * Asks for an inserted valid number a request to join his network
     */
    public void buttonClick(View arg0){
        SMSPeer peer = new SMSPeer(t1.getText().toString());
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            phoneNumber = tMgr.getLine1Number();
            if(phoneNumber.contains("+1555521")){
                phoneNumber = phoneNumber.substring(phoneNumber.length() - 4);
            }
        }
        if(peer.isValid()){
            NetworkConnection.getInstance(this, new SMSPeer(phoneNumber)).askToJoin(peer);
        }
        else{
            Toast.makeText(getApplicationContext(),"Please insert a valid SMSPeer!",Toast.LENGTH_SHORT).show();
        }
    }
}
