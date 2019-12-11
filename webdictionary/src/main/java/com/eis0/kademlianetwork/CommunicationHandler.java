package com.eis0.kademlianetwork;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SerializableObject;

/**
 * Class to send, view or set
 * @TODO verifying if there is any need for a Set method
 * @TODO Discuss if the right position for this methods really is this class
 * @TODO Handle
 *  => The sendContent method could be inside the KademliaNetwork class, which would realise it with
 *  some more basic methods, stored inside THIS class
 *  EXAMPLE: a method to send to a specified destination, the message with the code to add a content
 *  to the dictionary
 *
 * @author Enrico Cestaro
 */

public class CommunicationHandler {

    /**
     * Method used to add a content to the dictionary
     * @param content The String value to add to the Dictionary
     */
    public static void addToDictionary(String content) {
    /*
    1. Find the set of the Nodes closest to a specified ID (the ID represents the Hash of the content to insert in the KademliaNetwork)
        => Use the SMSKademliaRoutingTable (created in the KademliaNetwork instance)
        => Use its method findClosest(targetID, number of nodes = 1)
        => This method returns a List<KademliaNode>
    2. Extract the node from the List<>
        => Get the Peer
        => Send the message to the SMSPeer
     */
        //@TODO Complete, and remove your telephone number ffs
        SMSPeer peer = new SMSPeer("+393479281192"); //andrà sostituito col Peer restituito dal metodo che trova il nodo più vicino
        SMSMessage contentMessage = new SMSMessage(peer,  KademliaNetwork.RequestType.AddToDict + " " + content);
        SMSManager.getInstance().sendMessage(contentMessage);
    }


    /**
     *
     */
    public static void receiveAddToDictionaryRequest(String contentRequested) {
    /* This method is called by the Node already chosen as the closest one to the ID, and it's been
    chosen before the request to store the content was sent; in fact, the Node requesting to store
    the content ask for the closest ID and send the content directly to it
     */
    int hashCode = contentRequested.hashCode();
    //@TODO convert SerializableObject into String. Key??
    KademliaNetwork.getInstance().addToLocalDictionary(key, resource);
    }


    /**
     *
     */
    public static void viewContentRequest() {

    }


    /**
     *
     */
    public void sendContent() {
        //@TODO Complete, and remove your telephone number ffs
        SMSPeer peer = new SMSPeer("+393479281192");
        SMSMessage contentMessage = new SMSMessage(peer,  KademliaNetwork.RequestType.AddToDict + " " + content);
        SMSManager.getInstance().sendMessage(contentMessage);
    }
}
