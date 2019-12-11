package com.eis0.kademlianetwork;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

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
 */

public class CommunicationHandler {

    /**
     *
     */
    public void sendContent(String content) {
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
        SMSPeer peer = new SMSPeer("+393479281192");
        SMSMessage contentMessage = new SMSMessage(peer,  KademliaNetwork.RequestType.AddToDict + " " + content);
        SMSManager.getInstance().sendMessage(contentMessage);
    }


    /**
     *
     */
    public void receiveContent() {

    }


    /**
     *
     */
    public void viewContent() {

    }


    /**
     *
     */
    public void showContent() {

    }
}
