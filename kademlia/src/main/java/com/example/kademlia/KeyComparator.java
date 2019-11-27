package com.example.kademlia;


import java.math.BigInteger;
import java.util.Comparator;

/**
 * A Comparator to compare 2 keys to a given key
 */
public class KeyComparator implements Comparator<SMSKademliaNode>
{

    private final BigInteger key;

    /**
     * @param key The NodeId relative to which the distance should be measured.
     */
    public KeyComparator(KademliaId key)
    {
        this.key = key.getInt();
    }

    /**
     * Compare two objects which must both be of type <code>Node</code>
     * and determine which is closest to the identifier specified in the
     * constructor.
     *
     * @param n1 Node 1 to compare distance from the key
     * @param n2 Node 2 to compare distance from the key
     */
    @Override
    public int compare(SMSKademliaNode n1, SMSKademliaNode n2)
    {
        BigInteger b1 = n1.getNodeId().getInt();
        BigInteger b2 = n2.getNodeId().getInt();

        b1 = b1.xor(key);
        b2 = b2.xor(key);

        return b1.abs().compareTo(b2.abs());
    }
}