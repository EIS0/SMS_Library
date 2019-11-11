/**
 * This class allows the user to save, load and modify three different lists containing TernaryPoll
 * objects
 *
 * @author Enrico Cestaro
 */

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

    /**
     * @param listOfFiles
     * @return
     */
    public String convertToJson(ArrayList<String> listOfFiles) {
        Gson gson = new Gson();
        String json = gson.toJson(listOfFiles);
        Log.d("Data_management_process", json);
        return json;
    }

    /**
     * @param json
     * @return
     */
    public ArrayList<String> convertFromJson(String json) {
        Gson gson = new Gson();
        ArrayList<String> listOfFiles = gson.fromJson(json, ArrayList.class);
        Log.d("Data_management_process", json);
        return listOfFiles;
    }

    /**
     * @param context
     * @param listName
     * @param json
     */
    public void savePollList(Context context, String listName, String json) {
        saveJsonToInternal(context, listName, json);
    }

    /**
     * @param context
     * @param fileName
     * @return
     */
    public String loadPollList(Context context, String fileName) {
        return loadJsonFromInternal(context, fileName);
    }

    /**
     * @param fileName
     * @param listName
     */
    public void removeFromPollList(String fileName, String listName) {
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

    /**
     * @param fileName
     * @param listName
     */
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
