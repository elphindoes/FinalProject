package com.clinicapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;

import java.util.ArrayList;
import java.util.HashMap;

import AsyTasks.CommonAsyTask;
import Config.ApiParams;
import util.CommonActivity;
import util.NameValuePair;

public class ChangePasswordActivity extends CommonActivity {
    EditText txtNewPass, txtCPass, txtRPass;
    CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setHeaderLogo();
        allowBack();


        txtNewPass = (EditText) findViewById(R.id.txtNewPassword);
        txtCPass = (EditText) findViewById(R.id.txtCurrentPassword);
        txtRPass = (EditText) findViewById(R.id.txtRePassword);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    txtCPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    txtNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    txtRPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    txtCPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    txtNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    txtRPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }


            }
        });
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








