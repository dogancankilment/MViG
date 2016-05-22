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

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
    private DefaultHttpClient httpclient;
    private HttpResponse response;
    private BufferedReader in;

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


    public boolean getAllMessagesFromDB(){
        String line = null;
        try {
            httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = null;
            website = new URI("http://www.mvig.duckdns.org/message-api/message/?format=json");
            request.setURI(website);
            response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = in.readLine();

            JSONObject obj = new JSONObject(line);

            JSONArray array = obj.getJSONArray("objects");
            int a = array.length();
            while(a!=0) {
                JSONObject object = array.getJSONObject(a-1);
                String receiver = object.getString("destination_number");
                String message = object.getString("message_content");
                int id = object.getInt("id");
                boolean message_check = object.getBoolean("message_check");
                if(!message_check) {
                    sendMessage(receiver, message);
                    do_true(id);
                }
                a--;
        }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean do_true(int id){
        String line=null;
        httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        URI website = null;
        try {
            website = new URI("http://www.mvig.duckdns.org/message/check/?id="+id);
            request.setURI(website);
            response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = in.readLine();

            if(line.equals("ok")){
                return true;
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
                            getAllMessagesFromDB();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 2000); //execute in every 3000 ms
    }

    public void sendMessage(String number, String content) {
        if (!number.isEmpty() && !content.isEmpty() || number != null && content != null) {
            String no = number;
            String cntnt = content;
            manager.sendTextMessage(no, null, cntnt, null, null);
        }

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
