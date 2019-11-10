/**
 * Debugging class, a showcase of the library abilities
 *
 * @author Enrico Cestaro
 */

package com.eis0.storagelibrary;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SaveAndLoadPoll storage = new SaveAndLoadPoll();
        TernaryPoll added = new TernaryPoll();

        //Creating a custom fileName for the TernaryPoll object
        String fileName = storage.setFileName(added);
        Log.d("Data_management_process", "File name: " + fileName);

        //Converting the TernaryPoll object into a .json file
        String jason = storage.fromPollToJson(added);

        //Saving and loading from the Internal Storage
        storage.saveJsonToInternal(this, fileName, jason);
        String jasonReturned = storage.loadJsonFromInternal(this, fileName);

        //Converting the .json file into a TernaryPoll object
        TernaryPoll restituito = storage.fromJsonToPoll(jasonReturned);

        //Checking for the correct transfer of the file, visualizing a representative value of the object
        Log.d("Data_management_process", "Poll ID: " + restituito.pollId);

    }
}
