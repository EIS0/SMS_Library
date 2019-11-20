package com.eis0.library_demo.poll.main;

import android.content.Context;

/**
 * Interface to implement to save, load and delete files
 *
 * @author Enrico Cestaro
 */

public interface InternalMemoryStorage {
    /**
     * Saves a .json file with the specified name
     */
    void saveJsonToInternal(Context context, String fileName, String json);

    /**
     * Loads the content of the .json file specified by name
     */
    String loadJsonFromInternal(Context context, String fileName);

    /**
     * Deletes the file specified by name
     */
    void deleteInternalFile(Context context, String fileName);

    /**
     * Verifies if the file exists in the Internal Storage
     */
    boolean doesFileExist(Context context, String fileName);
}


