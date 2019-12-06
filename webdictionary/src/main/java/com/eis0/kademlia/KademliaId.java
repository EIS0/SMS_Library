package com.eis0.kademlia;

import android.util.Log;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private static final String TAG = "KademliaId";
    final static int ID_LENGTH = 160;
    final static int ID_LENGTH_BYTES = ID_LENGTH / 8;
    private byte[] keyBytes;

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
     */
    public KademliaId(SMSPeer peer) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASHING_ALG);
            md.update(peer.getAddress().getBytes());
            keyBytes = Arrays.copyOfRange(md.digest(), 0, ID_LENGTH_BYTES);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, HASHING_ALG + " is not a valid hashing algorithm");
        }
    }

    /**
     * Construct the NodeId from a string
     *
     * @param data The user generated key string
     */
    public KademliaId(String data) {
        this.keyBytes = data.getBytes();
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
     * Load the NodeId from a DataInput stream
     *
     * @param in The stream from which to load the NodeId
     * @throws IOException If there's an error in the input stream
     */
    public KademliaId(DataInputStream in) throws IOException {
        this.fromStream(in);
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
     * @return The BigInteger representating the key
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
            return this.hashCode() == nid.hashCode();
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
     * @param distance in number of bits
     * @return NodeId The newly generated NodeId
     */
    public KademliaId generateNodeIdByDistance(int distance) {

        byte[] result = new byte[ID_LENGTH_BYTES];

        /* Since distance = ID_LENGTH - prefixLength, we need to fill that amount with 0's */
        int numByteZeroes = (ID_LENGTH - distance) / 8;
        int numBitZeroes = 8 - (distance % 8);

        /* Filling byte zeroes */
        for (int i = 0; i < numByteZeroes; i++) {
            result[i] = 0;
        }

        /* Filling bit zeroes */
        BitSet bits = new BitSet(8);
        bits.set(0, 8);

        for (int i = 0; i < numBitZeroes; i++) {
            /* Shift 1 zero into the start of the value */
            bits.clear(i);
        }
        bits.flip(0, 8);        // Flip the bits since they're in reverse order
        result[numByteZeroes] = bits.toByteArray()[0];

        /* Set the remaining bytes to Maximum value */
        for (int i = numByteZeroes + 1; i < result.length; i++) {
            result[i] = Byte.MAX_VALUE;
        }

        return this.xor(new KademliaId(result));
    }

    /**
     * Counts the number of leading 0's in this NodeId
     *
     * @return Integer representing the number of leading 0's,
     * also returns -1 if there's only leading 0's
     * @author Marco Cognolato
     */
    public int getFirstSetBitIndex() {
        int prefixLength = 0;
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
     * @return Integer The distance
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
     * Add the NodeId to the stream
     *
     * @param out Data to write
     * @throws IOException If there's an error in the input stream
     */
    public void toStream(DataOutputStream out) throws IOException {
        out.write(this.getBytes());
    }

    /**
     * Uses a DataInputStream to generate a NodeId.
     *
     * @param in Data to read
     * @throws IOException If there's an error in the input stream
     */
    public final void fromStream(DataInputStream in) throws IOException {
        byte[] input = new byte[ID_LENGTH_BYTES];
        in.readFully(input);
        this.keyBytes = input;
    }

    /**
     * @return The hex format of this NodeId
     */
    public String hexRepresentation() {
        BigInteger bi = new BigInteger(1, this.keyBytes);
        return String.format("%0" + (this.keyBytes.length << 1) + "X", bi);
    }

    /**
     * @return String representing the NodeId
     */
    @Override
    @NonNull
    public String toString() {
        return this.hexRepresentation();
    }

}