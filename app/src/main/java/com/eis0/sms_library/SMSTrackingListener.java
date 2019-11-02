package com.eis0.sms_library;

public interface SMSTrackingListener {
    void onSent(int resultCode);
    void onDelivered(int resultCode);
}
