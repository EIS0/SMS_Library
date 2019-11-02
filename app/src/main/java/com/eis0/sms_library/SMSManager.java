package com.eis0.sms_library;

public class SMSManager implements CommunicationHandler {
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

    public void sendMessage(Message message, SentMessageListener sentListener){
        SMSHandler.SMSSendMessage(message, null, null);
    }

    public void sendMessage(Message message, SentMessageListener sentListener, DeliveredMessageListener deliveredListener){
        SMSHandler.SMSSendMessage(message, null, null);
    }


    public void addReceiveListener(ReceivedMessageListener listener){
        SMSHandler.setSMSOnReceiveListener(listener);
    }
}
