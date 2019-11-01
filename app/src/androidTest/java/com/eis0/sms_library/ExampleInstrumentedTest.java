package com.eis0.sms_library;

import android.app.PendingIntent;
import android.content.Context;
import android.provider.Telephony;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.ExpectedException;
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




