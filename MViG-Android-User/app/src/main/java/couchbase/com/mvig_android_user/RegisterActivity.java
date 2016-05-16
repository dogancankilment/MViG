package couchbase.com.mvig_android_user;

import android.content.*;
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

public class RegisterActivity extends AppCompatActivity {


    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor editor=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


        if (cancel) {
            focusView.requestFocus();
        } else {
            createNewUserAccount(email, mUsernameView.getText().toString(), password);
            MailSender sender = new MailSender();

            //Kullanıcının Girdiği Mail adresi parametre olarak göndermek üzere bir değişkene atılıyor.
            String userMailAdress = mEmailView.getText().toString();

            //Activasyon kodu hazırlanıyor.
            ActivationCreater.createCode();
            String activitionCode = ActivationCreater.getCode();
            Log.i("Aktivasyon Kod", activitionCode);

            //Aktivasyon kodunu sharedpreferences mekanizmasında saklamak için
            editor.putString("Aktivasyon", activitionCode);
            editor.putString("Email", userMailAdress);
            editor.commit();

            //Mail adresine kod gönderme işlemi başlatılıyor.
            sender.execute(userMailAdress, activitionCode);

            //Aktivasyon sayfasına geçiş
            Intent intent = new Intent(this, ActivationActivity.class);
            startActivity(intent);
        }
    }

    private void createNewUserAccount(String email, String username, String password) {
        User user = new User(email, username, password);
        Toast.makeText(this, "Kullanıcı Eklendi", Toast.LENGTH_LONG).show();
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