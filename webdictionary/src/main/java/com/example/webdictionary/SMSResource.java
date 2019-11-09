package com.example.webdictionary;

import com.eis0.smslibrary.SMSPeer;

public class SMSResource implements Resource<SMSPeer,String> {
    private SMSPeer peer;
    private String resource;

    public SMSResource(SMSPeer peer, String resource){
        this.peer = peer;
        this.resource = resource;
    }

    /**
     * Returns the Resource data (String in this case)
     */
    public String getResource(){
        return resource;
    }

    /**
     * Returns the Peer who has the resource
     */
    public SMSPeer getPeer(){
        return peer;
    }


    /**
     * An SMS Resource (meaning a resource that can be sent through an SMS),
     * has to be serialized as a String to be able to be sent as an SMS
     * @return A String definition of an SMSResource, to identify this object
     */
    public String serialize(){
        return resource;
    }
}
