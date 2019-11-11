/**
 * Interface to implement to save, load and delete files
 *
 * @author Enrico Cestaro
 */
package com.eis0.storagelibrary;

import android.content.Context;

public interface InternalMemoryStorage {
    /**
     * Saves a .json file with the specified name
     */
    void saveJsonToInternal(Context context, String fileName, String json);

    /**
     * Load the content of the .json file specified by name
     */
    String loadJsonFromInternal(Context context, String fileName);

    /**
     * Delete the file specified by name
     */
    void deleteInternalFile(Context context, String fileName);
}


