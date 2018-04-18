package com.example.nout.notifyme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NOUT on 9/15/2017.
 */

public class DBhelper extends SQLiteOpenHelper {
    public static final int DBVERSION = 7;
    public static final String DBNAME = "notifDB";
    public static final String TBNAME = "notifTB";
    public static final String TIME = "time";
    public static final String MESSAGE = "message";
    public static final String STATUS = "status";
    public SQLiteDatabase db;


    public DBhelper(Context context) {

        super(context, DBNAME, null, DBVERSION);
    }


    public void addNotification(OneNotification notif) {
        ContentValues cv = new ContentValues();
        cv.put(TIME, notif.getTime());
        cv.put(MESSAGE, notif.getMessage());
        cv.put(STATUS, notif.getIsCompleted());
        db = getWritableDatabase();
        db.insert(TBNAME, null, cv);
    }


    public List<OneNotification> getAllNotifications() {
        List<OneNotification> list = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);


        while (cursor.moveToNext()) {
            OneNotification notif = new OneNotification();
            notif.setId(cursor.getInt(0));
            notif.setTime(cursor.getLong(1));
            notif.setMessage(cursor.getString(2));
            notif.setCompleted(cursor.getInt(3));

            Log.d("IDcolumn", String.valueOf(cursor.getInt(0)));
            Log.d("TIMEcolumn", String.valueOf(cursor.getLong(1)));
            Log.d("MESSAGEcolumn", cursor.getString(2));
            Log.d("STATUScolumn", String.valueOf(cursor.getInt(3)));

            list.add(notif);
        }

        cursor.close();
        return list;

    }

    //method for returning last created notification
    public OneNotification getLastNotific(){
        OneNotification not = new OneNotification();
        db = getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);

            cursor.moveToLast();
            not.setId(cursor.getInt(0));
            not.setTime(cursor.getLong(1));
            not.setMessage(cursor.getString(2));
            not.setCompleted(cursor.getInt(3));

            return not;
    }


    //This is for searching if the picked time by user is already exist in DB or not
    public Long searchByTime(Long time){

        db = getReadableDatabase();
        OneNotification notific = new OneNotification();
        String selection = "time = ?";
        String selectionArgs[] = {time.toString()};

        Cursor c = db.query(TBNAME, null, selection, selectionArgs, null, null, null);

        Long t;
        if(c.getCount() > 0){

            c.moveToFirst();
            notific.setTime(c.getLong(1));
            c.close();
            t= notific.getTime();
        } else {
            t= null;
        }
        return t;
    }

    //This is for changing notification status when its completed
    public void updateStatusByID(int id, int isCompleted) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STATUS, isCompleted);

        db.update(TBNAME, cv, "id = ?", new String[]{id + ""});

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTable = "create table " + TBNAME
                + "(id integer primary key autoincrement, "+ TIME +" long, "+ MESSAGE +"  text, "+ STATUS +" integer);";
        db.execSQL(queryCreateTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notifTB" );
        db.execSQL("create table " + TBNAME
                + "(id integer primary key autoincrement, "+ TIME +" long, "+ MESSAGE +"  text, "+ STATUS +" integer);");

    }
}
