package com.example.nout.notifyme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button setAlarmButton;
    private EditText notifEditText;

    private AlarmManager alarmManager;
    private TimePicker alarmtimePicker;

    private DBhelper helper;
    private Calendar calendar;
    private Context context;
    private int hour;
    private int minute;
    private Long pickerTimeDB;
    private String min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        calendar = Calendar.getInstance();

        helper = new DBhelper(this);

        setAlarmButton = (Button) findViewById(R.id.alarmButton);
        notifEditText = (EditText) findViewById(R.id.notifEditText);
        alarmtimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        setAlarmButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.alarmButton) {
            //setting calendar instance with the time that we picked from timepicker
            if (Build.VERSION.SDK_INT < 23) {

                calendar.set(Calendar.HOUR_OF_DAY, alarmtimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmtimePicker.getCurrentMinute());
                hour = alarmtimePicker.getCurrentHour();
                minute = alarmtimePicker.getCurrentMinute();

            } else {

                calendar.set(Calendar.HOUR_OF_DAY, alarmtimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmtimePicker.getMinute());
                hour = alarmtimePicker.getHour();
                minute = alarmtimePicker.getMinute();
            }

            pickerTimeDB = calendar.getTimeInMillis();


            alarmIsOn();

        }
    }


    //This is main method for managing notification
    public void alarmIsOn() {

        manageNotification();

    }


    public void toastPickedTime(){

        min = String.valueOf(minute);
        if (minute < 10) {
            min = "0" + minute;
        }
        Toast.makeText(context, "Alarm is set to " + hour + ":" + min + " ☺ ", Toast.LENGTH_LONG).show();
    }

    public void manageNotification() {

        if (helper.getAllNotifications() == null && checkIfNotificationIsNotEmpty()) {

            addNotificationToDB();
            scheduleNotification();

            toastPickedTime();

        } else {

            //check out if the time is already exist in DB or not, and after that add it to DB
            if (checkIfNotificationIsNotEmpty()) {

                if (helper.searchByTime(pickerTimeDB) == null) {
                    addNotificationToDB();
                    scheduleNotification();
                    toastPickedTime();

                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    Date resultdate = new Date(pickerTimeDB);
                    String showTime = sdf.format(resultdate);

                    Toast.makeText(context, showTime + " This alarm is already setted, pick another!", Toast.LENGTH_LONG).show();

                }
            }
        }
    }


    //This is for checking if notification in user input is not empty, and after adding it to DB
    public boolean checkIfNotificationIsNotEmpty() {
        boolean notifIsNotEmpty = false;

        if (TextUtils.isEmpty(notifEditText.getText().toString()) == false) {

            notifIsNotEmpty = true;

        } else {
            Toast.makeText(context, "Fill the notification field!", Toast.LENGTH_SHORT).show();
        }
        return notifIsNotEmpty;
    }


    public void addNotificationToDB() {
        OneNotification notif = new OneNotification();

        notif.setMessage(notifEditText.getText().toString());
        notif.setTime(pickerTimeDB);
        notif.setCompleted(0);

        helper.addNotification(notif);
        Log.d("Alarm is added to DB succesfully","!!!!!!!");
        Log.d("DB","===="+helper.getAllNotifications());

    }


    private void scheduleNotification() {

        //create an intent to the AlarmBroadcast_Receiver class
        Intent notificationIntent = new Intent(this, AlarmBroadcast_Receiver.class);
        notificationIntent.putExtra("extra", notifEditText.getText().toString());
        notifEditText.setText("");

        //create a pending intent that delays intent until the specified calendar time
        final int _id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        //set alarm manager
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent);
    }
}
