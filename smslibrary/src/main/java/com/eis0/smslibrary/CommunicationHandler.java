package com.eis0.smslibrary;

public interface CommunicationHandler {
    void sendMessage(SMSMessage msg);
    void addReceiveListener(ReceivedMessageListener listener);
    void removeReceiveListener();
}
