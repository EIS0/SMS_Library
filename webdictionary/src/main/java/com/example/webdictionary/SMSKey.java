package com.example.webdictionary;


/**
 * @author Edoardo Raimondi
 */

public class SMSKey implements Key<Object> {

    private Object key;
    private volatile int hash = 0; //to manage hashCode

    public SMSKey(Object key) { this.key = key; }

    /**
     * @return key
     */
    public Object getKey() { return key; }

    /**
     * Override hashCode method to have the same hashCode everywhere
     * @return integer representing the key hashCode
     */
    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0) {
            h = key.hashCode();
            hash = h;
        }
        return h;
    }
}
