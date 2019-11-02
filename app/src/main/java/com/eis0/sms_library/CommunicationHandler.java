package com.eis0.sms_library;

public interface CommunicationHandler {
    void sendMessage(Message msg);
    void addReceiveListener(ReceivedMessageListener listener);
    void removeReceiveListener();
}
