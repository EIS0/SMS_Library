/**
 * Debugging class, a showcase of the library methods
 * This is a demonstration of how the methods of the Storage Library should be used in the launcher
 * activity
 * <p>
 * Insert it in che onCreate method overrided in the launcher Activity
 *
 * @author Enrico Cestaro
 */

package com.eis0.storagelibrary;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.eis0.smslibrary.SMSPeer;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        /**TODO: Hi there, I'm not a TODO, I'm here to remind you that I'm here
         * P.S. I hope they don't get mad
         */
        String destination = "+393479281192";
        PollStoring storage = new PollStoring();

        //Creazione delle tre liste
        PollListStoring listStorage = new PollListStoring(this);
        String receivedListName = listStorage.receivedListName;


        //Verifico che nella lista vi sia un elemento "Poll_2.json"
        boolean exist = listStorage.doesFileExist(this, "Poll_2.json");


        //Aggiungo alla lista
        listStorage.addToPollList(receivedListName, "Poll_2.json");
        String firstElementOfThePollList = listStorage.getFirstElement(listStorage.receivedListName);
        Log.d("Data_management_process", firstElementOfThePollList + " " + exist);

        //Creo 2 poll
        TernaryPoll firstPoll = new TernaryPoll(new SMSPeer(destination));
        TernaryPoll secondPoll = new TernaryPoll(new SMSPeer(destination));


        //Creating a custom fileName for the TernaryPoll object
        String fileName = storage.setFileName(secondPoll);
        Log.d("Data_management_process", "File name: " + fileName);


        //Converting and saving the TernaryPoll object as a .json file
        String jason = storage.convertToJson(secondPoll);
        storage.saveJsonToInternal(this, fileName, jason);

        //Loading and converting the Ternary object from .json format
        String jasonReturned = storage.loadJsonFromInternal(this, fileName);
        TernaryPoll restituito = storage.convertFromJson(jasonReturned);


        //Checking for the correct transfer of the file, visualizing a representative value of the object
        Log.d("Data_management_process", "Poll ID: " + restituito.getPollId());


        //Does the file Poll_2.json exist?
        exist = listStorage.doesFileExist(this, fileName);
        Log.d("Data_management_process", fileName + " " + exist);

        //Save the current states
        listStorage.saveCurrentStatus(this);
    }
}
