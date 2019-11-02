package com.eis0.smslibrary;

public interface CommunicationHandler {
    void sendMessage(Message msg);
    void addReceiveListener(ReceivedMessageListener listener);
    void removeReceiveListener();
}
