package com.clinicapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

import AsyTasks.CommonAsyTask;
import Config.ApiParams;
import util.CommonActivity;
import util.NameValuePair;

public class LoginActivity extends CommonActivity {
    EditText editEmail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setHeaderLogo();
        editEmail = (EditText) findViewById(R.id.txtEmail);
        editPassword = (EditText) findViewById(R.id.txtPassword);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            common.setToastMessage(getString(R.string.valid_required_email));
            focusView = editEmail;
            cancel = true;
        }
        if (!isValidEmail(email)) {
            common.setToastMessage(getString(R.string.valid_email));
            focusView = editEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            common.setToastMessage(getString(R.string.valid_required_password));
            focusView = editPassword;
            cancel = true;
        }

        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


            nameValuePairs.add(new NameValuePair("user_email", email));
            nameValuePairs.add(new NameValuePair("user_password", password));
            CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.LOGIN_URL, new CommonAsyTask.VJsonResponce() {
                @Override
                public void VResponce(ArrayList<HashMap<String, String>> hashMaps) {
                    HashMap<String, String> userdata = hashMaps.get(0);
                    Intent intent = null;
                    common.setSession(ApiParams.COMMON_KEY, userdata.get("user_id"));
                    common.setSession(ApiParams.USER_FULLNAME, userdata.get("user_fullname"));
                    common.setSession(ApiParams.USER_EMAIL, userdata.get("user_email"));
                    common.setSession(ApiParams.USER_PHONE, userdata.get("user_phone"));
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void VError(String responce) {
                    common.setToastMessage(responce);
                }
            }, true, this);
            commonTask.execute();
        }

    }

    public void forgotPassword(View view) {
        String email = editEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            common.setToastMessage(getString(R.string.valid_required_email));
            focusView = editEmail;
            cancel = true;
        }
        if (!isValidEmail(email)) {
            common.setToastMessage(getString(R.string.valid_email));
            focusView = editEmail;
            cancel = true;
        }
        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


            nameValuePairs.add(new NameValuePair("email", email));
            CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.FORGOT_PASSWORD_URL, new CommonAsyTask.VJsonResponce() {
                @Override
                public void VResponce(ArrayList<HashMap<String, String>> hashMaps) {
                    common.setToastMessage(hashMaps.get(0).get(ApiParams.PARM_DATA));
                }

                @Override
                public void VError(String responce) {
                    common.setToastMessage(responce);
                }
            }, true, this);
            commonTask.execute();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void registerClick(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}
