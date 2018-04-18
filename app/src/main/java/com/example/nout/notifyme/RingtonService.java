package com.example.nout.notifyme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import java.util.List;


public class RingtonService extends Service {
    private MediaPlayer mediaPlayer;
    private DBhelper helper;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("We are in the RingtonSevice","onStartCommand");
        helper = new DBhelper(getApplicationContext());

        //fetch extra String value from AlarmBroadCast_Receiver
        String user_message = intent.getExtras().getString("extra");

        //set up the Notification Service
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //create intent and pending intent that goes to main Activity
        Intent intent_mActivity = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent_mActivity = PendingIntent.getActivity(this, 0, intent_mActivity, 0);


        NotificationCompat.Builder popup_notification =  new NotificationCompat.Builder(this)
                .setContentTitle(" -Notify Me- ")
                .setContentText(user_message)
                .setSubText("• Created by Anna Zaqaryan •")
                .setSmallIcon(R.mipmap.notify_icon)
                .setContentIntent(pendingIntent_mActivity)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(user_message)); //this is for long messages


        //stex  tarber arjeqner em talis 1 in ,vor notificner@ irar tak beri voch te jnji naxord@
        notificationManager.notify((int)(Math.random() * 100) , popup_notification.build());

        //creating mediaPlayer for Notification
        mediaPlayer =  MediaPlayer.create(this, R.raw.symphony);
        mediaPlayer.start();


        // here we updateing notification status in DB,changing it to 1,so we know it is completed
        List<OneNotification> notifs = helper.getAllNotifications();
        for( OneNotification note : notifs) {

                helper.updateStatusByID(note.getId(),1);

                Log.d("Rington Serviceee"," alarm " +note.getId()+ " IsCompleted");

        }

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
       Log.d("RINGTON SERVICE IS DESTROYEEEED!!!","");
    }
}
