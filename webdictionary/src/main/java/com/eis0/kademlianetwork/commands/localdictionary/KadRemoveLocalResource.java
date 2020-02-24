package com.eis0.kademlianetwork.commands.localdictionary;

import com.eis0.netinterfaces.NetDictionary;
import com.eis0.netinterfaces.commands.RemoveResource;

public class KadRemoveLocalResource extends RemoveResource<String, String> {

    private NetDictionary localDictionary;

    /**
     * @param key of the resource to remove
     * @param localDictionary where the resource is
     */
    public KadRemoveLocalResource(String key, NetDictionary localDictionary){
        super(key, localDictionary);
    }

    /**
     * Remove the resource from the dictionary
     * @throws NullPointerException if there isn't that resource in my dictionary
     */
    public void execute(){
        localDictionary.removeResource(key);
    }
}
