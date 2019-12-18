package com.eis0.kademlianetwork;


import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.Contact;
import com.eis0.kademlia.SMSKademliaNode;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test by Marco Cognolato
 * Mock by Edoardo Raimondi
 */

public class ConnectionHandlerTest {
    private static final SMSPeer VALID_PEER1 = new SMSPeer("+395554");
    private static final SMSPeer VALID_PEER2 = new SMSPeer("+395556");
    private static final SMSKademliaNode VALID_NODE = new SMSKademliaNode(VALID_PEER2);
    private static final Contact VALID_CONTACT = new Contact(VALID_NODE);

    private KademliaNetwork instance;
    private KademliaListener mockListener = mock(KademliaListener.class);
    private Context mockContext = mock(Context.class);
    private SharedPreferences mockPref = mock(SharedPreferences.class);
    private SharedPreferences.Editor mockEditor = mock(SharedPreferences.Editor.class);
    private SmsManager mockSmsManager = mock(SmsManager.class);

    @Before
    public void setup(){
        instance = KademliaNetwork.getInstance();
        when(mockContext.getPackageName()).thenReturn("mock");
        when(mockContext.getSharedPreferences(eq("mock_preferences"),
                any(Integer.class))).thenReturn(mockPref);
        when(mockPref.edit()).thenReturn(mockEditor);
        when(mockEditor.commit()).thenReturn(true);

        instance.init(VALID_NODE, mockListener, mockContext);
    }

    @Test()
    public void correctRequest(){
        try{
            //this throws an error because of the send message mock missing.
            ConnectionHandler.acceptRequest(VALID_PEER2);
            fail();
        }
        catch(RuntimeException e){
            assertTrue(instance.isNodeInNetwork(VALID_NODE));
        }
    }
}
