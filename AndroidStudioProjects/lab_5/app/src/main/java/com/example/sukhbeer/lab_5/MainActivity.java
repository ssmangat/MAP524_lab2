package com.example.sukhbeer.lab_5;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {
    String[] colourNames;
    String select = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colourNames = getResources().getStringArray(R.array.listArray);
        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_listview, colourNames);
        lv.setAdapter(aa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                String[] colourCodes;
                colourCodes = getResources().getStringArray(R.array.listValues);
                String selection = colourCodes[position];
                select = selection;
                //String col = "#".concat(selection);
                String col = "#".concat(selection.substring(2));// substring to remove 00
                RelativeLayout real = (RelativeLayout) findViewById(R.id.relativeLayout);
                real.setBackgroundColor(Color.parseColor(col));//set background color
            }
        });
        registerForContextMenu(lv);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Write colour to SDCard");
        menu.add(0, v.getId(), 0, "Read colour from SDCard");
    }

    public void Write(){
        if(select != " ") {

            if (select.length() == 8) {
                select = "#".concat(select.substring(2));
            }

            try {
                File dir= Environment.getExternalStorageDirectory();// get sdCard
                File file = new File(dir,"color.txt");// make file
                FileOutputStream fOut = new FileOutputStream(file);

                byte[] Bytes = select.getBytes();

                fOut.write(Bytes);//write the file
                fOut.flush();
                fOut.close();//clode the file
            }
            catch (IOException e) { //if something wrong exception occcur
                Log.e("Exception", "Written failed: " + e.toString());

            }
        }

    }

    // http://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android

    public void ReadFrom(View v){ // function for read

        String read = " ";

        try {
            FileInputStream input = new FileInputStream(new File("storage/sdcard/color.txt")); //get the file
            if ( input != null ) { // if file not empty
                InputStreamReader inputReader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputReader);
                String RString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (RString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(RString);
                }

                input.close();
                read = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) { // if file not read set to exception
            Log.e("Error", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Error", "Can not read file: " + e.toString());
        }
        RelativeLayout real2 = (RelativeLayout) findViewById(R.id.relativeLayout);

        if(read != " ") { // if file not empty
            real2.setBackgroundColor(Color.parseColor(read)); //set to background color
        }

        else { // default color
            String back = "#f0ffff";
            real2.setBackgroundColor(Color.parseColor(back)); //set to default color
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Write colour to SDCard") {
            Write();
        } else if (item.getTitle() == "Read colour from SDCard") {

        } else {
            return false;
        }
        return true;
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
