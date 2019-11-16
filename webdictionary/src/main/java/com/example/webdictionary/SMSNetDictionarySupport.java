package com.example.webdictionary;

import java.util.Arrays;

/**
 * This class contains support methods for
 * SMSNetDictionary
 * @author Edoardo Raimondi
 */


public class SMSNetDictionarySupport {
    /**
     * Doubles array size
     * @param array to expand
     * @return Same array with double size
     */
    protected static SMSResource[] doubleArraySize(SMSResource[] array) {
        return java.util.Arrays.copyOf(array, array.length * 2);
    }
    /**
     *  Concatenates two arrays. If only one of them is null the other is returned, if both
     *  are null, null is returned
     * @param array1 to concatenates with array2
     * @param array2 to concatenates with array1
     * @return Array containing all the elements of param's arrays; null if both arrays are null,
     * the other array if only one is null
     */
    protected static SMSResource[] concatAll(SMSResource[] array1, SMSResource[] array2) {
        if(array1 == null && array2 == null) return null;
        if(array1 == null) return array2;
        if(array2 == null) return array1;
        int totalLength = array1.length + array2.length;
        SMSResource[] result = Arrays.copyOf(array1, totalLength);

        int offset = array1.length;

        System.arraycopy(array2, 0, result, offset, array2.length);
        return result;
    }
}
