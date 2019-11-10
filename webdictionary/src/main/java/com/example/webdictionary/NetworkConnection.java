package com.example.webdictionary;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

public class NetworkConnection {

    //Singleton Design Pattern
    private static NetworkConnection net;
    private NetworkConnection(){
        netDict = new SMSNetDictionary();
    }
    public static NetworkConnection getInstance(){
        if(net == null){
            net = new NetworkConnection();
        }
        return net;
    }

    private SMSNetDictionary netDict;

    public void askToJoin(SMSPeer peer){
        SMSManager.getInstance(null).sendMessage(new SMSMessage(peer, "JoinPermission"));
    }

    public void acceptJoin(){

    }

    public void inviteToJoin(){

    }

    public void sendPing(){

    }
}
