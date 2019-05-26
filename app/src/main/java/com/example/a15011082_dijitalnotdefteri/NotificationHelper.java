package com.example.a15011082_dijitalnotdefteri;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHelper extends ContextWrapper {
    public  static  final String channel1ID = "channel1ID";
    public  static  final String channel1name = "channel 1";

    private NotificationManager mManager;


    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel channel1 = new NotificationChannel(channel1ID,channel1name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManagger().createNotificationChannel(channel1);
    }

    public NotificationManager getManagger(){
        if (mManager== null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String message){
        Intent resultinten  =new Intent (this,MainActivity.class);
        PendingIntent pndIntent = PendingIntent.getActivity(this,1,resultinten,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = { 0, 100, 200, 300 };
        return new NotificationCompat.Builder(getApplicationContext(),channel1ID)
                .setContentTitle("Not Hatırlatıcı")
                .setContentText(message)
                .setColor(0xFFFF8800)
                .setSmallIcon(R.drawable.ic_1)
                .setSound(alarmSound)
                .setVibrate(vibrate)
                .setAutoCancel(true)
                .setContentIntent(pndIntent);



    }
}
