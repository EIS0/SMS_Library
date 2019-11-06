package com.eis0.smslibrary;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SMSManager extends CommunicationHandler<SMSMessage> {
    // Singleton Design Pattern
    private SMSManager() { }
    private static SMSManager instance = null;
    private Context context;

    /**
     * Returns an instance of SMSManager if none exist, otherwise the one instance already created
     * as per the Singleton Design Patter, gets also the context of the application for future use
     * @param context Context of the application to use when needed
     * @return Single instance of this class
     */
    public static SMSManager getInstance(Context context) {
        if(instance == null) instance = new SMSManager();
        instance.context = context;
        return instance;
    }

    private SentMessageListener smsSentListener;
    private BroadcastReceiver onSend = null;
    private DeliveredMessageListener smsDeliveredListener;
    private BroadcastReceiver onDeliver = null;
    private PendingIntent sent;
    private PendingIntent delivered;

    /**
     * Adds the listener watching for incoming SMSMessages
     * @param listener The listener to wake up when a message is received
     */
    public void addReceiveListener(ReceivedMessageListener<SMSMessage> listener) {
        SMSHandler.setReceiveListener(listener);
    }

    /**
     * Removes the listener of incoming messages
     */
    public void removeReceiveListener() {
        SMSHandler.removeReceiveListener();
    }

    /**
     * Sends a given valid message
     */
    public void sendMessage(SMSMessage message) {
        SMSHandler.sendMessage(message, sent, delivered);
    }

    /**
     * Sends a given valid message and sets a listener for message sent
     * @param message Valid message to send
     * @param listener Listener watching for message sent event
     */
    public void sendMessage(SMSMessage message, SentMessageListener listener) {
        setSentIntent(message, context, listener);
    }

    /**
     * Sends a given valid message and sets a listener for message delivery
     * @param message Valid message to send
     * @param listener Listener watching for message delivered event
     */
    public void sendMessage(SMSMessage message, DeliveredMessageListener listener) {
        setDeliveredIntent(message, context, listener);
    }

    /**
     * Sends a given valid message and sets listeners for both message sent and delivered
     * @param message Valid message to send
     * @param sendListener Listener watching for message sent event
     * @param deliveredListener Listener watching for message delivered event
     */
    public void sendMessage(SMSMessage message,
                            SentMessageListener sendListener,
                            DeliveredMessageListener deliveredListener) {
        setSentIntent(message, context, sendListener);
        setDeliveredIntent(message, context, deliveredListener);
    }

    /**
     * Sets the sent PendingIntent for a given message to send in a specific context
     * @param message The message to set the intent for
     * @param cont The context used for the event listener
     * @param listener the specific listener to link to the message
     */
    private void setSentIntent(final SMSMessage message, final Context cont, SentMessageListener listener) {
        if(onSend != null) context.unregisterReceiver(onSend);
        smsSentListener = listener;
        String action = "SMS_SENT";
        sent = PendingIntent.getBroadcast(context, 0, new Intent(action), 0);
        onSend = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                smsSentListener.onMessageSent(getResultCode(), message);
                cont.unregisterReceiver(onSend);
            }
        };
        context.registerReceiver(onSend, new IntentFilter(action));
    }

    /**
     * Sets the delivered PendingIntent for a given message to deliver in a specific context
     * @param message The message to set the intent for
     * @param cont The context used for the event listener
     * @param listener the specific listener to link to the message
     */
    private void setDeliveredIntent(final SMSMessage message, final Context cont, DeliveredMessageListener listener) {
        if(onDeliver != null) context.unregisterReceiver(onDeliver);
        smsDeliveredListener = listener;
        String action = "SMS_DELIVERED";
        delivered = PendingIntent.getBroadcast(context, 0, new Intent(action), 0);
        onDeliver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                smsDeliveredListener.onMessageDelivered(getResultCode(), message);
                cont.unregisterReceiver(onDeliver);
            }
        };
        context.registerReceiver(onDeliver, new IntentFilter(action));
    }
}
