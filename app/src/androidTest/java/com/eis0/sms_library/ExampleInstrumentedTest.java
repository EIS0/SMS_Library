package com.eis0.sms_library;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

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
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.eis0.sms_library", appContext.getPackageName());
    }

    /*
    @Test
    public void sendSMS() {
        SmsManager smgr = SmsManager.getDefault();
        smgr.sendTextMessage("+393423541601", null, "Ses", null, null);
    }

    @Test
    public void messageReception() {
        TestReceiveMethod tm1 = new TestReceiveMethod();
        TestReceiveMethod tm2 = new TestReceiveMethod();
        SMSLib receiver = new SMSLib();
        receiver.addOnReceiveListener(tm1);
        receiver.addOnReceiveListener(tm2);
    }
     */
}