package com.example.tobbe.uoweme.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tobbe.uoweme.R;
import helper.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import helper.Validation;
//import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static boolean result = false;

    private SharedPreferences pref;
    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private TextView _resetPasswordLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ButterKnife.bind(this);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);
        _resetPasswordLink = (TextView) findViewById(R.id.link_reset_password);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterUserActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        _resetPasswordLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
    }

    public void login() {
        Log.d(TAG, "Login");
        String name = "";
        String token = "";
        String gravatarLink = "";
        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        if (!validate(email, password)) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Base_Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        ArrayList params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        ServerRequest serverRequest = new ServerRequest();
        JSONObject json = serverRequest.getJSON(serverRequest.serverAddress + "/login",params);
        if(json != null){
            try{
                String response = json.getString("response");
                result = json.getBoolean("res");
                if(result){
                    token = json.getString("token");
                    gravatarLink= json.getString("grav");
                    name = json.getString("name");
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            try{
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("name", name);
                edit.putString("token", token);
                edit.putString("email", email);
                edit.putString("grav", gravatarLink);
                edit.commit();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(result == true) {
                            onLoginSuccess();
                        }
                        else{
                            onLoginFailed();
                        }
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    private boolean validate(String email, String password) {
        boolean valid = true;

        //String email = _emailText.getText().toString();
        //String password = _passwordText.getText().toString();

        if(!Validation.validateEmail(email)) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if(!Validation.validatePassword(password)) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
