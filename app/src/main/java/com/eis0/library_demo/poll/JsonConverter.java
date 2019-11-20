package com.eis0.library_demo.poll.main;

/**
 * Interface to implement to convert .json files from and to the specified object type
 *
 * @author Enrico Cestaro
 */

public interface JsonConverter<T> {
    /**
     * Converts the object
     */
    String convertToJson(T objectToConvert);

    /**
     * Converts the .json
     */
    T convertFromJson(String json);
}
