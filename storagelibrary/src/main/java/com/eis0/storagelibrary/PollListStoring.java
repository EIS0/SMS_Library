package com.eis0.storagelibrary;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

public class PollListStoring extends StoringJsons implements JsonConverter<ArrayList<String>> {

    public final String pendingListName = "pendingPollList.json";
    private ArrayList<String> pendingPollList;

    public final String closedListName = "closedPollList.json";
    private ArrayList<String> closedPollList;

    public final String receivedListName = "receivedPollList.json";
    private ArrayList<String> receivedPollList;


    public String convertToJson(ArrayList<String> listOfFiles) {
        Gson gson = new Gson();
        String json = gson.toJson(listOfFiles);
        Log.d("Data_management_process", json);
        return json;
    }

    public ArrayList<String> convertFromJson(String json) {
        Gson gson = new Gson();
        ArrayList<String> listOfFiles = gson.fromJson(json, ArrayList.class);
        Log.d("Data_management_process", json);
        return listOfFiles;
    }

    public void savePollList(Context context, String listName, String json) {
        saveJsonToInternal(context, listName, json);
    }

    public String loadPollList(Context context, String fileName) {
        return loadJsonFromInternal(context, fileName);
    }

    public void removeFromPollList(String fileName, String listName) {
        switch(listName) {
            case pendingListName:
                break;
            case closedListName:
                break;
            case receivedListName:
                break;
            default: Log.d("Data_mamagement_process","No list correspond to the one specified");
        }

    }

    public void addToPollList(String fileName, String listName) {
        switch (listName) {
            case pendingListName:
                break;
            case closedListName:
                break;
            case receivedListName:
                break;
            default:
                Log.d("Data_mamagement_process", "No list correspond to the one specified");
        }
    }


}
