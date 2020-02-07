package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSPeer;
import com.eis0.netinterfaces.Invitation;

public class KademliaInvitation implements Invitation<SMSPeer> {

    private SMSPeer invited;

    public KademliaInvitation(SMSPeer invited){
        this.invited = invited;
    }

    /**
     * @return The address of who sent this invitation.
     */
    public SMSPeer getInviterPeer(){
        return invited;
    }
}
