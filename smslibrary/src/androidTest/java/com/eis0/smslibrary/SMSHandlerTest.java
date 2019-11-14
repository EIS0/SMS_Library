package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

public class SMSHandlerTest {

    @Test
    public void isPendingMessagesEmpty() {
            boolean result;
            SMSHandler.setReceiveListener(null);
            result = SMSHandler.isPendingMessagesEmpty();
            assertEquals(result, true);

        }
    }
