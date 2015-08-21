package com.example.sukhbeer.myfriends;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TableRow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sukhbeer on 10/08/15.
 */
public class MySqlHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "friendlist";
    public static final String NAME = "name";
    public static final String ADDRESS = "Address";
    public static final String PHONE = "phone";
    public static final String FRIENDID = "id";
    public static final String DATABASE_NAME = "friends.db";
    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table" + DB_NAME + "(" + NAME + "text not null," + ADDRESS + "text not null," + PHONE + "integer" + FRIENDID + "integer primary key autoincrement);";
    public SQLiteDatabase db;
    public MySqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySqlHelper.class.getName(),"Upgrading database from version" + oldVersion + " to " + newVersion + ", which will destroy all data");
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    public SQLiteDatabase open(SQLiteDatabase db) {
        db = getWritableDatabase();
        return db;
    }


    /**public SQLiteDatabase getWritableDatabase(){
        Context context = null;
        String mCSVfile = "Data1.csv";
        AssetManager manager = context.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");
                if (colums.length != 4) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues cv = new ContentValues(3);
                cv.put(NAME, colums[0].trim());
                cv.put(ADDRESS, colums[1].trim());
                cv.put(PHONE, colums[2].trim());
                db.insert(DB_NAME, null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return db;
    }*/
}
