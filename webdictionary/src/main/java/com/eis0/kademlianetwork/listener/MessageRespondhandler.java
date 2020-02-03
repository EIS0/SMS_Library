package com.eis0.kademlianetwork.listener;

import java.io.IOException;

/**
 * Observer of message responds
 */
public interface MessageRespondhandler {

    void onRespondError(IOException exception);

    void waitForRespond() throws InterruptedException;

}
