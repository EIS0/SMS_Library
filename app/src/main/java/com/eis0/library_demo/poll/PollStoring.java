package com.eis0.library_demo.poll;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

/**
 * This class allows the User to:
 * => create a custom name for the file inside whom the Poll will be saved, based upon the Poll ID
 * => convert TernaryPoll Objects in .jar files, and vice versa*
 *
 * @author Enrico Cestaro
 */

public class PollStoring extends StoringJsons implements JsonConverter<TernaryPoll> {

    /**
     * This method makes use of the google library GSON to convert a TernaryPoll object in the
     * corresponding JSON String
     *
     * @param ternaryPoll the TernaryPoll object to convert
     * @return Returns a String containing the corresponding .jar value
     */
    public String convertToJson(TernaryPoll ternaryPoll) {
        Gson gson = new Gson();
        String json = gson.toJson(ternaryPoll);
        Log.d("Data_management_process", json);
        return json;
    }

    /**
     * This method makes use of the google library GSON to convert a String value (representing a
     * .json file) into a TernaryPoll object
     *
     * @param json The String value containing the .json format file
     * @return
     */
    public TernaryPoll convertFromJson(String json) {
        Gson gson = new Gson();
        TernaryPoll ternaryPollReturned = gson.fromJson(json, TernaryPoll.class);
        Log.d("Data_management_process", json);
        return ternaryPollReturned;
    }

    /**
     * This method allows the User to create custom names for the files which will be used to store
     * in the Internal Storage the content of the specified TernaryPoll objects
     *
     * @param poll Contains the TernaryPoll object of which must be created a name
     * @return Returns the name of the file inside to whom the corresponding poll is being saved
     * (in the .json format)
     */
    public String setFileName(TernaryPoll poll) {
        int pollID = poll.getPollId();
        return "Poll_" + pollID + ".json";
    }

    /**
     * This method saves the specified TernaryPoll object with the specified listName
     *
     * @param context
     * @param fileName The name of the list of files to save in the Internal Storage
     * @param poll The list to save in the Internal Storage
     */
    public void savePoll(Context context, String fileName, TernaryPoll poll) {
        String json = this.convertToJson(poll);
        saveJsonToInternal(context, fileName, json);
    }

    /**
     * This method loads the TernaryPoll object with the corresponding specified listName
     *
     * @param context
     * @param fileName The name of the ArrayList<String> object to load from the Internal Storage
     * @return
     */
    public TernaryPoll loadPoll(Context context, String fileName) {
        String json = loadJsonFromInternal(context, fileName);
        return this.convertFromJson(json);
    }

}