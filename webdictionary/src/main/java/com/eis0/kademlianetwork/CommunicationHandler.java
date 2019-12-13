package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.List;

/**
 * Class to send, view or set
 * @TODO SO, will we use only String object? AND where do we store them?
 * @TODO verifying if there is any need for a Set method
 * @TODO Discuss if the right position for this methods really is this class
 *  => The sendContent method could be inside the KademliaNetwork class, which would realise it with
 *  some more basic methods, stored inside THIS class
 *  EXAMPLE: a method to send to a specified destination, the message with the code to add a content
 *  to the dictionary
 *
 * @author Enrico Cestaro
 * @autor Edoardo Raimondi
 */

public class CommunicationHandler {

    /**
     * Method used to add a content to the dictionary
     *
     * @param peer    The receiver of the resource
     * @param key     The String key to be add to the Dictionary
     * @param content The String value to add to the Dictionary
     */
    public static void addToDictionary(SMSPeer peer, String key, String content) {

        SMSMessage contentMessage = new SMSMessage(peer, RequestTypes.AddToDict + " " + key + content);
        SMSManager.getInstance().sendMessage(contentMessage);
    }


    /**
     * Search for right receiver for the content
     *
     * @param key     to set
     * @param content to set
     */
    public static void receiveAddToDictionaryRequest(String key, String content) {
        KademliaId toFind = new KademliaId(key);
        List<SMSKademliaNode> closerNode;
        closerNode = KademliaNetwork.getInstance().getLocalRoutingTable().findClosest(toFind, 1);
        SMSPeer receiver = closerNode.get(0).getPeer();
        addToDictionary(receiver, key, content);
    }


    /**
     *
     */
    public static void viewContent() {

    }


    /**
     *
     */
    public void showContent() {

    }
}

