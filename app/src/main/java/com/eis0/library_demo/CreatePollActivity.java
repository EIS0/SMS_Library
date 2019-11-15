package com.eis0.library_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

/**
 * Create Poll Activity view controller, it checks and sends back data insered from the user.
 * In the Create Poll Activity the user can insert all the informations for the creation of a
 * new poll (e.g. name, question, user1, ...).
 * @author Marco Cognolato, modified by Matteo Carnelos.
 */
class CreatePollActivity extends AppCompatActivity {

    static final String ARG_POLL_NAME = "poll_name";
    static final String ARG_POLL_QUESTION = "poll_question";
    static final String ARG_POLL_PEERS = "poll_peers";

    private EditText pollNameTxt;
    private EditText pollQuestionTxt;
    private EditText peer1Txt;
    private EditText peer2Txt;
    private EditText peer3Txt;

    /**
     * Called when the activity is being created.
     * Initializes and links all the UI elements.
     * @param savedInstanceState Instance saved from a previous activity destruction.
     * @autor Marco Cognolato, modified by Matteo Carnelos.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        pollNameTxt = findViewById(R.id.pollNameTxt);
        pollQuestionTxt = findViewById(R.id.pollQuestionTxt);
        peer1Txt = findViewById(R.id.peer1Txt);
        peer2Txt = findViewById(R.id.peer2Txt);
        peer3Txt = findViewById(R.id.peer3Txt);
    }

    /**
     * Function called when the "Send" button is clicked. It gets and checks data insered from the
     * user and sends them back to the starting activity (i.e. Main Activity).
     * @param view The view on which the onClick event is coming from.
     * @author Marco Cognolato, modified by Matteo Carnelos.
     */
    protected void sendPollOnClick(View view) {
        // Check if the name and/or the question is empty, in case show a Toast.
        String name = pollNameTxt.getText().toString();
        String question = pollQuestionTxt.getText().toString();
        if(name.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_name_message), Toast.LENGTH_SHORT).show();
            return;
        }
        if(question.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_question_message), Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Advanced telephone number checking using Android APIs

        // Add peers to process
        SMSPeer peer1 = new SMSPeer(peer1Txt.getText().toString());
        SMSPeer peer2 = new SMSPeer(peer2Txt.getText().toString());
        SMSPeer peer3 = new SMSPeer(peer3Txt.getText().toString());
        ArrayList<SMSPeer> peers = new ArrayList<>();
        if(peer1.isValid()) peers.add(peer1);
        if(peer2.isValid()) peers.add(peer2);
        if(peer3.isValid()) peers.add(peer3);

        // At least one Peer is required
        if(peers.isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_peers_message), Toast.LENGTH_SHORT).show();
            return;
        }

        // If I'm here I have at least one Peer, and they're all valid
        Intent returnIntent = new Intent()
                .putExtra(ARG_POLL_NAME, name)
                .putExtra(ARG_POLL_QUESTION, question)
                .putExtra(ARG_POLL_PEERS, peers);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}