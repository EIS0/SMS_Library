package com.eis0.library_demo;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class SMSHandler_Tests {

    @Test(expected = IllegalArgumentException.class)
    public void numberTooLarge_isNotSent() {
        SMSHandler.SMSSendMessage("1111111111111111111111111","cioao",null,null);
        fail();
    }
}
