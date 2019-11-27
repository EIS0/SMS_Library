package com.example.kademlia;


import java.io.Serializable;


/**
 * A Node in the Kademlia network - Contains basic node network information.
 */
public class SMSKademliaNode implements Serializable
{

    private KademliaId nodeId;
    private int port;
    private final String strRep;

    public SMSKademliaNode(KademliaId nid, int port)
    {
        this.nodeId = nid;
        this.port = port;
        this.strRep = this.nodeId.toString();
    }



    /**
     * @return The NodeId object of this node
     */
    public KademliaId getNodeId() { return this.nodeId; }





    @Override
    public boolean equals(Object toCompare)
    {
        if (toCompare instanceof SMSKademliaNode)
        {
            SMSKademliaNode n = (SMSKademliaNode) toCompare;
            if (n == this)
            {
                return true;
            }
            return this.getNodeId().equals(n.getNodeId());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return this.getNodeId().hashCode();
    }

    @Override
    public String toString()
    {
        return this.getNodeId().toString();
    }
}