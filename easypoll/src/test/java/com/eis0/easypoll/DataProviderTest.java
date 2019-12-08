package com.eis0.easypoll;

import com.eis0.easypoll.poll.BinaryPoll;
import com.eis0.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Observer;

import static com.eis0.easypoll.BinaryPollTest.VALID_ID;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_1;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_2;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_3;
import static com.eis0.easypoll.BinaryPollTest.VALID_POLL_NAME;
import static com.eis0.easypoll.BinaryPollTest.VALID_POLL_QUESTION;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test class for DataProvider.
 *
 * @author Matteo Carnelos
 */
public class DataProviderTest {

    @Mock private Observer mockObserver = mock(Observer.class);
    private BinaryPoll incomingPoll;
    private BinaryPoll openedPoll;
    private BinaryPoll closedPoll;
    private DataProvider dataProvider = DataProvider.getInstance();

    /**
     * Setup for the test class, it adds the mocked observer and initializes three types of poll.
     *
     * @author Matteo Carnelos
     */
    @Before
    public void setup() {
        dataProvider.addObserver(mockObserver);
        incomingPoll = new BinaryPoll(VALID_PEER_1, VALID_ID, VALID_POLL_NAME, VALID_POLL_QUESTION);
        ArrayList<SMSPeer> testUsers = new ArrayList<>();
        testUsers.add(VALID_PEER_1);
        testUsers.add(VALID_PEER_2);
        testUsers.add(VALID_PEER_3);
        openedPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsers);
        closedPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsers);
        closedPoll.setNo(VALID_PEER_1);
        closedPoll.setYes(VALID_PEER_2);
        closedPoll.setNo(VALID_PEER_3);
    }

    /**
     * Verifies that an incoming poll is correctly added to the corresponding list and the observer
     * is notified.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void incomingPoll_isAdded() {
        dataProvider.onPollReceived(incomingPoll);
        assertTrue(DataProvider.getIncomingPolls().contains(incomingPoll));
        verify(mockObserver).update(dataProvider, incomingPoll);
    }

    /**
     * Verifies that an opened poll is correctly added to the corresponding list and the observer
     * is notified.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void openedPoll_isAdded() {
        dataProvider.onSentPollUpdate(openedPoll);
        assertTrue(DataProvider.getOpenedPolls().contains(openedPoll));
        verify(mockObserver).update(dataProvider, openedPoll);
    }

    /**
     * Verifies that a closed poll is correctly moved to the corresponding list and the observer
     * is notified.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void closedPoll_isMoved() {
        dataProvider.onSentPollUpdate(closedPoll);
        assertTrue(DataProvider.getClosedPolls().contains(closedPoll));
        verify(mockObserver).update(dataProvider, closedPoll);
    }
}
