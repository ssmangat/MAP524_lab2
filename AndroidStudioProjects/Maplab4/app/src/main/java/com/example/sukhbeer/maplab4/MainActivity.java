package com.example.sukhbeer.maplab4;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity {
    private static int s = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void switchOnClick(View view) {
        if (s == 0) {
            String ImageName = "image2";
            int res = getResources().getIdentifier(ImageName, "drawable", getPackageName());
            ImageView iv1 = (ImageView) findViewById(R.id.imageView);
            iv1.setImageResource(res);
            String ImageName1 = "image1";
            int res1 = getResources().getIdentifier(ImageName1, "drawable", getPackageName());
            ImageView iv2 = (ImageView) findViewById(R.id.imageView2);
            iv2.setImageResource(res1);
            s = 1;
        }
        else{
            String ImageName = "image1";
            int res = getResources().getIdentifier(ImageName, "drawable", getPackageName());
            ImageView iv1 = (ImageView) findViewById(R.id.imageView);
            iv1.setImageResource(res);
            String ImageName1 = "image2";
            int res1 = getResources().getIdentifier(ImageName1, "drawable", getPackageName());
            ImageView iv2 = (ImageView) findViewById(R.id.imageView2);
            iv2.setImageResource(res1);
            s = 0;
        }
    }
}
