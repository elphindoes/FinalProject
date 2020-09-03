package com.clinicapp;

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

public class ChangePasswordActivity extends CommonActivity {
    EditText txtNewPass, txtCPass, txtRPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setHeaderLogo();
        allowBack();

        txtNewPass = (EditText) findViewById(R.id.txtNewPassword);
        txtCPass = (EditText) findViewById(R.id.txtCurrentPassword);
        txtRPass = (EditText) findViewById(R.id.txtRePassword);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    public void register() {

        String newPassword = txtNewPass.getText().toString();
        String currentPassword = txtCPass.getText().toString();
        String rePassword = txtRPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.

        if (TextUtils.isEmpty(currentPassword)) {
            common.setToastMessage(getString(R.string.valid_required_current_password));
            focusView = txtCPass;
            cancel = true;
        }
        if (TextUtils.isEmpty(newPassword)) {
            common.setToastMessage(getString(R.string.valid_required_new_password));
            focusView = txtNewPass;
            cancel = true;
        }
        if (!txtNewPass.getText().toString().equals(txtRPass.getText().toString())) {
            common.setToastMessage(getString(R.string.valid_not_match));
            focusView = txtRPass;
            cancel = true;
        }
        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new NameValuePair("c_password", currentPassword));
            nameValuePairs.add(new NameValuePair("n_password", newPassword));
            nameValuePairs.add(new NameValuePair("r_password", rePassword));
            nameValuePairs.add(new NameValuePair("user_id", common.get_user_id()));
            CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.CHANGE_PASSWORD_URL, new CommonAsyTask.VJsonResponce() {
                @Override
                public void VResponce(ArrayList<HashMap<String, String>> hashMaps) {
                    common.setToastMessage(hashMaps.get(0).get(ApiParams.PARM_DATA));
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

}
