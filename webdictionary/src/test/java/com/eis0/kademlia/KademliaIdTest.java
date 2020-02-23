package com.eis0.kademlia;

import com.eis.smslibrary.SMSPeer;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class KademliaIdTest {

    private final int ID_LENGTH = KademliaId.ID_LENGTH;
    private final int ID_LENGTH_BYTES = KademliaId.ID_LENGTH_BYTES;

    private final SMSPeer VALID_PEER = new SMSPeer("+393408140326");
    private final KademliaId RANDOM_ID = new KademliaId();
    private final KademliaId STRING_ID = new KademliaId("11111111");
    private final KademliaId NUMBER_ID = new KademliaId(VALID_PEER);
    private final KademliaId SIMPLE_ID1 = new KademliaId("\03"); //0x03
    private final KademliaId SIMPLE_ID2 = new KademliaId("\02"); //0x02
    //by construction SIMPLE_ID1 xor SIMPLE_ID2 = SIMPLE_ID3
    private final KademliaId SIMPLE_ID3 = new KademliaId("\01"); //0x01

    private final String TEXT_TOO_LONG =
            new String(new char[ID_LENGTH+3]).replace('\0', 'a');


    @Test
    public void creationId_BySMSPeer() {
        KademliaId test = new KademliaId(VALID_PEER);
        String id = test.getInt().toString(16);
        /* SHA-256 hash of test SMSPeer, calculated with an external
        program, and truncated to the first 160 bits */
        String NUMBER_HASH = "8202021e5ef98f7a2dfcda894e49b8";
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

    /**
     * @author Giovanni Velludo
     */
    @Test
    public void nullString_sameAsEmptyByteArray(){
        KademliaId id1 = new KademliaId("");
        byte[] empty = new byte[1];
        KademliaId id2 = new KademliaId(empty);
        assertEquals(id1, id2);
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
        KademliaId toReturn = NUMBER_ID.xor(toCompareDistance);
        assertEquals(toReturn.getInt().toString(16), "0");
    }
    @Test
    public void getInt(){
        byte[] to = new byte[1];
        to[0] = 3;
        BigInteger expected = new BigInteger(to);
        assertEquals(SIMPLE_ID1.getInt(), expected);
    }

    @Test
    public void equals(){
        KademliaId STRING_ID = new KademliaId("11111111");
        assertTrue(STRING_ID.equals(STRING_ID));
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
        int shouldResult = NUMBER_ID.getDistance(toHaveDistance);
        assertEquals(shouldResult, 0);
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
        new KademliaId("745AF3B726C7C6F");
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
    public void distanceToSame_isZero(){
        assertEquals(STRING_ID.getDistance(STRING_ID), 0);
    }

    @Test
    public void hexRepresentation_ByPhoneNumber(){
        String shouldBe = "8202021E5EF98F7A2DFCDA894E49B8"; //calculated outside
        assertEquals(NUMBER_ID.hexRepresentation(), shouldBe);
    }

    @Test
    public void hexRepresentation_ByString(){
        String shouldBe = "000000000000003131313131313131"; //calculated outside
        assertEquals(STRING_ID.hexRepresentation(), shouldBe);
    }
}
