package com.eis0.kademlia;

import android.util.Log;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;

/**
 * Class that defines a KademliaId.
 * It is representing by the hasCode of the user's phone number
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 * paper</a> for more details.
 * @author Edoardo Raimondi, edits by Marco Cognolato
 */
public class KademliaId implements Serializable {

    private static final String HASHING_ALG = "SHA-256";
    public final static int ID_LENGTH = 64;
    final static int ID_LENGTH_BYTES = ID_LENGTH / 8;
    private static BitSet keySet = new BitSet();
    private static byte[] keyBytes = keySet.toByteArray();

    /**
     * Generates a KademliaId using a random key
     */
    public KademliaId() {
        keyBytes = new byte[ID_LENGTH_BYTES];
        new Random().nextBytes(keyBytes);
    }

    /**
     * Construct the NodeId based on the address of the peer.
     *
     * @param peer The peer for which NodeId generation is being requested.
     * @author Giovanni Velludo
     */
    public KademliaId(SMSPeer peer) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASHING_ALG);
            md.update(peer.getAddress().getBytes());
            keyBytes = Arrays.copyOfRange(md.digest(), 0, ID_LENGTH_BYTES);
        } catch (NoSuchAlgorithmException e) {
        }
    }

    /**
     * Construct the NodeId from a string
     *
     * @param data The user generated key string
     * @throws IllegalArgumentException if data has not the right fit
     */
    public KademliaId(String data) {
        this.keyBytes = data.getBytes();
        if(data.length() == ID_LENGTH_BYTES*2){
            this.keyBytes = new byte[ID_LENGTH_BYTES];
            for (int i = 0; i < keyBytes.length; i++) {
                int index = i * 2;
                int j = Integer.parseInt(data.substring(index, index + 2), 16);
                keyBytes[i] = (byte) j;
            }
        }
        if (this.keyBytes.length > ID_LENGTH_BYTES) {
            throw new IllegalArgumentException("Specified Data need to be " + ID_LENGTH_BYTES + " characters long.");
        }

        //if the string it's too short, add some leading null bytes
        if(this.keyBytes.length < ID_LENGTH_BYTES){
            this.keyBytes = addTrailingZeros(this.keyBytes);
        }
    }

    /**
     * Use a given byte array as the NodeId.
     *
     * @param bytes The byte array to use as the NodeId.
     * @throws IllegalArgumentException if data has not the right fit
     */
    public KademliaId(byte[] bytes) {
        if (bytes.length > ID_LENGTH_BYTES) {
            throw new IllegalArgumentException("Specified Data need to be " + ID_LENGTH_BYTES + " characters long. Data Given: '" + new String(bytes) + "'");
        }
        this.keyBytes = bytes;
        //if the byte array it's too short, add some leading null bytes
        if(bytes.length < ID_LENGTH_BYTES){
            this.keyBytes = addTrailingZeros(this.keyBytes);
        }
    }


    /**
     * Returns a byte array of length ID_LENGTH_BYTES with zeros and then a given number
     * @param number The byte array of the number to trail with zeros
     * @return A byte array with the number trailed with zeros
     * @author Marco Cognolato
     */
    private byte[] addTrailingZeros(byte[] number){
        //by default each element is set to 0x00
        byte[] toReturn = new byte[ID_LENGTH_BYTES];
        //add to the end (ax expected) each element
        for(int i = 0; i < number.length; i++){
            toReturn[i+ID_LENGTH_BYTES-number.length] = number[i];
        }
        return toReturn;
    }

    /**
     * @return Bytes' array (if used)
     */
    public byte[] getBytes() {
        return this.keyBytes;
    }

    /**
     * @return The BigInteger representing the key
     */
    public BigInteger getInt() {
        return new BigInteger(1, this.getBytes());
    }

    /**
     * Compares a given NodeId to this NodeId
     *
     * @param toCompare The NodeId to compare to this NodeId
     * @return true if the 2 NodeIds are equal
     */
    @Override
    public boolean equals(Object toCompare) {
        if (toCompare instanceof KademliaId) {
            KademliaId nid = (KademliaId) toCompare;
            return Arrays.equals(this.keyBytes,nid.getBytes());
        }
        return false;
    }

    /**
     * @return hashCode of my NodeId
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Arrays.hashCode(this.keyBytes);
        return hash;
    }

    /**
     * Checks the distance between two NodeIds
     *
     * @param nid The NodeId from which to calculate the metric xor distance
     * @return The distance of this NodeId from the given NodeId as a new Kademlia Id
     */
    public KademliaId xor(KademliaId nid) {
        byte[] result = new byte[ID_LENGTH_BYTES];
        byte[] nidBytes = nid.getBytes();

        for (int i = 0; i < ID_LENGTH_BYTES; i++) {
            result[i] = (byte) (this.keyBytes[i] ^ nidBytes[i]);
        }

        return new KademliaId(result);
    }

    /**
     * Generates a NodeId that is some distance away from this NodeId
     *
     * @param distance The index of the first bit that should be different,
     *                 From 0 to 159, where 0 changes the most significant bit
     * @return The newly generated NodeId with a given distance from this.
     * @throws IllegalArgumentException if the distance is < 0 or >= 160
     * @author Edordo Raimondi, improved by Marco Cognolato
     */
    public KademliaId generateNodeIdByDistance(int distance) {
        if(distance < 0 || distance >= ID_LENGTH) throw new IllegalArgumentException();

        byte[] result = keyBytes.clone();

        // calculate the index of the byte and bit to update
        int byteToUpdateIndex = (distance) / 8;
        int bitToUpdateIndex = 7 - (distance % 8);

        //change only the bit at the distance requested
        result[byteToUpdateIndex] = (byte)(result[byteToUpdateIndex] ^ 0x01<<bitToUpdateIndex);
        return new KademliaId(result);
    }

    /**
     * Counts the number of leading 0's in this NodeId
     *
     * @return Integer representing the number of leading 0's,
     * also returns -1 if there's only leading 0's
     * @author EdoardoRaimondi, Marco Cognolato
     */
    public int getFirstSetBitIndex() {
        for(int byteIndex = 0; byteIndex < keyBytes.length; byteIndex++) {
            byte currentByte = keyBytes[byteIndex];
            //8 bits in a byte, from 0 to 7
            for(int bitIndex = 7; bitIndex >= 0; bitIndex--) {
                if(((0x01 << bitIndex) & currentByte) == (0x01 << bitIndex)){
                    return (7-bitIndex) + byteIndex * 8;
                }
            }
        }
        return -1;
    }

    /**
     * Gets the bit distance from this NodeId to another NodeId
     * Given i index of the first set bit of the xor returned NodeId
     * The distance is ID_LENGTH - i
     *
     * @param to The node from which to calculate the distance
     * @return An integer from ID_LENGTH and 1 saying the first different bit.
     * if 0 is returned, it means the two id's are equal.
     */
    public int getDistance(KademliaId to) {
        int diffIndex = this.xor(to).getFirstSetBitIndex();
        if(diffIndex == -1) return 0;
        return ID_LENGTH - diffIndex;
    }

    /**
     * Returns the distance from this Id to a given Id in the xor metric system.
     * @param to The node to check the distance to
     * @return A BigInteger saying the distance between the two Ids in the metric xor system
     */
    public BigInteger getXorDistance(KademliaId to){
        //The xor distance is defined as the number returned
        // by the xor of the 2 numbers to compare
        return this.xor(to).getInt();
    }


    /**
     * @return The hex format of this NodeId
     */
    public String hexRepresentation() {
        BigInteger bi = new BigInteger(1, this.keyBytes);
        return String.format("%0" + (this.keyBytes.length << 1) + "X", bi);
    }

    /**
     * @return String representing the Node
     */
    @Override
    @NonNull
    public String toString() {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[keyBytes.length * 2];
        for (int j = 0; j < keyBytes.length; j++) {
            int v = keyBytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}