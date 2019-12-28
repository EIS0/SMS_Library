package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;

import java.util.List;
import java.util.TimerTask;

/**
 * Class that define a 10 refresh timer.
 * After that time, there is a local routing table refresh.
 *
 * @author Edoardo Raimondi
 */

public class RoutingTableRefresh extends TimerTask {

    private static final long time  = 600000;
    //create a timer to verify if I had a pong in at least 10 secs
    private RespondTimer timer = new RespondTimer();
    //node doing this refresh
    private SMSKademliaNode localNode;

    public RoutingTableRefresh(SMSKademliaNode node){
        localNode = node;
    }

    @Override
    public void run() {
        completeTask();
    }

    /**
     * Send a ping message to verify if a node is alive
     *
     * @param receiver The {@link SMSKademliaNode} represented by peer that I'm looking for
     */
    private void sendPing(SMSKademliaNode receiver){
        String pingMessage = RequestTypes.Ping.ordinal() + " ";
        SMSMessage ping = new SMSMessage(receiver.getPeer(), pingMessage);
        SMSManager.getInstance().sendMessage(ping);
    }

    /**
     * Refresh my routing table every 10 minutes
     *
     * @throws InterruptedException when the thread is interrupted during the execution
     */
    private void completeTask() {
        refresh();
        /*
        try {
            //assuming there is a 10 minutes interval every refreshing task
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
        * */
    }

    private void refresh() {

        //create the list of my routing table nodes
        List<SMSKademliaNode> allRoutingTableNodes = KademliaNetwork.getInstance().getLocalRoutingTable().getAllNodes();
        for (int i = 0; i < allRoutingTableNodes.size(); i++) {
            SMSKademliaNode currentNode = allRoutingTableNodes.get(i);
            sendPing(currentNode);

            //wait 10 secs to get a pong answer
            timer.run();

            //check if I received a pong (so if the node is alive)
            if (KademliaNetwork.getInstance().getPongKnown()) {
                //is alive, set the pong state to false in order to do it again
                KademliaNetwork.getInstance().setPong(false);
            } else { //the node is not alive. I must remove it.
                KademliaId currentId = currentNode.getId();
                //I check the bucket Id that contains that node
                int b = KademliaNetwork.getInstance().getLocalRoutingTable().getBucketId(currentId);
                //I remove it. If there is a replacement in the cache, it will automatically replace my node.
                KademliaNetwork.getInstance().getLocalRoutingTable().getBuckets()[b].removeNode(currentNode);
                //now I search for another one
                askForId(currentId);
            }
        }
    }

    /**
     * Generates an id to find and searches for it in the net sending a request
     *
     * @param id the id to replace
     */
    private void askForId(KademliaId id) {
        //take the node peer
        SMSPeer peer = localNode.getPeer();
        //create the fake id
        KademliaId fakeId = id.generateNodeIdByDistance(0);

        //search in the net for the fakeId and wait
        IdFinderHandler.searchId(fakeId, peer, ResearchMode.Refresh);
    }
}