package com.glucose.arjunwatane.gold_v1;

import android.content.ContentValues;
import android.content.Context;
//import android.database.Cursor;
import net.sqlcipher.Cursor;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteDatabase;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Victor on 11/4/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int SCHEMA = 1;
    private static final String DATABASE_NAME = "users.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_INFO = "userinfo";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_INFOID = "infoID";
    private static final String COLUMN_USERID = "userID";
    private static final String COLUMN_USER = "user";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_GLUCOSE = "glucose_reading";
    private static final String COLUMN_TIMESTAMP = "time_of_reading";
    private SQLiteDatabase db;
    private Context context;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    private static final String TABLE_CREATE_USERS = "CREATE TABLE "+ TABLE_USERS + "(ID integer primary key not null, " +
            "user text not null, password text not null);";
    private static final String TABLE_CREATE_INFO = "CREATE TABLE "+ TABLE_INFO + "(infoID integer primary key not null," +
            "userID integer not null, user text not null, glucose_reading integer not null, time_of_reading datetime not null);";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, SCHEMA);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.loadLibs(context);
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_INFO);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query = "DROP TABLE IF EXISTS" + TABLE_USERS;
        String query2 = "DROP TABLE IF EXISTS" + TABLE_INFO;

        db.execSQL(query);
        db.execSQL(query2);

        this.onCreate(db);
    }

    public void insertUser(User u)
    {
        load();
        db = this.getWritableDatabase("test");
        ContentValues values = new ContentValues();

        String query = "SELECT * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_USER, u.getName());
        values.put(COLUMN_PASSWORD, u.getPassword());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public String searchPass(String username)
    {
        load();
        db = this.getReadableDatabase("test");
        String query = "SELECT user, password FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query,null);
        String user, pass;


        pass = randomString(15);
        if(cursor.moveToFirst())
        {
            do
            {
                user = cursor.getString(0);

                if(user.equals(username))
                {
                    pass = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        db.close();
        return pass;
    }

    public int searchID(String username, String pass)
    {
        load();
        db = this.getReadableDatabase("test");
        String query = "SELECT ID FROM " + TABLE_USERS +" where user = ? and password = ?";
        Cursor cursor = db.rawQuery(query, new String[] {username, pass});
        int ID;

        cursor.moveToFirst();

        ID = cursor.getInt(0);

        db.close();
        return ID;
    }

    public ArrayList<QuerySet> search(String username, int ID)
    {
     //   load();
        db = this.getReadableDatabase("test");
        String query = "SELECT glucose_reading, time_of_reading FROM " + TABLE_INFO +" WHERE user = ? AND userID = ? ORDER BY time_of_reading DESC";
        Cursor cursor = db.rawQuery(query, new String[] {username, String.valueOf(ID)});

        ArrayList<QuerySet> queryResults = new ArrayList<QuerySet>();


        if(cursor.moveToFirst())
        {
            do
            {
                QuerySet queryHolder = new QuerySet();
                queryHolder.glucoseReading = cursor.getString(0);
                queryHolder.timestamp = cursor.getString(1);
                queryResults.add(queryHolder);
            }
            while(cursor.moveToNext());
        }
        db.close();

        return queryResults;
    }

    public void insertLog(String username, int userID, int glucoseValue)
    {
        load();
        db = this.getWritableDatabase("test");
        ContentValues values = new ContentValues();

        String query = "SELECT * FROM " + TABLE_INFO;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_INFOID, count);
        values.put(COLUMN_USERID, userID);
        values.put(COLUMN_USER, username);
        values.put(COLUMN_GLUCOSE, glucoseValue);
        values.put(COLUMN_TIMESTAMP, getDateTime());

        db.insert(TABLE_INFO, null, values);
        db.close();
    }


    String randomString( int len )
    {
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public void load()
    {
        SQLiteDatabase.loadLibs(context);
    }

    public static class QuerySet
    {
        public String glucoseReading;
        public String timestamp;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}