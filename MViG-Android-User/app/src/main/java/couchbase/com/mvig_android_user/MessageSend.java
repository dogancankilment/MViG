package couchbase.com.mvig_android_user;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class MessageSend extends AppCompatActivity {

    private AutoCompleteTextView message_receiver;
    private AutoCompleteTextView message_body;
    private Button btn_send;

    private HttpResponse response = null;
    private HttpClient httpclient = null;
    private BufferedReader in = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_send);

        message_receiver = (AutoCompleteTextView) findViewById(R.id.message_receiver);
        message_body = (AutoCompleteTextView) findViewById(R.id.message_body);
        btn_send = (Button) findViewById(R.id.btn_sendMessage);



        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receiver = message_receiver.getText().toString();
                String message = message_body.getText().toString();
                message = message.replace("\n","");
                if(message_send(receiver, message))
                    basariMesajiGonder();
            }
        });
    }

    private void basariMesajiGonder() {
        Toast.makeText(this,"Mesajınız gönderilmiştir",Toast.LENGTH_SHORT).show();
        message_body.setText("");
        message_receiver.setText("");
    }

    private boolean message_send(String receiver, String message){
        String line = null;
        try {
            httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = null;
            website = new URI("http://www.mvig.duckdns.org/message/android/?number="+ receiver + "&content=" + message);
            request.setURI(website);
            response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = in.readLine();

            if(line.equals("basarili"))
                return true;
            else
                return false;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

}
