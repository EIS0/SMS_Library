package com.eis0.easypoll;

import android.content.Context;
import android.util.Log;

import com.eis0.easypoll.poll.PollListener;
import com.eis0.easypoll.poll.PollManager;
import com.eis0.easypoll.poll.TernaryPoll;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * It's the PollManager listener, it handles polls updates by splitting them out in three
 * categories (incoming, opened, closed) and providing them to the ListAdapters.
 *
 * @author Matteo Carnelos
 */
public class DataProvider extends Observable implements PollListener {

    private static final String LOG_KEY = "DATA_PROVIDER";
    private static final String INCOMING_POLLS_FILE_NAME = "incomingPolls.json";
    private static final String OPENED_POLLS_FILE_NAME = "openedPolls.json";
    private static final String CLOSED_POLLS_FILE_NAME = "closedPolls.json";
    // Must always be static for getInstance to work
    private static DataProvider instance = null;
    private static List<TernaryPoll> incomingPolls = new ArrayList<>();
    private static List<TernaryPoll> openedPolls = new ArrayList<>();
    private static List<TernaryPoll> closedPolls = new ArrayList<>();

    /**
     * DataProvider constructor, sets this as the PollManager listener.
     * It cannot be accessed from outside the class because this follows the Singleton Design Pattern.
     *
     * @author Matteo Carnelos
     */
    private DataProvider() {
        PollManager.getInstance().setPollListener(this);
    }

    /**
     * Returns a new instance of DataProvider if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     *
     * @return The only instance of this class.
     * @author Matteo Carnelos
     */
    public static DataProvider getInstance() {
        if (instance == null) instance = new DataProvider();
        return instance;
    }

    /**
     * Save all the incoming/opened/closed polls data in the internal memory.
     *
     * @param context The context of the application.
     * @author Matteo Carnelos
     */
    static void saveDataToInternal(Context context) {
        savePollsList(incomingPolls, INCOMING_POLLS_FILE_NAME, context);
        savePollsList(openedPolls, OPENED_POLLS_FILE_NAME, context);
        savePollsList(closedPolls, CLOSED_POLLS_FILE_NAME, context);
    }

    /**
     * Save a poll list in the internal storage as a JSON file.
     *
     * @param list The list of Poll to save.
     * @param fileName The name of the file.
     * @param context The application context.
     * @author Matteo Carnelos
     */
    private static void savePollsList(List<TernaryPoll> list, String fileName, Context context) {
        Type listType = new TypeToken<List<TernaryPoll>>() {}.getType();
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String listJson = gson.toJson(list, listType);
        File listFile = new File(context.getFilesDir(), fileName);
        try {
            FileWriter fileWriter = new FileWriter(listFile, false);
            fileWriter.write(listJson);
            fileWriter.close();
        } catch (IOException e) {
            Log.e(LOG_KEY, "Unable to save list \"" + fileName + "\" to internal memory.");
        }
    }

    /**
     * Load all the incoming/opened/closed polls data from the internal memory.
     *
     * @param context The context of the application.
     * @author Matteo Carnelos
     */
    static void loadDataFromInternal(Context context) {
        incomingPolls.addAll(loadPollsList(INCOMING_POLLS_FILE_NAME, context));
        openedPolls.addAll(loadPollsList(OPENED_POLLS_FILE_NAME, context));
        closedPolls.addAll(loadPollsList(CLOSED_POLLS_FILE_NAME, context));
        PollManager.updateSentPolls();
    }

    /**
     * Load a poll list (in JSON format) from the internal storage.
     *
     * @param fileName The name of the file.
     * @param context The application context.
     * @return The loaded list, or an empty list if the file was not found.
     * @author Matteo Carnelos
     */
    private static List<TernaryPoll> loadPollsList(String fileName, Context context) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            Log.e(LOG_KEY, "List \"" + fileName + "\" not found in internal memory.");
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
        Type listType = new TypeToken<List<TernaryPoll>>() {}.getType();
        return new Gson().fromJson(listJson, listType);
    }

    /**
     * Get all the incoming polls.
     *
     * @return The incoming polls ArrayList.
     * @author Matteo Carnelos
     */
    public static List<TernaryPoll> getIncomingPolls() {
        return incomingPolls;
    }

    /**
     * Get all the opened polls.
     *
     * @return The opened polls ArrayList.
     * @author Matteo Carnelos
     */
    public static List<TernaryPoll> getOpenedPolls() {
        return openedPolls;
    }

    /**
     * Get all the closed polls.
     *
     * @return The closed polls ArrayList.
     * @author Matteo Carnelos
     */
    public static List<TernaryPoll> getClosedPolls() {
        return closedPolls;
    }

    /**
     * Called whenever a new poll is received by other users, adds it to the incomingPoll list and
     * notifies all the observers.
     *
     * @param poll The poll received.
     * @author Matteo Carnelos
     */
    public void onPollReceived(TernaryPoll poll) {
        incomingPolls.add(poll);
        setChanged();
        notifyObservers(poll);
    }

    /**
     * Called whenever a sent poll is created or receives an update (after an answer). When a poll
     * receives all the answers it is considered closed, so it will be moved to the closedPolls list.
     *
     * @param poll The poll created or updated.
     * @author Matteo Carnelos
     */
    public void onSentPollUpdate(TernaryPoll poll) {
        // Two scenarios:
        // Poll Closed, move it from the openedPolls to the closedPolls list
        // Poll Opened, it can be either a new poll or an update to an existing one
        if (poll.isClosed()) {
            openedPolls.remove(poll);
            closedPolls.add(poll);
        } else {
            int pollIndex = openedPolls.indexOf(poll);
            if (pollIndex == -1) openedPolls.add(poll);
            else openedPolls.set(pollIndex, poll);
        }
        setChanged();
        notifyObservers(poll);
    }
}
