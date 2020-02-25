package com.eis0.kademlianetwork.commands.localdictionary;

import androidx.annotation.NonNull;

import com.eis0.netinterfaces.NetDictionary;
import com.eis0.netinterfaces.commands.RemoveResource;

/**
 * Command to remove a resource from the local dictionary
 *
 * @author Edoardo Raimondi
 * @author Marco Cognolato (edited comments)
 */
public class KadRemoveLocalResource extends RemoveResource<String, String> {

    /**
     * @param key             of the resource to remove
     * @param localDictionary where the resource is
     */
    public KadRemoveLocalResource(@NonNull String key, @NonNull NetDictionary<String, String> localDictionary) {
        super(key, localDictionary);
    }

    /**
     * Remove the resource from the dictionary
     *
     * @throws NullPointerException if there isn't that resource in my dictionary
     */
    public void execute() {
        netDictionary.removeResource(key);
    }
}
