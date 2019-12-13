package com.eis0.kademlianetwork;

/**
 * Class that insert a resource to the right node
 */
public class AddToDictionaryHandler {

    /**
     * Add the resource to the right node
     * @param key     to set
     * @param content to set
     */
    public static void add(String key, String content){
        KademliaNetwork.getInstance().addToLocalDictionary(key, content);
    }

}
