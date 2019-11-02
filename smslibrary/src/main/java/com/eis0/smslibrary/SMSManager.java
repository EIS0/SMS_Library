package com.eis0.smslibrary;

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

    public void addReceiveListener(ReceivedMessageListener listener){
        SMSHandler.addReceiveListener(listener);
    }

    public void removeReceiveListener(){
        SMSHandler.removeReceiveListener();
    }

    public void sendMessage(Message message){
        SMSHandler.sendMessage(message);
    }
}
