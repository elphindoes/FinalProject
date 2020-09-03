package configfcm;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

import AsyTasks.CommonAsyTask;
import Config.ApiParams;
import util.CommonClass;
import util.NameValuePair;

/**
 * Created by subhashsanghani on 12/21/16.
 */

public class MyFirebaseRegister {
    Activity _context;
    CommonClass common;

    public MyFirebaseRegister(Activity context) {
        this._context = context;
        common = new CommonClass(_context);

    }

    public void RegisterUser(String user_id) {
        // [START subscribe_topics]
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("clinicapp");
        // [END subscribe_topics]


        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new NameValuePair("user_id", user_id));
        nameValuePairs.add(new NameValuePair("token", token));
        nameValuePairs.add(new NameValuePair("device", "android"));
        CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.REGISTER_FCM_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(ArrayList<HashMap<String, String>> hashMaps) {

            }

            @Override
            public void VError(String responce) {

            }
        }, true, _context);
        commonTask.execute();

    }

}
