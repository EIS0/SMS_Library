package com.eis0.kademlianetwork;

import androidx.annotation.Nullable;

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

    @Override
    public int hashCode() {
        return invited.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj)
            return true;

        if(obj == null || obj.getClass()!= this.getClass())
            return false;

        KademliaInvitation invitation = (KademliaInvitation) obj;
        return (invitation.invited.equals(this.invited));
    }
}
