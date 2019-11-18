package com.eis0.storagelibrary;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

/**
 * This interface allows the user to save, load and delete files to and from the Internal Storage
 *
 * @author Enrico Cestaro
 */

public class StoringJsons implements InternalMemoryStorage {
    /**
     * This method converts and saves the content of the poll inside of the corresponding file
     * in the .json format
     *
     * @param fileName Name of the file to save in the Internal Storage
     * @param json     The String value containing the .json format file
     */
    public void saveJsonToInternal(Context context, String fileName, String json) {
        FileOutputStream fileOutputStream = null;
        try {
            //Prints the String value inside of the designated file.
            fileOutputStream = context.openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            Log.d("Data_management_process", fileName + " saved successfully");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
     *
     * @param fileName The name of the file to load from the Internal Storage
     * @return Returns the content of the selected file
     */
    public String loadJsonFromInternal(Context context, String fileName) {
        FileInputStream fileInputStream = null;
        String text = "";
        String json = "";
        try {
            //Opens the file
            fileInputStream = context.openFileInput(fileName);
            //Converts the content of the file into a String value
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((text = bufferedReader.readLine()) != null) {
                stringBuilder.append(text).append("\n");
            }

            json = stringBuilder.toString();
            Log.d("Data_management_process", fileName + " loaded successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }


    /**
     * This method deletes the specified file to delete
     *
     * @param context
     * @param fileName Name of the file to delete from the Internal Storage
     */
    public void deleteInternalFile(Context context, String fileName) {
        //context.deleteFile(fileName);
        File dir = context.getFilesDir();
        File file = new File(dir, fileName);
        file.delete();
    }


    /**
     * This method verifies if a specified file exists in the Internal Storage
     *
     * @param context
     * @param fileName Name of the file to verify
     * @return Returns a boolean value, representing the presence of a file in the Internal Storage
     */
    public boolean doesFileExist(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
}
