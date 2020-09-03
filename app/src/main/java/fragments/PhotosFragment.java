package fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.clinicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import AsyTasks.CommonAsyTask;
import Config.ApiParams;
import Config.ConstValue;
import util.CommonClass;
import util.NameValuePair;

/**
 * Created by subhashsanghani on 1/17/17.
 */

public class PhotosFragment extends Fragment {
    ArrayList<HashMap<String, String>> arrayList;
    CommonClass common;
    PhotosAdapter adapter;
    GridView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        common = new CommonClass(getActivity());
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.PHOTOS_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(ArrayList<HashMap<String, String>> hashMaps) {
                arrayList = hashMaps;
                common.parseObject("photos", arrayList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void VError(String responce) {
                common.setToastMessage(responce);
            }
        }, true, getActivity());
        commonTask.execute();
        arrayList = common.getParseObject("photos");
        adapter = new PhotosAdapter();
        listview = (GridView) rootView.findViewById(R.id.gridView);
        listview.setAdapter(adapter);
        return rootView;
    }

    class PhotosAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public PhotosAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {

            if (arrayList == null)
                return 0;
            else
                return arrayList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = inflater.inflate(R.layout.row_photos, null);

            TextView textTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            ImageView imageIcon = (ImageView) convertView.findViewById(R.id.imageView);
            HashMap<String, String> obj = arrayList.get(position);
            textTitle.setText(obj.get("photo_title"));
            Picasso.with(getActivity()).load(ConstValue.BASE_URL + "/uploads/business/businessphoto/" + obj.get("photo_image")).into(imageIcon);

            return convertView;
        }


    }
}
