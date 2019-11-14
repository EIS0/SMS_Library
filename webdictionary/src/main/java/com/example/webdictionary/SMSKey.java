package com.example.webdictionary;

/**
 * @author Edoardo Raimondi
 */

public class SMSKey implements Key<Object> {

    private Object key;

    public SMSKey(Object key) { this.key = key; }

    /**
     * @return key
     */
    public Object getKey() { return key; }

}
