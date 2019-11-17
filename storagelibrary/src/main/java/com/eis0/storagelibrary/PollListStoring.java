package com.eis0.storagelibrary;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * This class allows the user to save, load and modify three different lists containing the names of
 * the file saved in the Internal Storage
 * The three lists differ only for their ultimate purpose
 *
 * @author Enrico Cestaro
 */

public class PollListStoring extends StoringJsons implements JsonConverter<ArrayList<String>> {

    //List for the sent polls, waiting to be closed/waiting for an answer
    public final String pendingListName = "pendingPollList.json";
    private static ArrayList<String> pendingPollList;

    //List for the closed polls
    public final String closedListName = "closedPollList.json";
    private static ArrayList<String> closedPollList;

    //List for the received polls, waiting to be closed
    public final String receivedListName = "receivedPollList.json";
    private static ArrayList<String> receivedPollList;


    /**
     * Initialize the three lists only if they don't already exist in the Internal Storage
     * If they do exist, they are loaded from the Internal Storage
     */
    public PollListStoring(Context context) {
        loadCurrentStatus(context);
    }


    /**
     * This method saves the three lists in the Internal Storage
     *
     * @param context
     */
    public void saveCurrentStatus(Context context) {
        savePollList(context, pendingListName, pendingPollList);
        savePollList(context, closedListName, closedPollList);
        savePollList(context, receivedListName, receivedPollList);
    }


    /**
     * Loads the content of the three ArrayLists containing the polls, from the Internal Storage
     *
     * @param context
     */
    public void loadCurrentStatus(Context context) {
        //Checking pending polls list
        if (!doesFileExist(context, pendingListName)) {
            pendingPollList = new ArrayList<>();
            savePollList(context, pendingListName, pendingPollList);
            Log.d("Data_management_process", "pendingPollList created");
        } else
            pendingPollList = loadPollList(context, pendingListName);
        //Checking closed polls list
        if (!doesFileExist(context, closedListName)) {
            closedPollList = new ArrayList<>();
            savePollList(context, closedListName, closedPollList);
            Log.d("Data_management_process", "closedPollList created");
        } else
            closedPollList = loadPollList(context, closedListName);
        //Checking received polls list
        if (!doesFileExist(context, receivedListName)) {
            receivedPollList = new ArrayList<>();
            savePollList(context, receivedListName, receivedPollList);
            Log.d("Data_management_process", "receivedPollList created");
        } else
            receivedPollList = loadPollList(context, receivedListName);
        Log.d("Data_management_process", "Lists loaded successfully");
    }

    /**
     * TODO: add the pollIdCounter to a file to save in the Internal Memory
     * Right now, it's only possible to modify it, it still get reset when the app is closed
     */
    private static int pollIdCounter;

    public void setPollIdCounter(int pollIdCounter) {
        this.pollIdCounter = pollIdCounter;
    }

    public int getPollIdCounter() {
        return pollIdCounter;
    }


    /**
     * This method converts an ArrayList<String> object into the corresponding .json file
     *
     * @param listOfFiles The ArrayList<String> object with the names of the files containing
     *                    the polls
     * @return returns a String value
     */
    public String convertToJson(ArrayList<String> listOfFiles) {
        Gson gson = new Gson();
        String json = gson.toJson(listOfFiles);
        Log.d("Data_management_process", json);
        return json;
    }

    /**
     * This method converts a .json file into the corresponding ArrayList<String> object
     *
     * @param json the String value of the .json file to convert
     * @return Returns an ArrayList<String> object
     */
    public ArrayList<String> convertFromJson(String json) {
        Gson gson = new Gson();
        ArrayList<String> listOfFiles = gson.fromJson(json, ArrayList.class);
        Log.d("Data_management_process", json);
        return listOfFiles;
    }

    /**
     * This method saves the specified ArrayList<String> object with the specified listName
     *
     * @param context
     * @param listName The name of the list of files to save in the Internal Storage
     * @param pollList The list to save in the Internal Storage
     */
    public void savePollList(Context context, String listName, ArrayList<String> pollList) {
        String json = this.convertToJson(pollList);
        saveJsonToInternal(context, listName, json);
    }

    /**
     * This method loads the ArrayList<String> object with the corresponding specified listName
     *
     * @param context
     * @param fileName The name of the ArrayList<String> object to load from the Internal Storage
     * @return
     */
    public ArrayList<String> loadPollList(Context context, String fileName) {
        String json = loadJsonFromInternal(context, fileName);
        return this.convertFromJson(json);
    }


    /**
     * This method removes from the specified list the String value equal to the fileName value inserted
     *
     * @param listName name of the list to modify
     * @param fileName String value to remove
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
        }
    }

    /**
     * This method adds to the specified list the String value equal to the fileName value inserted
     *
     * @param listName name of the list to modify
     * @param fileName String value to add
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
        }
    }

    /**
     * This method returns the first element of the ArrayList
     *
     * @param listName The name of the list
     * @return Returns the first String value of the ArrayList
     */
    public String getFirstElement(String listName) {
        switch (listName) {
            case pendingListName:
                return pendingPollList.get(0);
            case closedListName:
                return closedPollList.get(0);
            case receivedListName:
                return receivedPollList.get(0);
        }
        return listName;
    }

    /**
     * This method cleans the content of an ArrayList
     *
     * @param listName The name of the ArrayList
     */
    public void cleanPollList(String listName) {
        switch (listName) {
            case pendingListName:
                pendingPollList.clear();
            case closedListName:
                closedPollList.clear();
            case receivedListName:
                receivedPollList.clear();
        }
    }
}
