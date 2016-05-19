package couchbase.com.mvig_android_user;

import android.content.*;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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

public class RegisterActivity extends AppCompatActivity {


    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private SharedPreferences sharedPreferences=null;
    private SharedPreferences.Editor editor=null;
    private HttpResponse response = null;
    private HttpClient httpclient = null;
    private BufferedReader in = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences("activation_code",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        mConfirmPasswordView = (EditText) findViewById(R.id.confirmpassword);
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(mPasswordView.getText().toString())) {
                    if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
                        isPasswordConfirm(mPasswordView.getText().toString(), mConfirmPasswordView.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });

        Button registerButon = (Button) findViewById(R.id.registerButton);
        registerButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    private void attemptRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmpassword = mConfirmPasswordView.getText().toString();



        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!isPasswordConfirm(password, confirmpassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_not_equals_passwords));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        // Check for equals password with confirm password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmpassword)) {
            isPasswordConfirm(password, confirmpassword);
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        //web servise istek yapılıyor
        if (!verifyUser(email)){
            mEmailView.setError(getString(R.string.error_email_exist));
            focusView = mEmailView;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            //Kullanıcıyı database'e ekle
            boolean sonuc = addUserToDatabase(email, username, password);
            if(sonuc)
                Toast.makeText(this, "KAYIT BAŞARILI", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "KAYIT BAŞARISIZ", Toast.LENGTH_SHORT).show();

            //Activasyon kodu hazırlanıyor.
            ActivationCreater.createCode();
            String activitionCode = ActivationCreater.getCode();
            Log.i("Aktivasyon Kod", activitionCode);


            //Aktivasyon kodunu sharedpreferences mekanizmasında saklamak için
            editor.putString("Aktivasyon", activitionCode);
            editor.putString("Email", email);
            editor.commit();


            //Mail adresine kod gönderme işlemi başlatılıyor.
            MailSender sender = new MailSender();
            sender.execute(email, activitionCode);

            //Aktivasyon sayfasına geçiş
            Intent intent = new Intent(this, ActivationActivity.class);
            startActivity(intent);
            }

    }

    public boolean addUserToDatabase(String param1, String param2, String param3){
        String line = null;
        try {
            httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = null;
            website = new URI("http://www.mvig.duckdns.org/user/android/?username="+param2+"&email="+param1+"&password="+param3);
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


    //KUllanıcının email adresi daha önce kullanılmış mı?
    public boolean verifyUser(String mail){
        String line = null;
        StringBuilder textv=new StringBuilder();

        try {
            httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = null;

            website = new URI("http://www.mvig.duckdns.org/api/user/?format=json&email="+mail);
            request.setURI(website);
            response = httpclient.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = in.readLine();

            JSONObject obj = new JSONObject(line);

            JSONArray array =   obj.getJSONArray("objects");
            int a = array.length();
            if(a == 0){
                return true;
            }
            else
            {
                return false;
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }




    private void createNewUserAccount(String email, String username, String password) {
        User user = new User(email, username, password);
        //Toast.makeText(this, "Kullanıcı Eklendi", Toast.LENGTH_LONG).show();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    private boolean isPasswordConfirm(String password, String cPassword) {
        return password.equals(cPassword);
    }
}