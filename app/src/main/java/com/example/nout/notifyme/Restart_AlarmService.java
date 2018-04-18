package com.example.nout.notifyme;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Restart_AlarmService extends IntentService {

    private AlarmManager alarmManager;
    private String extraMessage;
    private DBhelper dbHelper;
    private  SimpleDateFormat sdf;
    private Date resultdate;


    public Restart_AlarmService() {
        super("Restart_AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Log.d("We are in the Restart Serviceee","!!!!!!!");

        dbHelper = new DBhelper(getApplicationContext());

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        extraMessage = "This is missed message example";

        sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");


        scheduleAlarms();


    }


    //Here will be scheduled past and future alarms
    private void scheduleAlarms() {

        //Getting notifications from DB, and check if they are past or future
        List<OneNotification> notifs = dbHelper.getAllNotifications();
        for (OneNotification note : notifs) {

            //skipped alarms
            if (note.getIsCompleted()==0  && (note.getTime() < System.currentTimeMillis()) ) {

                resultdate = new Date(note.getTime());
                String skippedTime=sdf.format(resultdate);

                extraMessage = note.getMessage()+" - "+skippedTime+" - " ;

                Intent notificationIntent = new Intent(this.getApplicationContext(), AlarmBroadcast_Receiver.class);
                notificationIntent.putExtra("extra", extraMessage);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent); //uxarkel not@ miangamic

                Log.d("Boot Receiverrrr", " alarm " + note.getId() + " was skipped: at "+skippedTime);

            }

            //future regular alarms
            if (note.getIsCompleted()==0 && (note.getTime() >= System.currentTimeMillis())) {

                extraMessage = note.getMessage();

                Intent notificationInt = new Intent(this.getApplicationContext(), AlarmBroadcast_Receiver.class);
                notificationInt.putExtra("extra", extraMessage);
                PendingIntent pendingInt = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), notificationInt, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.set(AlarmManager.RTC, note.getTime(), pendingInt);

                Log.d("Boot Receiverrrr", " alarm " + note.getId() + " is future");

            }
        }
    }


}

