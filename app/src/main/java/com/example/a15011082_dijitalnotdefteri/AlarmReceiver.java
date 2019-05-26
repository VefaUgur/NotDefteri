package com.example.a15011082_dijitalnotdefteri;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("Message");
        NotificationHelper notificationHelper =new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(msg);
        notificationHelper.getManagger().notify(1,nb.build());
    }
}
