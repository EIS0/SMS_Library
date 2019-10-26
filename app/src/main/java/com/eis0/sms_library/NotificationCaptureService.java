package com.eis0.sms_library;

import android.annotation.TargetApi;
import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;


@TargetApi(21)
public class NotificationCaptureService extends NotificationListenerService {
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        // you can get notification info here

        // you can cancel the notification in this way
        cancelNotification(sbn.getKey());
    }
}
