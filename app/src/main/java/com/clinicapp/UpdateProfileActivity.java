package com.clinicapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import AsyTasks.CommonAsyTask;
import Config.ApiParams;
import util.CommonActivity;
import util.NameValuePair;

public class UpdateProfileActivity extends CommonActivity {
    EditText editEmail, editPhone, editFullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        setHeaderLogo();
        allowBack();
        editEmail = (EditText) findViewById(R.id.txtEmail);
        editFullname = (EditText) findViewById(R.id.txtFirstname);
        editPhone = (EditText) findViewById(R.id.txtPhone);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new NameValuePair("user_id", common.get_user_id()));
        CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.USERDATA_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(ArrayList<HashMap<String, String>> hashMaps) {
                if (hashMaps != null) {
                    HashMap<String, String> userdata = hashMaps.get(0);
                    editEmail.setText(userdata.get("user_email"));
                    editFullname.setText(userdata.get("user_fullname"));
                    editPhone.setText(userdata.get("user_phone"));
                    editEmail.setEnabled(false);
                }
            }

            @Override
            public void VError(String responce) {
                common.setToastMessage(responce);
            }
        }, true, this);
        commonTask.execute();
    }

    public void register() {
        String fullname = editFullname.getText().toString();
        String phone = editPhone.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.

        if (TextUtils.isEmpty(fullname)) {
            common.setToastMessage(getString(R.string.valid_required_fullname));
            focusView = editFullname;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            common.setToastMessage(getString(R.string.valid_required_password));
            focusView = editPhone;
            cancel = true;
        }
        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new NameValuePair("user_fullname", fullname));
            nameValuePairs.add(new NameValuePair("user_phone", phone));
            nameValuePairs.add(new NameValuePair("user_id", common.get_user_id()));
            CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.UPDATEPROFILE_URL, new CommonAsyTask.VJsonResponce() {
                @Override
                public void VResponce(ArrayList<HashMap<String, String>> hashMaps) {
                    Toast.makeText(getApplicationContext(), getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();

                    common.setSession(ApiParams.USER_FULLNAME, editFullname.getText().toString());
                    common.setSession(ApiParams.USER_PHONE, editPhone.getText().toString());
                }

                @Override
                public void VError(String responce) {
                    common.setToastMessage(responce);
                }
            }, true, this);
            commonTask.execute();
        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
