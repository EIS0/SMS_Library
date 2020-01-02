package com.eis0.easypoll;

import com.eis0.easypoll.poll.BinaryPoll;
import com.eis0.networklibrary.Network;
import com.eis0.networklibrary.NetworksPool;
import com.eis0.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import static com.eis0.easypoll.BinaryPollTest.VALID_ID;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_1;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_2;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_3;
import static com.eis0.easypoll.BinaryPollTest.VALID_POLL_NAME;
import static com.eis0.easypoll.BinaryPollTest.VALID_POLL_QUESTION;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link DataProvider}.
 *
 * @author Matteo Carnelos
 */
public class DataProviderTest {

    @Mock private Observer mockObserver = mock(Observer.class);
    private BinaryPoll incomingPoll;
    private BinaryPoll openedPoll;
    private BinaryPoll closedPoll;
    private DataProvider dataProvider = DataProvider.getInstance();

    @Before
    public void setup() {
        Network testAuthorsNetwork = NetworksPool.obtainNetwork(VALID_PEER_1);
        List<SMSPeer> testUsers = new ArrayList<>();
        testUsers.add(VALID_PEER_1);
        testUsers.add(VALID_PEER_2);
        testUsers.add(VALID_PEER_3);
        Network testUsersNetwork = NetworksPool.obtainNetwork(testUsers);
        dataProvider.addObserver(mockObserver);
        incomingPoll = new BinaryPoll(VALID_ID, VALID_POLL_NAME, VALID_POLL_QUESTION, testAuthorsNetwork, testUsersNetwork);
        openedPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsersNetwork);
        closedPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsersNetwork);
        closedPoll.setAnswer(false);
        closedPoll.setAnswer(true);
        closedPoll.setAnswer(false);
    }

    @Test
    public void incomingPoll_isAdded() {
        dataProvider.addPoll(incomingPoll);
        assertTrue(DataProvider.getIncomingPolls().contains(incomingPoll));
        verify(mockObserver).update(dataProvider, incomingPoll);
    }

    @Test
    public void openedPoll_isAdded() {
        dataProvider.addPoll(openedPoll);
        assertTrue(DataProvider.getOpenedPolls().contains(openedPoll));
        verify(mockObserver).update(dataProvider, openedPoll);
    }

    @Test
    public void closedPoll_isMoved() {
        dataProvider.updatePoll(closedPoll);
        assertTrue(DataProvider.getClosedPolls().contains(closedPoll));
        verify(mockObserver).update(dataProvider, closedPoll);
    }
}
