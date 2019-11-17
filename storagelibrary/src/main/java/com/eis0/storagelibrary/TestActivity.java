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

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        String destination = "+393479281192";
        PollStoring storage = new PollStoring();

        //Creation of the three lists
        PollListStoring listStorage = new PollListStoring(this);
        String receivedListName = listStorage.receivedListName;


        //Searching in the list for one element named "Poll_2.json"
        boolean exist = listStorage.doesFileExist(this, "Poll_2.json");


        //Adding to the list
        listStorage.addToPollList(receivedListName, "Poll_2.json");
        String firstElementOfThePollList = listStorage.getFirstElement(listStorage.receivedListName);
        Log.d("Data_management_process", firstElementOfThePollList + " " + exist);

        /*Creating 2 polls
        TernaryPoll firstPoll = new TernaryPoll(new SMSPeer(destination));
        TernaryPoll secondPoll = new TernaryPoll(new SMSPeer(destination));
         */

        //Creating a custom fileName for the TernaryPoll object
        //String fileName = storage.setFileName(secondPoll);
        String fileName = "";   //Per correggere l'errore generato dal merge
        Log.d("Data_management_process", "File name: " + fileName);


        //Converting and saving the TernaryPoll object as a .json file
        //String jason = storage.convertToJson(secondPoll);
        String jason = "";
        storage.saveJsonToInternal(this, fileName, jason);

        //Loading and converting the Ternary object from .json format
        String jasonReturned = storage.loadJsonFromInternal(this, fileName);
        TernaryPoll restituito = storage.convertFromJson(jasonReturned);


        //Checking for the correct transfer of the file, visualizing a representative value of the object
        Log.d("Data_management_process", "Poll ID: " + restituito.getPollId());


        //Does the file Poll_2.json exist? Verify
        exist = listStorage.doesFileExist(this, fileName);
        Log.d("Data_management_process", fileName + " " + exist);

        //Save the current states
        listStorage.saveCurrentStatus(this);
    }
}
