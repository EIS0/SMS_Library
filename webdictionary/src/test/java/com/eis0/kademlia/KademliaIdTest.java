package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.*;

public class KademliaIdTest {

    private int ID_LENGTH = KademliaId.ID_LENGTH;
    private int ID_LENGTH_BYTES = KademliaId.ID_LENGTH_BYTES;

    private final KademliaId RANDOM_ID = new KademliaId();
    private final KademliaId STRING_ID = new KademliaId("11111111111111111111");
    private final KademliaId NUMBER_ID = new KademliaId(new SMSPeer("3497463255"));
    private final KademliaId SIMPLE_ID1 = new KademliaId("\03"); //0x03
    private final KademliaId SIMPLE_ID2 = new KademliaId("\02"); //0x02
    //by construction SIMPLE_ID1 xor SIMPLE_ID2 = SIMPLE_ID3
    private final KademliaId SIMPLE_ID3 = new KademliaId("\01"); //0x01
    /* SHA-256 hash of test SMSPeer, calculated with an external
     program, and truncated to the first 160 bits */
    private final String NUMBER_HASH = "744d03373e3d5cc8e61259efe99e9dbc00fc6b69";

    @Test
    public void creationId_BySMSPeer() {
        KademliaId test = new KademliaId(new SMSPeer("3497463255"));
        String id = test.getInt().toString(16);
        assertEquals(NUMBER_HASH, id);
    }

    @Test
    public void xorTest_asExpected(){
        assertEquals(SIMPLE_ID1.xor(SIMPLE_ID2), SIMPLE_ID3);
    }

    @Test
    public void creationRandomId_shouldBeDifferent() {
        KademliaId toCompare = new KademliaId();
        assertNotEquals(RANDOM_ID, toCompare);
    }

    @Test
    public void creationId_ByBytes() {
        byte[] result = new byte[ID_LENGTH_BYTES];
        for (int i = 0; i < ID_LENGTH_BYTES; i++) {
            result[i] = (byte) 1;
        }
        KademliaId testByByte = new KademliaId(result);
        assertEquals(testByByte, testByByte);
    }

    @Test
    public void xorDistanceTestByString() {
        KademliaId toCompareDistance = new KademliaId("11111111111111111110");
        KademliaId toReturn = STRING_ID.xor(toCompareDistance);
        byte[] distance = new byte[ID_LENGTH_BYTES];
        for (int i = 0; i < ID_LENGTH_BYTES - 1; i++) {
            distance[i] = 0;
        }
        distance[ID_LENGTH_BYTES - 1] = 1; // I expect a distance of 1
        KademliaId Distance = new KademliaId(distance);
        assertEquals(toReturn, Distance);
    }

    @Test
    public void xorDistanceTest_ByPhoneNumber() {
        SMSPeer distance = new SMSPeer("3497463254");
        KademliaId toCompareDistance = new KademliaId(distance);
        /*distance calculated with external programs*/
        String hashCodeResultDistance = "2a561e48699e5d1b59deb931db6580b386495e2f";
        KademliaId toReturn = NUMBER_ID.xor(toCompareDistance);
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
        int shouldResult = NUMBER_ID.getDistance(toHaveDistance);
        assertEquals(shouldResult, result);
    }
}