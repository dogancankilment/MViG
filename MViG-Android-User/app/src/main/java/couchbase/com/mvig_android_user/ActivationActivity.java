package couchbase.com.mvig_android_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.content.Context;
import android.view.inputmethod.CompletionInfo;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class ActivationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit;
    private EditText edt_Activation;
    private boolean sonuc=false;
    private DefaultHttpClient httpclient;
    private HttpResponse response;
    private BufferedReader in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        edt_Activation = (EditText) findViewById(R.id.edt_activation_code);

        sharedPreferences = getSharedPreferences("activation_code",Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        edt_Activation.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (edt_Activation.getText().toString().length() == 6) {
                    sonuc = verify_activate_code();
                    if(sonuc){
                        if(do_Is_verified_true())
                            go_loginActivity();
                    }
                }
                return false;
            }
        });

    }

    private boolean do_Is_verified_true() {
        String line = null;
        String email = sharedPreferences.getString("Email","Boş");
        if(!email.equals("Boş")) {
            try {
                httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                URI website = null;
                website = new URI("http://www.mvig.duckdns.org/user/verify/android/?email="+email);
                request.setURI(website);
                response = httpclient.execute(request);
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                line = in.readLine();

                if (line.equals("ok")) {
                    edit.remove("Email");
                    edit.remove("Aktivasyon");
                    edit.apply();
                    return true;
                }
                else
                    return false;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this,"Email gelmedi",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void go_loginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public boolean verify_activate_code(){
        String str_aktivasyonKodu = sharedPreferences.getString("Aktivasyon","Boş");
        String girilen_aktivasyon_kodu = edt_Activation.getText().toString();
        if(!str_aktivasyonKodu.equals("Boş")){
            int aktivasyonKodu = Integer.parseInt(str_aktivasyonKodu);
            int kontrol = Integer.parseInt(girilen_aktivasyon_kodu);
            if(aktivasyonKodu == kontrol){
                Toast.makeText(this.getApplicationContext(),"Hesabınız aktifleştirildi",Toast.LENGTH_LONG ).show();
                return true;
            }
        }
        return false;
    }

}
