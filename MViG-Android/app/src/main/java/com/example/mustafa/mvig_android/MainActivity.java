package com.example.mustafa.mvig_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private Thread t1 = null;
    private ProgressDialog progress = null;
    private EditText receiver;
    private EditText message;
    private EditText pk;
    private int GLOBAL_PK = 0;
    int pk_value = 0;
    SmsManager manager;
    SharedPreferences sharedpreferences=null;
    private boolean girdim = false;

    private static final String TAG_FIELDS = "fields";
    private static final String TAG_MESSAGE = "message_content";
    private static final String TAG_DESTINATION = "destination_number";
    private static final String TAG_PK = "pk";
    public static final String MyPREFERENCES = "Prefs";
    public static final String Name = "GLOBAL_PK";

    public void loadingInIncrements() {
        progress = new ProgressDialog(this);
        progress.setMessage("Sending Message");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.show();
        final int totalProgressTime = 100;
        t1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    i = i * i + i * 2;
                }
                int jumpTime = 0;
                while (jumpTime < totalProgressTime) {
                    try {
                        Thread.sleep(200);
                        jumpTime += 5;
                        progress.setProgress(jumpTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                progress.dismiss();
            }
        };
        t1.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = SmsManager.getDefault();
        if(sharedpreferences == null)
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        GLOBAL_PK = sharedpreferences.getInt(Name, 0);
        /*SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(Name, 0);
        editor.commit();*/
        callAsynchronousTask();
        //JSONParse parse = new JSONParse();
        //parse.execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            receiver = (EditText) findViewById(R.id.edt_receiver);
            message = (EditText) findViewById(R.id.edt_message);
            pk = (EditText) findViewById(R.id.edt_pk);
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONArray json = jParser.getJSONFromUrl("http://mefedck.hol.es/");
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            GLOBAL_PK = sharedpreferences.getInt(Name, 0);
            System.out.println("GLOBAL PK: " + GLOBAL_PK);
            pDialog.dismiss();

            try {
                for (int i = GLOBAL_PK; i < json.length(); i++) {
                    // Getting JSON Array
                    girdim = true;
                    JSONObject main_object = json.getJSONObject(i);
                    JSONObject fields = main_object.getJSONObject(TAG_FIELDS);

                    // Storing  JSON item in a Variable
                    String message_content = fields.getString(TAG_MESSAGE);
                    String destination_number = fields.getString(TAG_DESTINATION);
                    pk_value = Integer.parseInt(main_object.getString(TAG_PK));

                    //Set JSON Data in TextView
                    message.setText(message_content);
                    receiver.setText(destination_number);
                    pk.setText("" + pk_value);
                    sendMessage(destination_number, message_content);

                }
                if(girdim) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt(Name, pk_value);
                    editor.commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            JSONParse performBackgroundTask = new JSONParse();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 1000 ms
    }

    public void sendMessage(String number, String content) {
        String no = number;
        String cntnt = content;
        manager.sendTextMessage(no, null, cntnt, null, null);
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

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("GLOBAL PK : "+GLOBAL_PK);
    }
}
