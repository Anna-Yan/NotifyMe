package com.example.nout.notifyme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by NOUT on 9/17/2017.
 */

//This class is for giving skipped notification after turning off and on the phone
public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){

            Log.d("We are in the Boot Receiverrrr","!!!!!");

            Intent restartService = new Intent(context, Restart_AlarmService.class);
            context.startService(restartService);


        }
    }
}
