package com.eis0.sms_library;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SMSCore_Tests {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.eis0.sms_library", appContext.getPackageName());
    }

    @Test()
    public void messageToNumber_isSent() {
        //I expect this to work without any exception thrown
        SMSCore.sendMessage("5554", "sending a message", null, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void messageToNull_isNotSent() {
        SMSCore.sendMessage("", "sending a message", null, null);
        fail();
    }

    @Test(expected=IllegalArgumentException.class)
    public void noMessageToNumber_isNotSent() {
        SMSCore.sendMessage("5554", "", null, null);
        fail();
    }

    @Test(expected=IllegalArgumentException.class)
    public void noMessageToNoNumber_isNotSent() {
        SMSCore.sendMessage("", "", null, null);
        fail();
    }
}
