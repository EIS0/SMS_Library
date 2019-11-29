package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for SMSPeer.
 *
 * @author Matteo Carnelos
 */
public class SMSPeerTest {

    static final String VALID_ADDR = "3401234567";
    private static final String VALID_ADDR_WITH_PREFIX = "+39" + VALID_ADDR;
    private static final String VALID_ADDR_WITH_SPACES = "340 1234567";
    private static final String VALID_ADDR_WITH_PREFIX_AND_SPACES = "+39 " + VALID_ADDR_WITH_SPACES;
    private static final String VALID_ADDR_WITH_DASHES = "340-1234567";
    private static final String VALID_ADDR_WITH_PREFIX_AND_DASHES = "+39-" + VALID_ADDR_WITH_DASHES;

    // Test for simulator special types of phone numbers
    private static final String SIMULATOR_ADDR = "5556";
    private static final String SIMULATOR_ADDR_WITH_EXT = "555521" + SIMULATOR_ADDR;
    private static final String SIMULATOR_ADDR_WITH_PREFIX_AND_EXT = "+1" + SIMULATOR_ADDR_WITH_EXT;

    private static final String EMPTY_ADDR = "";
    private static final String LONG_ADDR = "3401234567901234";
    private static final String INVALID_ADDR = "String with 3401234567 inside";

    /**
     * Checks that a typical valid peer without prefix is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPeer_isCreated() {
        SMSPeer testPeer = new SMSPeer(VALID_ADDR);
        assertEquals(VALID_ADDR, testPeer.getAddress());
    }

    /**
     * Checks that a valid peer with prefix is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPeerWithPrefix_isCreated() {
        SMSPeer testPeer = new SMSPeer(VALID_ADDR_WITH_PREFIX);
        assertEquals(VALID_ADDR, testPeer.getAddress());
    }

    /**
     * Checks that a valid peer without prefix and with spaces between digits is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPeerWithSpaces_isCreated() {
        SMSPeer testPeer = new SMSPeer(VALID_ADDR_WITH_SPACES);
        assertEquals(VALID_ADDR, testPeer.getAddress());
    }

    /**
     * Checks that a valid peer with prefix and spaces between digits is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPeerWithPrefixAndSpaces_isCreated() {
        SMSPeer testPeer = new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_SPACES);
        assertEquals(VALID_ADDR, testPeer.getAddress());
    }

    /**
     * Checks that a valid peer without prefix and with dashes between digits is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPeerWithDashes_isCreated() {
        SMSPeer testPeer = new SMSPeer(VALID_ADDR_WITH_DASHES);
        assertEquals(VALID_ADDR, testPeer.getAddress());
    }

    /**
     * Checks that a valid peer with prefix and dashes between digits is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPeerWithPrefixAndDashes_isCreated() {
        SMSPeer testPeer = new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_DASHES);
        assertEquals(VALID_ADDR, testPeer.getAddress());
    }

    /**
     * Tests if the creation of an empty peer fails.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = IllegalArgumentException.class)
    public void emptyPeer_isNotCreated() {
        new SMSPeer(EMPTY_ADDR);
        fail();
    }

    /**
     * Tests if the creation of a long peer fails.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = IllegalArgumentException.class)
    public void longPeer_isNotCreated() {
        new SMSPeer(LONG_ADDR);
        fail();
    }

    /**
     * Tests if the creation of an invalid peer fails.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = IllegalArgumentException.class)
    public void invalidPeer_isNotCreated() {
        new SMSPeer(INVALID_ADDR);
        fail();
    }

    /**
     * Checks that a simulator number without prefix and extension is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void simulatorPeer_isCreated() {
        SMSPeer testPeer = new SMSPeer(SIMULATOR_ADDR);
        assertEquals(SIMULATOR_ADDR_WITH_EXT, testPeer.getAddress());
    }

    /**
     * Checks that a simulator number (without prefix) with extension is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void simulatorPeerWithExtension_isCreated() {
        SMSPeer testPeer = new SMSPeer(SIMULATOR_ADDR_WITH_EXT);
        assertEquals(SIMULATOR_ADDR_WITH_EXT, testPeer.getAddress());
    }

    /**
     * Checks that a simulator number with prefix and extension is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void simulatorPeerWithPrefixAndExtension_isCreated() {
        SMSPeer testPeer = new SMSPeer(SIMULATOR_ADDR_WITH_PREFIX_AND_EXT);
        assertEquals(SIMULATOR_ADDR_WITH_EXT, testPeer.getAddress());
    }

    /**
     * Checks that all the valid peers are equal.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPeers_areEqual() {
        assertEquals(new SMSPeer(VALID_ADDR), new SMSPeer(VALID_ADDR_WITH_PREFIX));
        assertEquals(new SMSPeer(VALID_ADDR_WITH_SPACES), new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_SPACES));
        assertEquals(new SMSPeer(VALID_ADDR_WITH_DASHES), new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_DASHES));
        assertEquals(new SMSPeer(SIMULATOR_ADDR), new SMSPeer(SIMULATOR_ADDR_WITH_EXT));
        assertEquals(new SMSPeer(SIMULATOR_ADDR), new SMSPeer(SIMULATOR_ADDR_WITH_PREFIX_AND_EXT));
    }

    /**
     * Checks if all the hashes created from valid peers are equal.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPeersHashes_areEqual() {
        assertEquals(new SMSPeer(VALID_ADDR).hashCode(), new SMSPeer(VALID_ADDR_WITH_PREFIX).hashCode());
        assertEquals(new SMSPeer(VALID_ADDR_WITH_SPACES).hashCode(), new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_SPACES).hashCode());
        assertEquals(new SMSPeer(VALID_ADDR_WITH_DASHES).hashCode(), new SMSPeer(VALID_ADDR_WITH_PREFIX_AND_DASHES).hashCode());
        assertEquals(new SMSPeer(SIMULATOR_ADDR).hashCode(), new SMSPeer(SIMULATOR_ADDR_WITH_EXT).hashCode());
        assertEquals(new SMSPeer(SIMULATOR_ADDR).hashCode(), new SMSPeer(SIMULATOR_ADDR_WITH_PREFIX_AND_EXT).hashCode());
    }
}
