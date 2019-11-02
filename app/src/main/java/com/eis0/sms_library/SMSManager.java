package com.eis0.sms_library;

import android.app.Activity;
import android.app.PendingIntent;

public class SMSManager implements ComunicationHandler {
    //Singleton Design Pattern
    private SMSManager() { }
    private static SMSManager instance = null;
    public static SMSManager getInstance(){
        if(instance == null){
            instance = new SMSManager();
        }
        return instance;
    }

    public void sendMessage(Message message){
        SMSHandler.SMSSendMessage(message, null, null);
    }

    public static void sendTrackingSms(String to, String message, PendingIntent sent, PendingIntent delivered){
        SMSHandler.SMSSendMessage(to, message, sent, delivered);
    }

    public void addReceiveListener(ReceivedMessageListener listener){
        ReceivedMessageListener newListener = new ReceivedMessageListener() {
            @Override
            public void SMSOnReceive(String from, String message) {
                listener.onReceiveSMS(from, message);
            }
        };
        SMSHandler.setSMSOnReceiveListener(newListener);
    }

    public static void checkPermissions(Activity activity) {
        SMSCore.checkPermissions(activity);
    }
}
