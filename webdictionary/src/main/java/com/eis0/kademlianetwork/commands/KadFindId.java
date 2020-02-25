package com.eis0.kademlianetwork.commands;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.KademliaFailReason;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.activitystatus.FindIdTimer;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindIdRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.Command;

/**
 * Command used to find an id inside the network
 *
 * @author Marco Cognolato
 */
public class KadFindId extends Command {

    private KademliaId idToFind;
    private RequestsHandler requestsHandler;
    private KademliaFailReason failReason;
    private boolean hasSucceeded = false;
    private SMSPeer peerFound;

    public KadFindId(@NonNull KademliaId idToFind, @NonNull RequestsHandler requestsHandler) {
        this.idToFind = idToFind;
        this.requestsHandler = requestsHandler;
    }

    /**
     * Executes the FindId command
     */
    protected void execute() {
        FindIdRequest findIdRequest = requestsHandler.startFindIdRequest(idToFind);
        SMSPeer searcher = KademliaJoinableNetwork.getInstance().getLocalNode().getPeer();
        IdFinderHandler.searchId(idToFind, searcher);

        //wait for the findIdRequest to finish
        new FindIdTimer(findIdRequest).run();

        //if I'm here it means that either the request took too long, or that I have found the
        //closest person to the id to find
        if (!findIdRequest.isCompleted()) {
            failReason = KademliaFailReason.REQUEST_EXPIRED;
            return;
        }
        hasSucceeded = true;
        peerFound = findIdRequest.getPeerFound();
    }

    /**
     * @return Returns true if the FindIdStarter command completed successfully, false otherwise
     */
    public boolean hasSuccessfullyCompleted() {
        return hasSucceeded;
    }

    /**
     * @return Returns the SMSPeer found after the findId command, null if no peer was found
     */
    public SMSPeer getPeerFound() {
        return peerFound;
    }

    /**
     * @return Returns the KademliaFailReason if the findId operation has failed, null otherwise
     */
    public KademliaFailReason getFailReason() {
        return failReason;
    }
}
