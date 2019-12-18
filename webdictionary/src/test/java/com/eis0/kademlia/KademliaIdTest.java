package com.eis0.kademlia;

import com.eis.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class KademliaIdTest {

    private int ID_LENGTH = KademliaId.ID_LENGTH;
    private int ID_LENGTH_BYTES = KademliaId.ID_LENGTH_BYTES;

    private final SMSPeer VALID_PEER = new SMSPeer("+393408140326");
    private final KademliaId RANDOM_ID = new KademliaId();
    private final KademliaId STRING_ID = new KademliaId("11111111");
    private final KademliaId NUMBER_ID = new KademliaId(VALID_PEER);
    private final KademliaId SIMPLE_ID1 = new KademliaId("\03"); //0x03
    private final KademliaId SIMPLE_ID2 = new KademliaId("\02"); //0x02
    //by construction SIMPLE_ID1 xor SIMPLE_ID2 = SIMPLE_ID3
    private final KademliaId SIMPLE_ID3 = new KademliaId("\01"); //0x01
    /* SHA-256 hash of test SMSPeer, calculated with an external
     program, and truncated to the first 160 bits */
    private final String NUMBER_HASH = "108877ecc3a9b2c2";

    private final String TEXT_TOO_LONG =
            new String(new char[ID_LENGTH+3]).replace('\0', 'a');


    @Test
    public void creationId_BySMSPeer() {
        KademliaId test = new KademliaId(VALID_PEER);
        String id = test.getInt().toString(16);
        assertEquals(NUMBER_HASH, id);
    }

    @Test
    public void xorTest_asExpected(){
        assertEquals(SIMPLE_ID1.xor(SIMPLE_ID2), SIMPLE_ID3);
    }

    @Test
    public void trailingZeros_correctOrder(){
        //manually checking, a 8 char number is ordered left ot right,
        //so giving a 7 char number will skip the first char (set as 0)
        // and go on as intended
        KademliaId id1 = new KademliaId("2345678");
        KademliaId id2 = new KademliaId("\00"+"2345678");
        assertEquals(id1,id2);
    }

    @Test
    public void nullString_sameAsEmptyByteArray(){
        KademliaId id1 = new KademliaId("");
        byte[] empty = new byte[1];
        KademliaId id2 = new KademliaId(empty);
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
        KademliaId toCompareDistance = new KademliaId("11111110");
        KademliaId toReturn = STRING_ID.xor(toCompareDistance);
        byte[] distanceByteArr = new byte[ID_LENGTH_BYTES];
        for (int i = 0; i < ID_LENGTH_BYTES - 1; i++) {
            distanceByteArr[i] = 0;
        }
        distanceByteArr[ID_LENGTH_BYTES - 1] = 1; // I expect a distance of 1
        KademliaId distance = new KademliaId(distanceByteArr);
        assertEquals(toReturn, distance);
    }

    @Test
    public void metricXorDistance(){
        assertEquals(SIMPLE_ID1.getXorDistance(SIMPLE_ID2), SIMPLE_ID3.getInt());
    }

    @Test
    public void xorDistanceTest_ByPhoneNumber() {
        KademliaId toCompareDistance = new KademliaId(VALID_PEER);
        /*distance calculated with external programs*/
        String hashCodeResultDistance = "4e936a93940ab311";
        KademliaId toReturn = NUMBER_ID.xor(toCompareDistance);
        assertEquals(toReturn.getInt().toString(16), hashCodeResultDistance);
    }

    @Test
    public void getFirstSetBitIndexTest() {
        byte[] test = new byte[ID_LENGTH_BYTES];
        for (int i = 0; i < ID_LENGTH_BYTES - 1; i++) {
            test[i] = 0;
        }
        test[ID_LENGTH_BYTES - 1] = 0x01; //0b00000001
        KademliaId testByBytes = new KademliaId(test);
        /*I expect 159 leading's zero*/
        assertEquals(testByBytes.getFirstSetBitIndex(), ID_LENGTH - 1);
    }

    @Test
    public void getDistanceTest() {
        KademliaId toHaveDistance = new KademliaId(VALID_PEER);
        int result = 63; // calculated with an external problem
        int shouldResult = NUMBER_ID.getDistance(toHaveDistance);
        assertEquals(shouldResult, result);
    }

    @Test
    public void newIdByDistance_asExpected(){
        assertEquals(SIMPLE_ID1.generateNodeIdByDistance(ID_LENGTH-1), SIMPLE_ID2);
    }

    @Test
    public void anotherNewIdByDistance_asExpected(){
        assertEquals(SIMPLE_ID1.generateNodeIdByDistance(ID_LENGTH-2), SIMPLE_ID3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void idByDistanceOutOfRangeNegative_throws(){
        SIMPLE_ID1.generateNodeIdByDistance(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void idByDistanceOutOfRangePositive_throws(){
        SIMPLE_ID1.generateNodeIdByDistance(160);
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateIdTooLong_throws(){
        new KademliaId(TEXT_TOO_LONG);
    }

    @Test
    public void generateIdByHex(){
        new KademliaId("1745AF3B726C7C6F");
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateIdByByteArray_tooLongThrows(){
        new KademliaId(new byte[20]);
    }

    @Test
    public void compareIdWithAnother_different(){
        assertNotEquals(SIMPLE_ID1, new Object());
    }

    @Test
    public void firstSetIndexOfZeros_returnsNegative(){
        byte[] arr = new byte[ID_LENGTH_BYTES];
        KademliaId id = new KademliaId(arr);
        assertEquals(id.getFirstSetBitIndex(), -1);
    }

    @Test
    public void distanceToSame_distanzeZero(){
        assertEquals(STRING_ID.getDistance(STRING_ID), 0);
    }
}