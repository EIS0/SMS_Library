package com.eis0.sms_library;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class SMSSendMessageTest {
    @Test
        (expected = IllegalArgumentException.class)
     SMSSendMessage(11111111111111111111,"cioao",0,0);

    }
}