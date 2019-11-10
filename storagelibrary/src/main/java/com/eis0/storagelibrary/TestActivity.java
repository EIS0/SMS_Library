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
        TernaryPoll inserito = new TernaryPoll();
        String fileName= storage.setFileName(inserito);
        Log.d("Data_management_process", "File name: " + fileName);
        String jason = storage.fromPollToJson(inserito);
        storage.saveJsonToInternal(this, fileName, jason);
        String jasonReturned = storage.loadJsonFromInternal(this, fileName);
        TernaryPoll restituito = storage.fromJsonToPoll(jasonReturned);
        Log.d("Data_management_process", "Poll ID: " + restituito.pollId);

    }
}
