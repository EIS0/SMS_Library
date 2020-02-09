package com.eis0.kademlianetwork.commands;

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
     */
    public void execute(){
        localDictionary.removeResource(key);
    }
}
