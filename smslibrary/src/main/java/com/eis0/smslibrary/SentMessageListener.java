package com.eis0.smslibrary;

public interface SentMessageListener {
    void onMessageSent(int resultCode, SMSMessage message);
}
