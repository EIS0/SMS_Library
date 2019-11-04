package com.eis0.library_demo;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test(expected=IllegalArgumentException.class)
    public void messageToNull_isNotSent() {
        SMSCore.sendMessage("", "sending a message", null, null);
        fail();
    }
    @Test(expected = IllegalArgumentException.class)
    public void invalidDestination(){
        SMSCore.sendMessage("111111111111111111", "", null, null);
        fail();
    }

    @Test //test to Handle message (case smsListener null)
    public void testArrayList(){
        boolean isEmpty = true;
        boolean result;
        SMSHandler.setSMSOnReceiveListener(null);
        result = SMSHandler.isPendingMessagesEmpty();
        assertEquals(isEmpty, result);

    }
}




