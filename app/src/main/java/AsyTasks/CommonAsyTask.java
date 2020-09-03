package AsyTasks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.clinicapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Config.ApiParams;
import util.CommonClass;
import util.JSONParser;
import util.NameValuePair;

/**
 * Created by subhashsanghani on 1/15/17.
 */

public class CommonAsyTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
    private ArrayList<NameValuePair> _nameValuePairs;
    private String _baseUrl;
    @SuppressLint("StaticFieldLeak")
    private Activity _activity;
    private String error_string;
    private Handler handler;
    private ProgressDialog progressDialog;
    private boolean is_progress_show;
    private CommonClass common;
    private boolean is_success;
    private JSONObject responceObj;
    private VJsonResponce vJsonResponce;
    private boolean isAllData = false;

    public CommonAsyTask(ArrayList<NameValuePair> nameValuePairs, String baseUrl, Handler handler, boolean is_progress_show, Activity activity) {
        responceObj = new JSONObject();
        _nameValuePairs = nameValuePairs;
        _baseUrl = baseUrl;
        _activity = activity;
        this.handler = handler;
        this.is_progress_show = is_progress_show;
        common = new CommonClass(_activity);
    }

    public CommonAsyTask(ArrayList<NameValuePair> nameValuePairs, String baseUrl, VJsonResponce vJsonResponce, boolean is_progress_show, Activity activity) {
        responceObj = new JSONObject();
        _nameValuePairs = nameValuePairs;
        _baseUrl = baseUrl;
        _activity = activity;
        this.vJsonResponce = vJsonResponce;
        this.is_progress_show = is_progress_show;
        common = new CommonClass(_activity);
    }

    public CommonAsyTask(ArrayList<NameValuePair> nameValuePairs, String baseUrl, VJsonResponce vJsonResponce, boolean is_progress_show, boolean isAllData, Activity activity) {
        responceObj = new JSONObject();
        _nameValuePairs = nameValuePairs;
        _baseUrl = baseUrl;
        _activity = activity;
        this.vJsonResponce = vJsonResponce;
        this.isAllData = isAllData;
        this.is_progress_show = is_progress_show;
        common = new CommonClass(_activity);
    }

    public interface VJsonResponce {
        public void VResponce(ArrayList<HashMap<String, String>> hashMaps);

        public void VError(String responce);
    }

    @Override
    protected void onPreExecute() {
        if (is_progress_show) {
            progressDialog = ProgressDialog.show(_activity, "", _activity.getResources().getString(R.string.process_with_Data), true);
        }
        super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        super.onCancelled();
    }

    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(String... strings) {
        JSONParser jsonParser = new JSONParser(_activity);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        try {
            String json_responce = jsonParser.execPostScriptJSON(_baseUrl, _nameValuePairs);

            Log.e(_activity.toString(), "response:" + json_responce);

            JSONObject jObj = new JSONObject(json_responce);
            responceObj = jObj;
            if (jObj.has(ApiParams.PARM_RESPONCE) && !jObj.getBoolean(ApiParams.PARM_RESPONCE)) {
                is_success = false;
                error_string = jObj.getString(ApiParams.PARM_ERROR);
                return null;
            } else {
                if (isAllData) {
                    if (vJsonResponce != null) {
                        is_success = true;
                        arrayList.add(common.getMapJsonObject(jObj));
                        return arrayList;
                    }
                }
                if (jObj.has(ApiParams.PARM_DATA)) {
                    if (jObj.get(ApiParams.PARM_DATA) instanceof String) {
                        is_success = true;
                        error_string = jObj.getString(ApiParams.PARM_DATA);
                        if (vJsonResponce != null) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(ApiParams.PARM_DATA, error_string);
                            arrayList.add(hashMap);
                            return arrayList;
                        }
                    } else if (jObj.get(ApiParams.PARM_DATA) instanceof JSONObject) {
                        is_success = true;
                        arrayList.clear();
                        JSONObject d = jObj.getJSONObject(ApiParams.PARM_DATA);
                        arrayList.add(common.getMapJsonObject(d));
                        return arrayList;
                    } else if (jObj.get(ApiParams.PARM_DATA) instanceof JSONArray) {
                        is_success = true;
                        JSONArray services = jObj.getJSONArray(ApiParams.PARM_DATA);
                        arrayList.clear();
                        for (int i = 0; i < services.length(); i++) {
                            JSONObject d = services.getJSONObject(i);
                            arrayList.add(common.getMapJsonObject(d));
                        }
                        return arrayList;
                    }
                }
            }
            return null;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            error_string = e.getMessage();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            error_string = e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, String>> hashMaps) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (vJsonResponce != null) {
            if (is_success) {
                vJsonResponce.VResponce(hashMaps);
            } else {
                vJsonResponce.VError(error_string);
            }
        } else {
            if (hashMaps == null) {
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString(ApiParams.PARM_ERROR, error_string);
                bundle.putString("object", responceObj.toString());
                bundle.putBoolean(ApiParams.PARM_RESPONCE, is_success);
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ApiParams.PARM_DATA, hashMaps);
                bundle.putBoolean(ApiParams.PARM_RESPONCE, is_success);
                bundle.putString("object", responceObj.toString());
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }
        super.onPostExecute(hashMaps);
    }
}
