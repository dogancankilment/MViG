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

public class ActivationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit;
    private EditText edt_Activation;
    private boolean sonuc=false;
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
                    if(sonuc)
                        go_loginActivity();
                }
                return false;
            }
        });

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
                edit.remove("Email");
                edit.apply();
                return true;
            }
        }
        return false;
    }

}
