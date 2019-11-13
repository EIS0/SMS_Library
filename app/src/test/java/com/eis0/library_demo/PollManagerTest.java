package com.eis0.library_demo;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

public class PollManagerTest {

    @Test
    public void createPoll() {
    }

    @Test
    public void answerPoll() {
    }

    @Test
    public void onMessageReceived() {
        private Context context = ApplicationProvider.getApplicationContext();
        PollManager pollManager = PollManager.getInstance(context);
        String message = "";
        pollManager.onMessageReceived(message);



    }
}