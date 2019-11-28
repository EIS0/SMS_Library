package com.eis0.kademlia;

import org.junit.Test;

import static org.junit.Assert.*;

public class KademliaIdTest {

    int ID_LENGTH = 160;

    KademliaId testByString = new KademliaId("11111111111111111111");
    KademliaId testByRandom = new KademliaId();

    @Test
    public void creationId_ByString(){
        KademliaId toCompare = new KademliaId("11111111111111111111");
        assertTrue(testByString.equals(toCompare));
    }

    @Test
    /*I except two random Ids are different*/
    public void creationId_Random(){
        KademliaId toCompare = new KademliaId();
        assertFalse(testByRandom.equals(toCompare));
    }

    @Test
   public void creationId_ByBytes(){
        byte[] result = new byte[ID_LENGTH / 8];
        for (int i = 0; i < ID_LENGTH / 8; i++) {
            result[i] = (byte) 1;
        }
        KademliaId testByByte = new KademliaId(result);
        assertTrue(testByByte.equals(testByByte));
    }

    @Test
    public void xorDistanceTest(){
        KademliaId toCompareDistance = new KademliaId("11111111111111111110");
        KademliaId toReturn = testByString.xor(toCompareDistance);
        byte[] distance = new byte[160/8];
        for(int i = 0; i < 160/8 - 1; i++){ distance[i] = 0;}
        distance[160/8-1] = 1; // I expect a distance of 1
        KademliaId Distance = new KademliaId(distance);
        assertTrue(toReturn.equals(Distance));
    }

    @Test
    public void getFirstSetBitIndexTest(){
        byte[] test = new byte[160/8];
        for(int i = 0; i < 160/8 - 1; i++){ test[i] = 0;}
        test[160/8-1] = 1;
        KademliaId testByBytes = new KademliaId(test);
        /*I expect 159 leading's zero*/
        assertEquals(testByBytes.getFirstSetBitIndex(), 159);
    }

}