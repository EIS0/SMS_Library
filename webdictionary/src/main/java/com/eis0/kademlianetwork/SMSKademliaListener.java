package com.eis0.kademlianetwork;


import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;
import com.eis0.kademlianetwork.InformationDeliveryManager.RequestTypes;

/**
 * Listener class that sends the appropriate command to the relative appropriate handler
 *
 * @author Marco Cognolato
 * @author Matteo Carnelos
 * @author Edoardo Raimondi
 * @author Enrico Cestaro
 */

public class SMSKademliaListener extends SMSReceivedServiceListener {
    IntMsgKademliaListener msgListener;

    public SMSKademliaListener(KademliaNetwork kadNet) {
        msgListener = IntMsgKademliaListener.getInstance(kadNet);
    }

    /**
     * This method analyzes the incoming messages, extracts the content, and processes it depending
     * upon the {@link RequestTypes} contained at the beginning of the message
     *
     * @param message The message received.
     */
    @Override
    public void onMessageReceived(SMSMessage message) {
        msgListener.onMessageReceived(message);
    }
}
