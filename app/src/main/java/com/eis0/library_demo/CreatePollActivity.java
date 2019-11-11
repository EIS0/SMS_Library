package com.eis0.library_demo;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

public class CreatePollActivity extends AppCompatActivity {

    private EditText questionEditText;
    private EditText peerEditText1;
    private EditText peerEditText2;
    private EditText peerEditText3;
    private ArrayList<SMSPeer> peers = new ArrayList<>();
    private PollManager pollManager = PollManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        questionEditText = findViewById(R.id.questionText);
        peerEditText1 = findViewById(R.id.peerText1);
        peerEditText2 = findViewById(R.id.peerText2);
        peerEditText3 = findViewById(R.id.peerText3);
    }

    /**
     * Function called when the "Send" button to send a poll is clicked
     * @param view View of the Create Poll Activity
     */
    public void sendPollOnClick(View view) {
        //check if the question is empty
        String question = questionEditText.getText().toString();
        if(question.isEmpty()){
            Toast.makeText(this, getString(R.string.empty_to_field_message), Toast.LENGTH_SHORT).show();
            return;
        }

        //add peers to process
        peers.add(new SMSPeer(peerEditText1.getText().toString()));
        peers.add(new SMSPeer(peerEditText2.getText().toString()));
        peers.add(new SMSPeer(peerEditText3.getText().toString()));

        //at least one Peer is required
        removeEmptyPeers();
        if(peers.isEmpty()){
            Toast.makeText(this, "At least one Peer is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        //remove useless peers from the list
        //every Peer must be valid
        if(!isEachPeerValid()){
            Toast.makeText(this, "Every Peer must be valid!", Toast.LENGTH_SHORT).show();
            return;
        }
        //if I'm here I have at least one Peer, and they're all valid

        //create and send each SMS
        //for(SMSPeer peer : peers){
        //    SMSMessage sms = new SMSMessage(peer, question);
        //    SMSManager.getInstance(this).sendMessage(sms);
        //}

        String mPhoneNumber = "";
        try {
            TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getLine1Number();
        } catch(SecurityException e) {
            throw e;
        }

        pollManager.createPoll(question, new SMSPeer(mPhoneNumber), peers);
    }

    /**
     * Returns true if every Peer is valid, false otherwise
     */
    private boolean isEachPeerValid(){
        //assume every input is valid
        boolean isValid = true;
        //check if they're all valid
        for(SMSPeer peer : peers){
            if(!peer.isValid())
                isValid = false;
        }

        return isValid;
    }

    /**
     * Removes empty Peers from the peers list
     */
    private void removeEmptyPeers() {
        for(SMSPeer peer : peers){
            if(peer.isEmpty()){
                peers.remove(peer);
            }
        }
    }
}
