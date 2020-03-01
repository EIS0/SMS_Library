package com.eis0.easypoll;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis0.easypoll.poll.BinaryPoll;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * It's the PollManager listener, it handles polls updates by splitting them out in three
 * categories (incoming, opened, closed) and providing them to the ListAdapters.
 *
 * @author Matteo Carnelos
 */
public class DataProvider extends Observable {

    private static final String LOG_KEY = "DATA_PROVIDER";
    private static final String INCOMING_POLLS_FILE_NAME = "incomingPolls.json";
    private static final String OPENED_POLLS_FILE_NAME = "openedPolls.json";
    private static final String CLOSED_POLLS_FILE_NAME = "closedPolls.json";

    private static DataProvider instance = null;

    private static List<BinaryPoll> incomingPolls = new ArrayList<>();
    private static List<BinaryPoll> openedPolls = new ArrayList<>();
    private static List<BinaryPoll> closedPolls = new ArrayList<>();
    private static File filesDir;

    // ---------------------------- SINGLETON CONSTRUCTORS ---------------------------- //

    /**
     * DataProvider constructor, sets this as the PollManager listener.<br>
     * It cannot be accessed from outside the class because this follows the Singleton Design Pattern.
     *
     * @author Matteo Carnelos
     */
    private DataProvider() {

    }

    /**
     * Returns a new instance of DataProvider if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     *
     * @return The only instance of this class.
     * @author Matteo Carnelos
     */
    @NonNull
    public static DataProvider getInstance() {
        if (instance == null) instance = new DataProvider();
        return instance;
    }

    // ---------------------------- DATA STORING ---------------------------- //

    /**
     * Set the output directory for file saving.<br>
     * If you want to use the memorization feature you should set the outputFilesDir as soon as the
     * app starts.
     *
     * @author Matteo Carnelos
     */
    static void setOutputFilesDir(@Nullable File outputFilesDir) {
        filesDir = outputFilesDir;
    }

    /**
     * Save all the incoming/opened/closed polls data in the internal memory.<br>
     * Note: This method needs the output directory to be previously set with
     * {@link #setOutputFilesDir(File)} otherwise it will return doing nothing.
     *
     * @author Matteo Carnelos
     */
    private static void saveDataToInternal() {
        if(filesDir == null) return;
        savePollsList(incomingPolls, INCOMING_POLLS_FILE_NAME, filesDir);
        savePollsList(openedPolls, OPENED_POLLS_FILE_NAME, filesDir);
        savePollsList(closedPolls, CLOSED_POLLS_FILE_NAME, filesDir);
    }

    /**
     * Load all the incoming/opened/closed polls data from the internal memory.<br>
     *
     * @param context The context of the application.
     * @author Matteo Carnelos
     */
    static void loadDataFromInternal(@NonNull Context context) {
        incomingPolls = loadPollsList(INCOMING_POLLS_FILE_NAME, context);
        openedPolls = loadPollsList(OPENED_POLLS_FILE_NAME, context);
        closedPolls = loadPollsList(CLOSED_POLLS_FILE_NAME, context);
    }

    /**
     * Save a poll list in the internal storage as a JSON file.
     *
     * @param list The list of Poll to save.
     * @param fileName The name of the file.
     * @param directory The directory path.
     * @author Matteo Carnelos
     */
    private static void savePollsList(@NonNull List<BinaryPoll> list, @NonNull String fileName, @NonNull File directory) {
        Type listType = new TypeToken<List<BinaryPoll>>() {}.getType();
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String listJson = gson.toJson(list, listType);
        File listFile = new File(directory, fileName);
        try {
            FileWriter fileWriter = new FileWriter(listFile, false);
            fileWriter.write(listJson);
            fileWriter.close();
        } catch (IOException e) {
            Log.e(LOG_KEY, "Unable to save list \"" + fileName + "\" to internal memory.");
        }
    }

    /**
     * Load a poll list (in JSON format) from the internal storage.
     *
     * @param fileName The name of the file.
     * @param context The application context.
     * @return The loaded list, or an empty list if the file was not found.
     * @author Matteo Carnelos
     */
    @NonNull
    private static List<BinaryPoll> loadPollsList(@NonNull String fileName, @NonNull Context context) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            Log.i(LOG_KEY, "List \"" + fileName + "\" not found in internal memory.");
            return new ArrayList();
        }
        String listJson = "";
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while(line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
            listJson = stringBuilder.toString();
        } catch (IOException e) {
            Log.e(LOG_KEY, "Unable to read data from list \"" + fileName + "\".");
        }
        Type listType = new TypeToken<List<BinaryPoll>>() {}.getType();
        return new Gson().fromJson(listJson, listType);
    }

    // ---------------------------- GETTERS ---------------------------- //

    /**
     * Get all the incoming polls.
     *
     * @return The incoming polls ArrayList.
     * @author Matteo Carnelos
     */
    @NonNull
    public static List<BinaryPoll> getIncomingPolls() {
        return incomingPolls;
    }

    /**
     * Get all the opened polls.
     *
     * @return The opened polls ArrayList.
     * @author Matteo Carnelos
     */
    @NonNull
    public static List<BinaryPoll> getOpenedPolls() {
        return openedPolls;
    }

    /**
     * Get all the closed polls.
     *
     * @return The closed polls ArrayList.
     * @author Matteo Carnelos
     */
    @NonNull
    public static List<BinaryPoll> getClosedPolls() {
        return closedPolls;
    }

    /**
     * Tell if {@link DataProvider} has no polls.
     *
     * @return True is there are no polls, false otherwise.
     * @author Matteo Carnelos
     */
    public static boolean isEmpty() {
        return incomingPolls.isEmpty() && openedPolls.isEmpty() && closedPolls.isEmpty();
    }

    // ---------------------------- SETTERS ---------------------------- //

    /**
     * This method overrides the Observable.setChanged() method.<br>
     * Save the dataset anytime is changed.
     *
     * @author Matteo Carnelos
     */
    @Override
    protected void setChanged() {
        super.setChanged();
        saveDataToInternal();
    }

    // ---------------------------- LISTENING EVENTS ---------------------------- //

    /**
     * Add a poll to the opened polls list. If the poll is received from another device add it also
     * to the incoming polls list. Finally notify observers.<br>
     * It is a synchronized method in order to avoid concurrent modifications of the lists.
     *
     * @param poll The poll to add.
     * @author Matteo Carnelos
     */
    public synchronized void addPoll(@NonNull BinaryPoll poll) {
        openedPolls.add(poll);
        if(poll.isIncoming())
            incomingPolls.add(poll);
        setChanged();
        notifyObservers(poll);
    }

    /**
     * Updated the poll data. If the poll is closed move it to the closed polls list. Finally
     * notify observers.<br>
     * It is a synchronized method in order to avoid concurrent modifications of the lists.
     *
     * @param poll The poll to update.
     * @author Matteo Carnelos
     */
    public synchronized void updatePoll(@NonNull BinaryPoll poll) {
        if(poll.isClosed()) {
            openedPolls.remove(poll);
            closedPolls.add(poll);
        } else openedPolls.set(openedPolls.indexOf(poll), poll);
        setChanged();
        notifyObservers(poll);
    }
}
