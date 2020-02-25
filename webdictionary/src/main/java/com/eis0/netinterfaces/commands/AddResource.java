package com.eis0.netinterfaces.commands;

import androidx.annotation.NonNull;

import com.eis0.netinterfaces.NetDictionary;

/**
 * Command to add a resource to the net dictionary
 *
 * @author Edoardo Raimondi
 * @author Marco Cognolato (edited a comment)
 * @author Giovanni Velludo (added @NonNull)
 */
public abstract class AddResource<K, R> extends Command {

    protected final K key;
    protected final R value;
    protected final NetDictionary<K, R> netDictionary;

    /**
     * Constructor for the AddResource command, needs the data to operate
     *
     * @param key           The key of the resource to add
     * @param value         The value of the resource to add
     * @param netDictionary The dictionary to add the resource in
     */
    public AddResource(@NonNull K key, @NonNull R value, @NonNull NetDictionary<K, R> netDictionary) {
        this.key = key;
        this.value = value;
        this.netDictionary = netDictionary;
    }

    /**
     * Adds the key-resource pair to the dictionary, then broadcasts the message
     */
    protected abstract void execute();
}
