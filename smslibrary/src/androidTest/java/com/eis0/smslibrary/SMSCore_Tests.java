package com.eis0.smslibrary;

import android.Manifest;
import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class SMSCore_Tests {

    @Rule public GrantPermissionRule sendSMSRule = GrantPermissionRule.grant(Manifest.permission.SEND_SMS);
    @Rule public GrantPermissionRule receiveSMSRule = GrantPermissionRule.grant(Manifest.permission.RECEIVE_SMS);
    @Rule public GrantPermissionRule readPhoneStateRule = GrantPermissionRule.grant(Manifest.permission.READ_PHONE_STATE);
    @Rule public GrantPermissionRule readSMSRule = GrantPermissionRule.grant(Manifest.permission.READ_SMS);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.eis0.smslibrary.test", appContext.getPackageName());
    }

    @Test()
    public void messageToNumber_isSent() {
        //I expect this to work without any exception thrown
        SMSPeer peerTest = new SMSPeer("3408140326");
        SMSMessage messageTest = new SMSMessage(peerTest, "ciao");
        SMSCore.sendMessage(messageTest, null, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void messageToNull_isNotSent() {
        SMSPeer peerTest = new SMSPeer("");
        SMSMessage messageTest = new SMSMessage(peerTest, "ciao");
        SMSCore.sendMessage( messageTest, null, null);
        fail();
    }

    @Test(expected=IllegalArgumentException.class)
    public void noMessageToNumber_isNotSent() {
        SMSPeer peerTest = new SMSPeer("3408140326");
        SMSMessage messageTest = new SMSMessage(peerTest, "");
        SMSCore.sendMessage( messageTest, null, null);
        fail();
    }

    @Test(expected=IllegalArgumentException.class)
    public void noMessageToNoNumber_isNotSent() {
        SMSPeer peerTest = new SMSPeer("");
        SMSMessage messageTest = new SMSMessage(peerTest, "");
        SMSCore.sendMessage(messageTest, null, null);
        fail();
    }
}
