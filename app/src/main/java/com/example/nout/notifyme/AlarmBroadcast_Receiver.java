package com.example.nout.notifyme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by NOUT on 9/16/2017.
 */

public class AlarmBroadcast_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("We are in the Broadcast Receiver", "!!!");

        String extra_mActivity = intent.getExtras().getString("extra");
        Log.d("Extra from Activity ====== ", extra_mActivity);


        //create an intent to the Rington Service
        Intent ringtonService_intent = new Intent(context, RingtonService.class);

        //pass extra from mActivity to Rington Service
        ringtonService_intent.putExtra("extra",extra_mActivity);

        //start the Rington Service
        context.startService(ringtonService_intent);

    }
}
