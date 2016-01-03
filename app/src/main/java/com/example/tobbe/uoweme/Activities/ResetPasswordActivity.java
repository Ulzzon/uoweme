package com.example.tobbe.uoweme.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tobbe.uoweme.R;
import com.example.tobbe.uoweme.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import helper.Validation;

/**
 * Created by TobiasOlsson on 15-12-29.
 */
public class ResetPasswordActivity extends Activity{
    private static String TAG = "ResetPasswordActivity";
    private TextView _emailText;
    private TextView _resetCodeText;
    private TextView _newPassword;
    private Button _resetButton;
    private Button _cancelButton;
    private Button _confirmButton;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        _emailText = (TextView) findViewById(R.id.emailAddressText);
        _resetButton = (Button) findViewById(R.id.resetPasswordButton);
        _cancelButton = (Button) findViewById(R.id.cancelButton);

        _resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = _emailText.getText().toString().trim();
                if (Validation.validateEmail(email)) {
                    resetPassword(email);
                } else {
                    _emailText.setError("Not a valid email address");
                    Log.d(TAG, "Failed with email: " + email);
                }
            }
        });

        _cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void resetPassword(String email){
        showWorkingDialog("Resetting Password...");
        ArrayList params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        ServerRequest serverRequest = new ServerRequest();
        //  JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/resetpass", params);
        JSONObject json = serverRequest.getJSON(serverRequest.serverAddress + "/api/resetpass", params);
        Log.d(TAG, "Resetting password for: " + email);
        closeWorkingDialog(1000);
        if (json != null) {
            try {
                String response = json.getString("response");
                if(json.getBoolean("res")){
                    Log.e("JSON", response);
                    Toast.makeText(getApplication(), response, Toast.LENGTH_LONG).show();
                    showConfirmationView(email);
                } else {
                Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
            e.printStackTrace();
            }
        }else{
            Log.d(TAG, "No response from server");
        }
    }

    private void showConfirmationView(final String email){
        final Dialog confirmDialog = new Dialog(this);
        confirmDialog.setTitle("Confirm Email");
        confirmDialog.setContentView(R.layout.dialog_confirm_email_code);
        _resetCodeText = (TextView) confirmDialog.findViewById(R.id.resetCodeText);
        _newPassword = (TextView) confirmDialog.findViewById(R.id.newPasswordText);
        _confirmButton = (Button) confirmDialog.findViewById(R.id.confirmCodeButton);
        Button cancelButton = (Button) confirmDialog.findViewById(R.id.cancelButton);
        _confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String code = _resetCodeText.getText().toString();
                String newPassword = _newPassword.getText().toString();
                boolean confirmed = getServerConfirmation(email, code, newPassword);
                if(confirmed){
                    confirmDialog.dismiss();
                }
                else {

                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        confirmDialog.show();
    }

    private boolean getServerConfirmation(String email, String code, String newPassword){
        showWorkingDialog("Confirming...");
        ArrayList params = new ArrayList<NameValuePair>();
        ServerRequest serverRequest = new ServerRequest();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("newpass", newPassword));
        JSONObject json = serverRequest.getJSON(serverRequest.serverAddress + "/api/resetpass/chg",params);
        closeWorkingDialog(1000);
        if(json != null){
            try{
                String response = json.getString("response");
                Toast.makeText(getApplication(), response, Toast.LENGTH_LONG);
                return json.getBoolean("res");
            } catch(Exception e){
                Log.d(TAG, "Failed to get json response");
            }
        }
        return false;
    }

    private void showWorkingDialog(String message){
        progressDialog = new ProgressDialog(ResetPasswordActivity.this,
                R.style.Base_Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void closeWorkingDialog(){
        progressDialog.dismiss();
    }

    private void closeWorkingDialog(int delay){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        closeWorkingDialog();
                    }
                }, delay);
    }
}
