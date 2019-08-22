package com.example.searsnotes.reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.example.searsnotes.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID=3;
    public static final String CHANNEL_ID="channel";
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

         notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        myNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID);
        builder.setContentTitle("Test");
        builder.setSmallIcon(R.drawable.ic_note_add_black_24dp);
        builder.setContentText("message");
        builder.setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID,builder.build());
      /*  Uri notification_ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(context,notification_ring);
        ringtone.play();*/
    }

    private void myNotificationChannel() {
        if (Build.VERSION.SDK_INT>=26) //(Build.VERSION.SDK_INT>=build.VERSION_CODES.O)
        {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"My Channel",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("My Channel Description");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
