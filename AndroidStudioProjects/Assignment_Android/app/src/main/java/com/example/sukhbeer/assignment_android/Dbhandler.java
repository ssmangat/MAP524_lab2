package com.example.sukhbeer.assignment_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sukhbeer on 17/08/15.
 */
public class Dbhandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "currency.db";
    public static final String TABLE_NAME = "currency";
    public static final String YEAR_MONTH = "year_month";
    public static final String CANADA = "canada";
    public static final String OTHER = "other";
    public static final String PERIOD = "period";
    public static final String AVERAGE = "average";
    public static final String VECTOR = "vector";
    public static final String COORDINATE = "coordinate";
    public static final String VALUE = "value";
    private static final int DATABASE_VERSION =2;
    private static String DB_PATH;
    private SQLiteDatabase database = null;
    private final Context context;


    public Dbhandler(Context context) {
        super(context, DATABASE_NAME,null,1);
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.context = context;
        Log.d("database Path", DB_PATH);
//        Log.d("database Name",DB_NAME);


    }

    public void open(){
        database = getWritableDatabase();
        onCreate(database);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        db.execSQL("create table currency ( ID integer primary key autoincrement, YEAR_MONTH integer, CANADA text, OTHER text,PERIOD text, AVERAGE text, VECTOR VARCHAR, COORDINATE integer, VALUE  integer);");
        Log.d("Created0","currency");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }
}
