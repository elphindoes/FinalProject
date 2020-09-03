package util;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Config.ApiParams;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Lenovo on 10-05-2017.
 */

public class VJsonRequest {
    VJsonResponce vresponce;
    Activity instance;
    String url;
    ArrayList<NameValuePair> params;
    //int method;

    public VJsonRequest(Activity activity, String url, VJsonResponce vresponce) {
        this.instance = activity;
        this.vresponce = vresponce;
        this.url = url;
        //method = Request.Method.GET;
        this.params = new ArrayList<NameValuePair>();
        new jsonRequestTask().execute();
    }

    public VJsonRequest(Activity activity, String url, ArrayList<NameValuePair> parms, VJsonResponce vresponce) {
        this.instance = activity;
        this.vresponce = vresponce;
        this.url = url;
        //this.method = Request.Method.POST;
        this.params = parms;

        new jsonRequestTask().execute();
    }

    public interface VJsonResponce {
        public void VResponce(String responce);

        public void VError(String responce);
    }

    private class jsonRequestTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void[] params) {
            // do above Server call here
            return requestStringData();

        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            //process message
            if (jObj != null) {
                try {
                    if (jObj.has(ApiParams.PARM_RESPONCE)) {
                        if (jObj.getBoolean(ApiParams.PARM_RESPONCE)) {
                            vresponce.VResponce(jObj.getString(ApiParams.PARM_DATA));
                        } else {
                            vresponce.VError(jObj.getString(ApiParams.PARM_ERROR));
                        }
                    } else {
                        vresponce.VResponce(jObj.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private JSONObject requestStringData() {

        OkHttpClient client = new OkHttpClient();
        //increased timeout for slow response
        OkHttpClient eagerClient = client.newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        //Log.i("PARAMETERS", "PARAMETERS ::" + valuePairs);

        FormBody.Builder builder = new FormBody.Builder();

        try {

            for (NameValuePair param : params){
                builder.add(param.name, param.value);
            }

            Request request = new Request.Builder().url(this.url).post(builder.build()).build();
            Log.i("Registration Request::", request.toString());

            Response response = eagerClient.newCall(request).execute();
            Log.i("REGISTRATION RESPONSE::", response.toString());
            String res = response.body().string();

            JSONObject jObj = null;

            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
            // common.setSession(ApiParams.PREF_ERROR, e.getMessage().toString());
            //return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //res = res.replace("%20"," ");
        //}else
        //{
        //Snackbar.make(null, "Replace with your own action", Snackbar.LENGTH_LONG)
        //		.setAction("Action", null).show();
        //}
        return null;
    }

}
