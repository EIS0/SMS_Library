package com.eis0.smslibrary;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Manager class of the library. It is the highest level, it communicates with external activities.
 *
 * @author Marco Cognolato
 * @author Matteo Carnelos
 */
public class SMSManager extends CommunicationManager<SMSMessage> {

    private static final String SENT_ACTION = "SMS_SENT";
    private static final String DELIVERED_ACTION = "SMS_DELIVERED";

    // Singleton Design Pattern
    private SMSManager() { }
    private static SMSManager instance = null;

    private SentMessageListener smsSentListener;
    private BroadcastReceiver onSend = null;
    private DeliveredMessageListener smsDeliveredListener;
    private BroadcastReceiver onDeliver = null;
    private PendingIntent sent;
    private PendingIntent delivered;

    /**
     * Returns an instance of SMSManager if none exist, otherwise the one instance already created
     * as per the Singleton Design Patter, gets also the context of the application for future use.
     *
     * @return Single instance of this class.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    public static SMSManager getInstance() {
        if(instance == null) instance = new SMSManager();
        return instance;
    }

    /**
     * Set the listener watching for incoming SMSMessages.
     *
     * @param listener The listener to wake up when a message is received.
     * @author Marco Cognolato
     */
    @Override
    public void setReceiveListener(ReceivedMessageListener<SMSMessage> listener) {
        SMSHandler.setReceiveListener(listener);
    }

    /**
     * Removes the listener of incoming messages.
     *
     * @author Marco Cognolato
     */
    @Override
    public void removeReceiveListener() {
        SMSHandler.removeReceiveListener();
    }

    /**
     * Sends a given valid message.
     *
     * @author Marco Cognolato
     */
    @Override
    public void sendMessage(SMSMessage message) {
        sendMessage(message, null, null, null);
    }

    /**
     * Sends a given valid message and sets a listener for message sent.
     *
     * @param message Valid message to send.
     * @param listener Listener watching for message sent event.
     * @param context Context of the application.
     * @author Marco Cognolato
     */
    public void sendMessage(SMSMessage message, SentMessageListener listener, Context context) {
        sendMessage(message, listener, null, context);
    }

    /**
     * Sends a given valid message and sets a listener for message delivery.
     *
     * @param message Valid message to send.
     * @param listener Listener watching for message delivered event.
     * @param context Context of the application.
     * @author Marco Cognolato
     */
    public void sendMessage(SMSMessage message, DeliveredMessageListener listener, Context context) {
        sendMessage(message, null, listener, context);
    }

    /**
     * Sends a given valid message and sets listeners for both message sent and delivered.
     *
     * @param message Valid message to send.
     * @param sendListener Listener watching for message sent event.
     * @param deliveredListener Listener watching for message delivered event.
     * @param context Context of the application.
     * @author Marco Cognolato
     */
    public void sendMessage(SMSMessage message,
                            SentMessageListener sendListener,
                            DeliveredMessageListener deliveredListener,
                            Context context) {
        if(sendListener != null) setSentIntent(message, context, sendListener);
        if(deliveredListener != null) setDeliveredIntent(message, context, deliveredListener);
        SMSHandler.sendMessage(message, sent, delivered);
    }

    /**
     * Sets the sent PendingIntent for a given message to send in a specific context.
     *
     * @param message The message to set the intent for.
     * @param cont The context used for the event listener.
     * @param listener the specific listener to link to the message.
     * @author Giovanni Velludo
     */
    private void setSentIntent(final SMSMessage message, final Context cont, SentMessageListener listener) {
        if(onSend != null) cont.unregisterReceiver(onSend);
        smsSentListener = listener;
        sent = PendingIntent.getBroadcast(cont, 0, new Intent(SENT_ACTION), 0);
        onSend = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                smsSentListener.onMessageSent(getResultCode(), message);
                cont.unregisterReceiver(onSend);
            }
        };
        cont.registerReceiver(onSend, new IntentFilter(SENT_ACTION));
    }

    /**
     * Sets the delivered PendingIntent for a given message to deliver in a specific context.
     *
     * @param message The message to set the intent for.
     * @param cont The context used for the event listener.
     * @param listener the specific listener to link to the message.
     * @author Giovanni Velludo
     */
    private void setDeliveredIntent(final SMSMessage message, final Context cont, DeliveredMessageListener listener) {
        if(onDeliver != null) cont.unregisterReceiver(onDeliver);
        smsDeliveredListener = listener;
        delivered = PendingIntent.getBroadcast(cont, 0, new Intent(DELIVERED_ACTION), 0);
        onDeliver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                smsDeliveredListener.onMessageDelivered(getResultCode(), message);
                cont.unregisterReceiver(onDeliver);
            }
        };
        cont.registerReceiver(onDeliver, new IntentFilter(DELIVERED_ACTION));
    }
}
