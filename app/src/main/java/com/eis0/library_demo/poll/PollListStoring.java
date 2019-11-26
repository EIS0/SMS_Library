package com.eis0.library_demo.poll;

import android.content.Context;
import android.content.SharedPreferences;
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
    public final String openedListName = "openedPollList.json";
    private static ArrayList<String> openedPollList;

    //List for the closed polls
    public final String closedListName = "closedPollList.json";
    private static ArrayList<String> closedPollList;

    //List for the received polls, waiting to be closed
    public final String incomingListName = "incomingPollList.json";
    private static ArrayList<String> incomingPollList;


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
        savePollList(context, openedListName, openedPollList);
        savePollList(context, closedListName, closedPollList);
        savePollList(context, incomingListName, incomingPollList);
    }


    /**
     * Loads the content of the three ArrayLists containing the polls, from the Internal Storage
     *
     * @param context
     */
    public void loadCurrentStatus(Context context) {
        //Checking pending polls list
        if (!doesFileExist(context, openedListName)) {
            openedPollList = new ArrayList<>();
            savePollList(context, openedListName, openedPollList);
            Log.d("Data_management_process", "pendingPollList created");
        } else
            openedPollList = loadPollList(context, openedListName);
        //Checking closed polls list
        if (!doesFileExist(context, closedListName)) {
            closedPollList = new ArrayList<>();
            savePollList(context, closedListName, closedPollList);
            Log.d("Data_management_process", "closedPollList created");
        } else
            closedPollList = loadPollList(context, closedListName);
        //Checking received polls list
        if (!doesFileExist(context, incomingListName)) {
            incomingPollList = new ArrayList<>();
            savePollList(context, incomingListName, incomingPollList);
            Log.d("Data_management_process", "receivedPollList created");
        } else
            incomingPollList = loadPollList(context, incomingListName);
        Log.d("Data_management_process", "Lists loaded successfully");
    }

    /**
     * This method saves in the SharedPreferences the pollIdCounter, making it possible to load it
     * later.
     *
     * @param context       Context in which the method is used
     * @param pollIdCounter
     */
    public void setPollIdCounter(Context context, int pollIdCounter) {
        SharedPreferences settings = context.getSharedPreferences("PollID", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("PollID", pollIdCounter);
    }

    /**
     * This method loads from the SharedPreferences the pollIdCounter, needed to create, save and
     * load polls from and to the Internal Storage
     *
     * @param context Context in which the method is used
     * @return
     */
    public int getPollIdCounter(Context context) {
        SharedPreferences settings = context.getSharedPreferences("PollID", 0);
        int pollID = settings.getInt("PollID", 0);
        return pollID;
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
            case openedListName:
                openedPollList.remove(fileName);
                break;
            case closedListName:
                closedPollList.remove(fileName);
                break;
            case incomingListName:
                incomingPollList.remove(fileName);
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
            case openedListName:
                openedPollList.add(fileName);
                break;
            case closedListName:
                closedPollList.add(fileName);
                break;
            case incomingListName:
                incomingPollList.add(fileName);
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
            case openedListName:
                return openedPollList.get(0);
            case closedListName:
                return closedPollList.get(0);
            case incomingListName:
                return incomingPollList.get(0);
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
            case openedListName:
                openedPollList.clear();
            case closedListName:
                closedPollList.clear();
            case incomingListName:
                incomingPollList.clear();
        }
    }

    /**
     * This method returns the poll list with the specified listName, if the list doesn't correspond
     * to one of the three inside of this class, the method returns null. It only works with:
     * - openedListName
     * - closedListName
     * - incomingListName
     * @param listName The name of the list
     * @return {@link ArrayList} with the poll list specified
     */
    public ArrayList<String> getPollList(String listName) {
        switch (listName) {
            case openedListName:
                return openedPollList;
            case closedListName:
                return closedPollList;
            case incomingListName:
                return incomingPollList;
        }
        return null;
    }
}
