package com.eis0.kademlianetwork.activitystatus;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindIdRequest;

public class FindIdTimerTest {

    private final SMSPeer VALID_PEER = new SMSPeer("+393335552121");
    private final KademliaId VALID_ID = new KademliaId(VALID_PEER);
    private final FindIdRequest FIREQ = new FindIdRequest(VALID_ID);

}