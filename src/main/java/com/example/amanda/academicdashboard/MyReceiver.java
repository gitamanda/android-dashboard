package com.example.amanda.academicdashboard;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

    static int notificationID;
    String channelID = "test";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Notification n = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Course/Assessment Date Notification")
                .setContentTitle("Academic Dashboard Notification").build();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++,n);

    }



}
