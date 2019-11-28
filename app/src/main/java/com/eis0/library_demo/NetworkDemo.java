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
import com.eis0.webdictionary.NetworkConnection;

import java.util.ArrayList;
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
    private ArrayList<String> userList;
    private ListView addressesList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_demo);
        Button btn = findViewById(R.id.button);
        t1 = findViewById(R.id.editText);
        addressesList = findViewById(R.id.ListView);

        requestPermissions();
        //list containing user's number inside the network
        userList = getPeersAsStrings();
        //adapter array that will provide addressesList's information
        adapter = new ArrayAdapter<> ( this,android.R.layout.simple_list_item_1, userList);
        addressesList.setAdapter(adapter); //insert information to addressesList
        Timer timer = new Timer();
        timer.schedule(new UpdateList(this), 0,500);
    }

    /**
     * Function called every 500ms to update the view
     */
    public void updateList(){
        userList = getPeersAsStrings();
        Log.d("Demo", "updating list " + userList.toString());
        adapter.clear();
        adapter.addAll(userList);
        adapter.notifyDataSetChanged();
    }

    /**
     * Returns online peers as an ArrayList of Strings
     */
    private ArrayList<String> getPeersAsStrings(){
        SMSPeer[] peers = NetworkConnection.getInstance(this, getPhoneNumber()).getOnlinePeers();
        ArrayList<String> peersAsStrings = new ArrayList<>();
        for(SMSPeer peer : peers){
            peersAsStrings.add(peer.getAddress());
        }
        return peersAsStrings;
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

        if(peer.isValid()){
            NetworkConnection.getInstance(this, getPhoneNumber()).askToJoin(peer);
        }
        else{
            Toast.makeText(getApplicationContext(),"Please insert a valid SMSPeer!",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns the telephone number of this phone as an SMSPeer
     */
    private SMSPeer getPhoneNumber(){
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            phoneNumber = tMgr.getLine1Number();
            if(phoneNumber.equals("+15555215554")){
                phoneNumber = "5556";
            }
            else if(phoneNumber.equals("+15555215556")){
                phoneNumber = "5554";
            }
            Log.d("NetDemo", "Found my Phone: " + phoneNumber);
        }
        return new SMSPeer(phoneNumber);
    }
}
