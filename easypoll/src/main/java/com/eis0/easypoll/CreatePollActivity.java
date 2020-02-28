package com.eis0.easypoll;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eis0.easypoll.ui.PeersAdapter;
import com.eis0.smslibrary.SMSPeer;

/**
 * Create Poll Activity view controller, it checks and sends back data inserted from the user.
 * In the Create Poll Activity the user can insert all the information for the creation of a
 * new poll (e.g. name, question, user1, ...).
 *
 * @author Marco Cognolato
 * @author Matteo Carnelos
 */
public class CreatePollActivity extends AppCompatActivity {

    private static final int CONTACT_PICKER_REQUEST_CODE = 1;
    private static final int MAX_USERS_ALLOWED = 10;

    static final String ARG_POLL_NAME = "poll_name";
    static final String ARG_POLL_QUESTION = "poll_question";
    static final String ARG_POLL_PEERS = "poll_peers";

    private EditText pollNameTxt;
    private EditText pollQuestionTxt;
    private EditText peerTxt;
    private ImageButton addPeerBtn;
    private PeersAdapter peersAdapter;

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) addPeerBtn.setImageResource(R.drawable.ic_add_black_48dp);
            else addPeerBtn.setImageResource(R.drawable.ic_person_add_black_48dp);
        }
    };
    TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == 0 && event.getAction() == KeyEvent.ACTION_UP)
                addPeerBtn.callOnClick();
            return true;
        }
    };
    View.OnTouchListener fullAlphaTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.setAlpha((event.getAction() == MotionEvent.ACTION_DOWN)? 0.5f : 1f);
            // There is no need to override performClick() because the returned value is false,
            // meaning that the handling of the touch is delegated to the system
            return false;
        }
    };
    public static View.OnTouchListener halfAlphaTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.setAlpha((event.getAction() == MotionEvent.ACTION_DOWN)? 0.25f : 0.5f);
            // Same as above, there is no need to override performClick() because the returned
            // value is false, meaning that the handling of the touch is delegated to the system
            return false;
        }
    };

    /**
     * Called when the activity is being created.
     * Initialize and links all the UI elements.
     *
     * @param savedInstanceState Instance saved from a previous activity destruction.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        Slide slide = new Slide();
        slide.setPropagation(null);
        getWindow().setEnterTransition(slide);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create new poll");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pollNameTxt = findViewById(R.id.pollNameTxt);
        pollQuestionTxt = findViewById(R.id.pollQuestionTxt);
        peerTxt = findViewById(R.id.peerTxt);
        addPeerBtn = findViewById(R.id.addPeerBtn);
        TextView infoTxt = findViewById(R.id.infoTxt);

        peerTxt.setOnFocusChangeListener(focusChangeListener);
        peerTxt.setOnEditorActionListener(editorActionListener);
        addPeerBtn.setOnTouchListener(fullAlphaTouchListener);
        infoTxt.setOnTouchListener(halfAlphaTouchListener);

        RecyclerView peerList = findViewById(R.id.peersRclView);
        peerList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        peerList.setLayoutManager(layoutManager);
        peersAdapter = new PeersAdapter(infoTxt);
        peerList.setAdapter(peersAdapter);
    }

    /**
     * Called when the back button is pressed.
     *
     * @param item The {@link MenuItem} pressed.
     * @return True if the action as been handled, false otherwise.
     * @author Matteo Carnelos
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finishAfterTransition();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Function called when the add user button is clicked. It gets and checks data inserted from the
     * user and adds it in the peer recycler view.
     *
     * @param view The view on which the onClick event is coming from.
     * @author Matteo Carnelos
     */
    public void addPeerBtnOnClick(View view) {
        if(!peerTxt.hasFocus()) {
            openContactPicker(view);
            return;
        }
        if(peersAdapter.getItemCount() >= MAX_USERS_ALLOWED) {
            Toast.makeText(this, getString(R.string.limit_reached_message), Toast.LENGTH_SHORT).show();
            return;
        }
        String peerAddress = peerTxt.getText().toString();
        SMSPeer peer;
        try {
            peer = new SMSPeer(peerAddress);
            if(!peersAdapter.addPeer(peer))
                Toast.makeText(this, getString(R.string.duplicated_user_message), Toast.LENGTH_SHORT).show();
            peerTxt.setText("");
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(this, getString(R.string.invalid_peer_message), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Open the system contact picker.
     *
     * @author Matteo Carnelos
     */
    public void openContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK);
        contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_REQUEST_CODE);
    }

    /**
     * Called when the contact picker has finished.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult().
     * @param resultCode The integer result code returned by the child activity.
     * @param data An Intent, which can return result data to the caller.
     * @author Matteo Carnelos
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            if(contactUri == null) return;
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = this.getContentResolver().query(contactUri, projection,
                    null, null, null);
            if(cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                peerTxt.setText(number);
                cursor.close();
            }
            peerTxt.requestFocus();
            addPeerBtn.callOnClick();
            peerTxt.clearFocus();
        }
    }

    /**
     * Function called when the "Send" button is clicked. It gets and checks data inserted from the
     * user and sends them back to the starting activity (i.e. Main Activity).
     *
     * @param view The view on which the onClick event is coming from.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    public void sendPollOnClick(View view) {
        // Check if the name and/or the question is empty, in case show a Toast
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

        // At least one Peer is required
        if(peersAdapter.getPeersDataset().isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_peers_message), Toast.LENGTH_SHORT).show();
            return;
        }

        // If I'm here I have at least one Peer, and they're all valid
        Intent returnIntent = new Intent()
                .putExtra(ARG_POLL_NAME, name)
                .putExtra(ARG_POLL_QUESTION, question)
                .putExtra(ARG_POLL_PEERS, peersAdapter.getPeersDataset());
        setResult(Activity.RESULT_OK, returnIntent);
        finishAfterTransition();
    }
}
