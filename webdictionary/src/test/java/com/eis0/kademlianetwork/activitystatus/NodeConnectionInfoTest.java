package com.eis0.kademlianetwork.activitystatus;

import com.eis0.kademlianetwork.KademliaNetwork;

import org.junit.Test;
import org.w3c.dom.Node;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

public class NodeConnectionInfoTest {

    NodeConnectionInfo test = new NodeConnectionInfo();

    @Test
    public void hasRespond() {
        test.setRespond(true); // 2 test in 1. Not so elegant but necessary
        assertTrue(test.hasRespond());
    }

    @Test
    public void hasPong() {
        test.setPong(true); //2 test in 1. Not to elegant but necessary
        assertTrue(test.hasPong());
    }

    @Test
    public void reset() {
        assertFalse(test.hasRespond());
        assertFalse(test.hasPong());
    }

    @Test
    public void run() {
        test.run();
        //an imaginary user responds after 5 secs
        Timer timer = new Timer();
        timer.schedule(new TimerTest(test), 5000);
    }

    private class TimerTest extends TimerTask{
        private final NodeConnectionInfo connectionInfo;

        TimerTest(NodeConnectionInfo connectionInfo){
            this.connectionInfo = connectionInfo;
        }

        @Override
        public void run() {
            //when an acknowledge is received this gets called
            connectionInfo.setRespond(true);
        }
    }

}