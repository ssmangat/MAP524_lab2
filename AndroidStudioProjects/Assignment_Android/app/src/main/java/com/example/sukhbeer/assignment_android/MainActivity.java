package com.example.sukhbeer.assignment_android;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    // button to show progress dialog
    Button btnShowProgress;

    // Progress Dialog
    private ProgressDialog pDialog;
    ImageView my_image;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    String unZipLocation = "/sdcard/";
    private Context context;
    private Dbhandler dbhandler;
    public SQLiteDatabase db;
    FileReader fr;
    // File url to download
    private static String file_url = "http://www20.statcan.gc.ca/tables-tableaux/cansim/csv/01760064-eng.zip";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // show progress bar button
        btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
        // Image view to show image after downloading
        my_image = (ImageView) findViewById(R.id.my_image);
        /**
         * Show Progress bar click event
         * */
        context = this;
        dbhandler = new Dbhandler(this);
        dbhandler.open();
        db = dbhandler.getWritableDatabase();
        btnShowProgress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // starting new Async Task
                new DownloadFileFromURL().execute(file_url);
            }
        });
    }

    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = new FileOutputStream("/sdcard/downloadedfile.zip");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {

            DownloadManager downloadManager = null;
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            try {
                //String in = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloaded.zip";
                String file = "/sdcard/downloadedfile.zip";
                Log.d("in",file);
                FileInputStream fs = new FileInputStream(file);
              //  + "/downloadedfile.zip";
               // InputStream input = openFileInput(String.valueOf(fReader));
                ZipInputStream zipin = new ZipInputStream(fs);
                Log.d("in",file);
                ZipEntry ze = null;
                while ((ze = zipin.getNextEntry()) != null) {
                    Log.v("Decompress", "Unzipping" + ze.getName());
                    if (ze.isDirectory()) {
                        directoryChecker(ze.getName());
                    } else {
                        FileOutputStream out = new FileOutputStream(unZipLocation + ze.getName());
                        for (int i = zipin.read(); i != -1; i = zipin.read()) {
                            out.write(i);
                        }
                        zipin.closeEntry();
                        out.close();
                    }
                }
                //zipin.close();
                ;
            } catch (Exception e) {
                Log.e("Decompress", "Unzip", e);
            }
            databaseHelper();
        }

        private void directoryChecker(String s) {
            File file = new File(unZipLocation + s);
            if (!(file.isDirectory())) {
                file.mkdirs();
            }
        }
    }

    private void databaseHelper() {

        try {
            String name = "currency.db";
            String path = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";

            File databaseFile = new File(path + name);
            Log.d("DB", "Created");
            if (!databaseFile.exists()) {
                databaseFile.mkdirs();
                Log.d("DB", "Created-1");
            }
            File destinationFile = new File(path + name);
            if (!destinationFile.exists()) {
                copyFile(getApplicationContext().getAssets().open(name), new FileOutputStream(path + name));
                Log.d("DB", "Created-2");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Check", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Check", e.toString());
        }
        Cursor cursor = db.rawQuery("select * from currency", null);
        if (cursor.getCount() < 1) {
            try {
                String filename = "01760064-eng.csv";
                fr = new FileReader("/sdcard/" + filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
          /*  String filename = "01760064-eng.csv";
            try {
                fr = new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/unzipped/" + filename);
                Log.d("FR", "Chak leya");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            String data = "";
            int i = 0;
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(fr);
            String tableName = "currency";
            String columns = "YEAR_MONTH , CANADA , OTHER , VECTOR , COORDINATE , VALUE ";
            String s1 = "INSERT INTO " + tableName + " (" + columns + ") values (";
            String s2 = ");";

            db.beginTransaction();
            try {
                String dump = bufferedReader.readLine();
                while ((data = bufferedReader.readLine()) != null) {
                    StringBuilder sb = new StringBuilder(s1);
                    //data = data.replaceAll("\" ",  " ");
                    //data = data.replaceAll("\"","");
                    String[] StringArray = data.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    StringArray[2] = StringArray[2].replaceAll("\"", " ");
                    //StringArray[4] = StringArray[4].replaceAll("\"", " ");
                    String[] array = StringArray[2].split(",");
                    Log.d(StringArray[0], "0");
                    Log.d(StringArray[1], "1");


                    Log.d(array[0], "a0");
                    //Log.d(array[1],"a1");
                    //Log.d(array[2],"a2");
                    Log.d(StringArray[3], "3");
                    Log.d(StringArray[4], "4");
                    Log.d(StringArray[5], "5");
                    //Log.d(StringArray[6],"6");
                    //Log.d(StringArray[7],"7");


                    //if (!(StringArray.length == 8)) {
                    /** String tmp = StringArray[4];
                     String tmp1 = StringArray[5];
                     String tmp2 = StringArray[6];
                     StringArray[4] = " average ";
                     StringArray[5] = tmp;
                     StringArray[6] = tmp1;
                     sb.append("'" + StringArray[0] + "', '" + StringArray[1] + "', '" + StringArray[2] + "', '" + StringArray[3] + "', '" + StringArray[4] + "', '" + StringArray[5] + "', '" + StringArray[6] + "', '" + tmp2 + "'");
                     i++;
                     //}else {*/
                    sb.append("'" + StringArray[0] + "', '" + StringArray[1] + "', '" + array[0] + "', '" + StringArray[3] + "', '" + StringArray[4] + "', '" + StringArray[5] + "'");
                    sb.append(s2);
                    db.execSQL(sb.toString());
                    i++;
                    Log.d("Done", String.valueOf(i));
                    // dbhandler.insert(StringArray[0],StringArray[1],StringArray[2],StringArray[3],StringArray[4],StringArray[5],StringArray[6],StringArray[7]);
                }
                Log.d("end[0]", "0");
            } catch (IOException e) {
                e.printStackTrace();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0)
            outputStream.write(buffer, 0, length);
        inputStream.close();
        outputStream.close();
    }
    }