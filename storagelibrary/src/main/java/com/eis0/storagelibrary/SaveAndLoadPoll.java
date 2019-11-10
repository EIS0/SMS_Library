package com.eis0.storagelibrary;

/*
This class allows the User to:
=> create a custom name for the file inside whom the Poll will be saved, based upon the Poll ID
=> convert TernaryPoll Objects in .jar files, and vice versa
=> save and load .jar files in and from the Internal Storage
*/

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

public class SaveAndLoadPoll implements PollStorage<TernaryPoll> {

    /**
     * This method makes use of the google library GSON to convert a TernaryPoll object in the
     * corresponding JSON String
     * @param ternaryPoll the TernaryPoll object to convert
     * @return Returns a String containing the corresponding .jar value
     */
    public String fromPollToJson(TernaryPoll ternaryPoll) {
        Gson gson = new Gson();
        ternaryPoll = new TernaryPoll();
        String json = gson.toJson(ternaryPoll);
        Log.d("Data_management_process", json);
        return json;
    }

    /**
     * This method makes use of the google library GSON to convert a String value (representing a
     * .json file) into a TernaryPoll object
     * @param json
     * @return
     */
    public TernaryPoll fromJsonToPoll(String json) {
        Gson gson = new Gson();
        TernaryPoll ternaryPollReturned = gson.fromJson(json, TernaryPoll.class);
        Log.d("Data_management_process", json);
        return ternaryPollReturned;
    }

    /**
     * This method allows the User to create custom names for the files which will be used to store
     * in the Internal Storage the content of the specified TernaryPoll objects
     * @param poll
     * @return the name of the file inside to whom the corresponding poll is being saved
     * (in the .json format)
     */
    public String setFileName(TernaryPoll poll) {
        int pollID = poll.pollId;
        return "Poll_" + pollID + ".json";
    }

    /**
     * This method converts and saves the content of the poll inside of the corresponding file
     * in the .json format
     * @param fileName
     * @param json
     */
    public void saveJsonToInternal(Context context, String fileName, String json) {
        FileOutputStream fileOutputStream = null;
        try {
            //Prints the String value inside of the designated file.
            fileOutputStream = context.openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            Log.d("Data_management_process", "Poll saved successfully");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method loads the content of the specified file as a String value
     * @param fileName
     * @return Returns the content of the selected file
     */
    public String loadJsonFromInternal(Context context, String fileName) {
        FileInputStream fileInputStream = null;
        String text = "";
        String json = "";
        try  {
            //Opens the file
            fileInputStream = context.openFileInput(fileName);
            //Converts the content of the file into a String value
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while((text = bufferedReader.readLine()) != null) {
                stringBuilder.append(text).append("\n");
            }

            json = stringBuilder.toString();
            Log.d("Data_management_process", "Poll loaded successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }
}