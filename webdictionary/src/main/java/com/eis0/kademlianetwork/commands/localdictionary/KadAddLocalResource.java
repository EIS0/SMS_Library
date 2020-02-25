package com.eis0.kademlianetwork.commands.localdictionary;

import androidx.annotation.NonNull;

import com.eis0.netinterfaces.NetDictionary;
import com.eis0.netinterfaces.commands.AddResource;

/**
 * Command to add a resource to the local dictionary
 *
 * @author Edoardo Raimondi
 * @author Marco Cognolato (edited comments)
 */
public class KadAddLocalResource extends AddResource<String, String> {

    /**
     * Constructor for the AddLocalResource Command, needs data to work
     *
     * @param key             The key identifier of the resource to add to the dictionary
     * @param value           The resource value to add to the dictionary
     * @param localDictionary where pair <key, value> has to be inserted
     */
    public KadAddLocalResource(@NonNull String key, @NonNull String value, @NonNull NetDictionary<String, String> localDictionary) {
        super(key, value, localDictionary);
    }

    /**
     * Add the key-resource pair in the dictionary
     */
    public void execute() {
        netDictionary.addResource(key, value);
    }

}
