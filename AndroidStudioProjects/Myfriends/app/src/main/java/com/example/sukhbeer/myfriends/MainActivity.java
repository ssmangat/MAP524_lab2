package com.example.sukhbeer.myfriends;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    Context context;
    MySqlHelper helper = new MySqlHelper(context);
    ListView listView;
    String table = "friendlist";
    private String TAG = "mangat";
    private String s="create table empty";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        Log.d(TAG, "start");
        Log.d(TAG, "start");
        Log.d(TAG, "db");
        context = getApplicationContext();
        String query = "select name from friendlist";
        helper = new MySqlHelper(context);
        helper.open(db);
        Cursor cursor = db.rawQuery(query, null);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_main, (List) cursor);
        listView.setAdapter(aa);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
