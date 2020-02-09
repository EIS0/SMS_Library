package com.eis0.kademlianetwork.commands.localdictionary;

import androidx.annotation.NonNull;

import com.eis0.netinterfaces.NetDictionary;
import com.eis0.netinterfaces.commands.AddResource;

public class KadAddLocalResource extends AddResource<String, String> {

    private NetDictionary localDictionary;

    /**
     * @param key
     * @param value
     * @param localDictionary where pair <key, value> has to be inserted
     */
    public KadAddLocalResource(@NonNull String key, @NonNull String value, @NonNull NetDictionary localDictionary){
       super(key, value, localDictionary);
    }

    /**
     * Add the pair in the dictionary
     */
    public void execute(){
        localDictionary.addResource(key, value);
    }

}
