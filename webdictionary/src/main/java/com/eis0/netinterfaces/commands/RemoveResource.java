package com.eis0.netinterfaces.commands;

import androidx.annotation.NonNull;

import com.eis0.netinterfaces.NetDictionary;

/**
 * Command to remove a resource from the local network dictionary.
 * A network dictionary is a dictionary containing key-resource pairs.
 * A key identifies a resource, while a resource can be anything, from a string, to a file,
 * to a Peer with a file, basically something to keep in a network.
 *
 * @author Edoardo Raimondi
 * @author Marco Cognolato
 * @author Giovanni Velludo
 */
public abstract class RemoveResource<K, R> extends Command {

    protected final NetDictionary<K, R> netDictionary;
    protected final K key;

    /**
     * Constructor for the KadDeleteResource command, needs the data to operate
     *
     * @param key           The key identifier of the resource to remove
     * @param netDictionary The dictionary to remove the resource from
     */
    public RemoveResource(@NonNull K key, @NonNull NetDictionary<K, R> netDictionary) {
        this.key = key;
        this.netDictionary = netDictionary;
    }

    /**
     * Removes a Resource from the dictionary, then broadcasts it to the net
     */
    protected abstract void execute();
}
