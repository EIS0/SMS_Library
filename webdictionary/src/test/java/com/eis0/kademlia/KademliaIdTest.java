package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.*;

public class KademliaIdTest {

    private int ID_LENGTH = KademliaId.ID_LENGTH;
    private int ID_LENGTH_BYTES = KademliaId.ID_LENGTH_BYTES;

    private KademliaId testByRandom = new KademliaId();
    private KademliaId testByString = new KademliaId("11111111111111111111");
    private KademliaId testByNumber = new KademliaId(new SMSPeer("3497463255"));

    @Test
    public void creationId_BySMSPeer() {
        KademliaId test = new KademliaId(new SMSPeer("3497463255"));
        String toCompare = "744d03373e3d5cc8e61259efe99e9dbc00fc6b69"; /* SHA-256 hash of test
        SMSPeer, calculated with an external program, and truncated to the first 160 bits */
        String id = test.getInt().toString(16);
        assertEquals(toCompare, id);
    }


    @Test
    /*I except two random Ids to be different*/
    public void creationId_Random() {
        KademliaId toCompare = new KademliaId();
        assertFalse(testByRandom.equals(toCompare));
    }

    @Test
    public void creationId_ByBytes() {
        byte[] result = new byte[ID_LENGTH_BYTES];
        for (int i = 0; i < ID_LENGTH_BYTES; i++) {
            result[i] = (byte) 1;
        }
        KademliaId testByByte = new KademliaId(result);
        assertTrue(testByByte.equals(testByByte));
    }

    @Test
    public void xorDistanceTestByString() {
        KademliaId toCompareDistance = new KademliaId("11111111111111111110");
        KademliaId toReturn = testByString.xor(toCompareDistance);
        byte[] distance = new byte[ID_LENGTH_BYTES];
        for (int i = 0; i < ID_LENGTH_BYTES - 1; i++) {
            distance[i] = 0;
        }
        distance[ID_LENGTH_BYTES - 1] = 1; // I expect a distance of 1
        KademliaId Distance = new KademliaId(distance);
        assertTrue(toReturn.equals(Distance));
    }

    @Test
    public void xorDistanceTest_ByPhoneNumber() {
        SMSPeer distance = new SMSPeer("3497463254");
        KademliaId toCompareDistance = new KademliaId(distance);
        /*distance calculated with external programs*/
        String hashCodeResultDistance = "2a561e48699e5d1b59deb931db6580b386495e2f";
        KademliaId toReturn = testByNumber.xor(toCompareDistance);
        assertEquals(toReturn.getInt().toString(16), hashCodeResultDistance);
    }

    @Test
    public void getFirstSetBitIndexTest() {
        byte[] test = new byte[ID_LENGTH_BYTES];
        for (int i = 0; i < ID_LENGTH_BYTES - 1; i++) {
            test[i] = 0;
        }
        test[ID_LENGTH_BYTES - 1] = 1;
        KademliaId testByBytes = new KademliaId(test);
        /*I expect 159 leading's zero*/
        assertEquals(testByBytes.getFirstSetBitIndex(), ID_LENGTH - 1);
    }

    @Test
    public void getDistanceTest() {
        SMSPeer distance = new SMSPeer("3497463254");
        KademliaId toHaveDistance = new KademliaId(distance);
        int result = 158; // calculated outside
        int shouldResult = testByNumber.getDistance(toHaveDistance);
        assertEquals(shouldResult, result);
    }

}