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

    /**TODO Make it better
     *
     */
    private static int pollIdCounter;
    public void setPollIdCounter(int pollIdCounter) {
    this.pollIdCounter = pollIdCounter;
    }
    public int getPollIdCounter() {
        return pollIdCounter;
    }


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
     * @param pollList
     */
    public void savePollList(Context context, String listName, ArrayList<String> pollList) {
        String json = this.convertToJson(pollList);
        saveJsonToInternal(context, listName, json);
    }

    /**
     * @param context
     * @param fileName
     * @return
     */
    public ArrayList<String> loadPollList(Context context, String fileName) {
        String json = loadJsonFromInternal(context, fileName);
        return this.convertFromJson(json);
    }

    /**
     * @param listName
     * @param fileName
     */
    public void removeFromPollList(String listName, String fileName) {
        switch (listName) {
            case pendingListName:
                pendingPollList.remove(fileName);
                break;
            case closedListName:
                closedPollList.remove(fileName);
                break;
            case receivedListName:
                receivedPollList.remove(fileName);
                break;
            default:
                Log.d("Data_mamagement_process", "No list correspond to the one specified");
        }
    }

    /**
     * @param listName
     * @param fileName
     */
    public void addToPollList(String listName, String fileName) {
        switch (listName) {
            case pendingListName:
                pendingPollList.add(fileName);
                break;
            case closedListName:
                closedPollList.add(fileName);
                break;
            case receivedListName:
                receivedPollList.add(fileName);
                break;
            default:
                Log.d("Data_mamagement_process", "No list correspond to the one specified");
        }
    }
}
