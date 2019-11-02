package com.eis0.sms_library;

public interface ComunicationHandler {
    void sendMessage(Message msg);
    void addReceiveListener(ReceivedMessageListener listener);
}
