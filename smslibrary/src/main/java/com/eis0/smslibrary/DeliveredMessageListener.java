package com.eis0.smslibrary;

public interface DeliveredMessageListener {
    void onMessageDelivered(int resultCode, SMSMessage message);
}
