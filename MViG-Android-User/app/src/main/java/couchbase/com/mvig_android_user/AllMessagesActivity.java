package couchbase.com.mvig_android_user;

import android.os.Bundle;
import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllMessagesActivity extends AppCompatActivity {

    private ListView listView;
    private DefaultHttpClient httpclient;
    private HttpResponse response;
    private BufferedReader in;
    private String email;
    private String id;
    private ArrayList<Message> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_messages);
        setTitle("Mesaj Geçmişi");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = getIntent().getStringExtra("mail");

        list = new ArrayList<Message>();
        getAllMessages(email);

        listView = (ListView) findViewById(R.id.list);
        final CustomAdapter adapter = new CustomAdapter(this,R.layout.list_item,list);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                          list.get(position).getMessag_receiver(), Toast.LENGTH_LONG)
                        .show();

            }
        });
    }

    private int getAllMessages(String email){
        String line = null;
        try {
            httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = null;
            website = new URI("http://www.mvig.duckdns.org/message/get-messages/?email="+email);
            request.setURI(website);
            response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = in.readLine();
            System.out.println(line);
            try {
                //    JSONObject object = new JSONObject(line);
                //JSONArray array =   object.getJSONArray("objects");
                JSONArray array = new JSONArray(line);
                int arraySize = array.length();
                if(arraySize != 0){
                    do {
                        JSONObject object2 = array.getJSONObject(arraySize-1);
                        JSONObject fields = object2.getJSONObject("fields");
                        String numara = fields.getString("destination_number");
                        String mesaj = fields.getString("message_content");

                        list.add(new Message(mesaj, numara));
                        arraySize--;
                    }while (arraySize!=0);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
