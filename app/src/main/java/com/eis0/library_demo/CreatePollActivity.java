package com.eis0.library_demo;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

public class CreatePollActivity extends AppCompatActivity {

    private EditText pollQuestionTxt;
    private EditText peer1Txt;
    private EditText peer2Txt;
    private EditText peer3Txt;
    private ArrayList<SMSPeer> peers = new ArrayList<>();
    private PollManager pollManager = PollManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        pollQuestionTxt = findViewById(R.id.pollQuestionTxt);
        peer1Txt = findViewById(R.id.peer1Txt);
        peer2Txt = findViewById(R.id.peer2Txt);
        peer3Txt = findViewById(R.id.peer3Txt);
    }

    /**
     * Function called when the "Send" button is clicked
     * @param view View of the Create Poll Activity
     */
    public void sendPollOnClick(View view) {
        // Check if the question is empty
        String question = pollQuestionTxt.getText().toString();
        if(question.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_question_message), Toast.LENGTH_SHORT).show();
            return;
        }

        // Add peers to process
        SMSPeer peer1 = new SMSPeer(peer1Txt.getText().toString());
        SMSPeer peer2 = new SMSPeer(peer2Txt.getText().toString());
        SMSPeer peer3 = new SMSPeer(peer3Txt.getText().toString());
        if(peer1.isValid()) peers.add(peer1);
        if(peer2.isValid()) peers.add(peer2);
        if(peer3.isValid()) peers.add(peer3);

        // At least one Peer is required
        if(peers.isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_peers_message), Toast.LENGTH_SHORT).show();
            return;
        }

        // If I'm here I have at least one Peer, and they're all valid

        pollManager.createPoll(question, peers);
    }
}
