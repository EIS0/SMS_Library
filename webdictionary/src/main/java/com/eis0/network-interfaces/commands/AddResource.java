package com.eis.communication.network.commands;

import androidx.annotation.NonNull;

import com.eis.communication.network.NetDictionary;

/**
 * Command to add a resource to the net dictionary
 *
 * @author Edoardo Raimondi
 * @author Marco Cognolato
 * @author Giovanni Velludo
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
