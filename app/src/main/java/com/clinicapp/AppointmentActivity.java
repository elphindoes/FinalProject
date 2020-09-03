package com.clinicapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import AsyTasks.CommonAsyTask;
import Config.ApiParams;
import Config.ConstValue;
import fragments.TimeSlotFragment;
import util.CommonActivity;
import util.NameValuePair;
import util.VJsonRequest;

public class AppointmentActivity extends CommonActivity {
    TabLayout tabLayout;
    Calendar calender;
    private int c_day;
    private int c_month;
    private int c_year;
    SimpleDateFormat df, df2;
    String currentdate;
    Button buttonChooseDate;

    ViewPager viewPager;
    HashMap<String, String> timeslotdata;
    SampleFragmentPagerAdapter sampleFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        allowBack();
        setHeaderLogo();
        calender = Calendar.getInstance(TimeZone.getDefault());
        c_day = calender.get(Calendar.DAY_OF_MONTH);
        c_month = calender.get(Calendar.MONTH);
        c_year = calender.get(Calendar.YEAR);
        df = new SimpleDateFormat("yyyy-MM-dd", ConstValue.LOCALE);
        df2 = new SimpleDateFormat("HH:mm aa", ConstValue.LOCALE);
        currentdate = c_year + "-" + (c_month + 1) + "-" + c_day;
        //df.format(calender.getTime());

        buttonChooseDate = (Button) findViewById(R.id.buttonChooseDate);
        buttonChooseDate.setText(currentdate);

        buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });

        loadSlotTask();


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_morning)));
        //tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_afternoon)));
        //tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_evening)));
        //setupTabIcons();

    }

    public void loadSlotTask() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new NameValuePair("date", currentdate));

        CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.TIMESLOT_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(ArrayList<HashMap<String, String>> hashMaps) {
                if (hashMaps != null && hashMaps.size() > 0) {
                    timeslotdata = hashMaps.get(0);

                    sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
                    viewPager.setAdapter(sampleFragmentPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                }
            }

            @Override
            public void VError(String responce) {
                common.setToastMessage(responce);
            }
        }, true, this);
        commonTask.execute();
    }

    public void DateDialog() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                currentdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                //txtDate.setText(currentdate);
                c_year = year;
                c_day = dayOfMonth;
                c_month = monthOfYear + 1;
                buttonChooseDate.setText(currentdate);
                loadSlotTask();

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, c_year, c_month, c_day);
        Date date = new Date();

        dpDialog.getDatePicker().setMinDate(date.getTime());

        dpDialog.show();

    }

    /*private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_service);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_contact);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat_s);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_image);
    }*/
    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[]{getString(R.string.tab_morning), getString(R.string.tab_afternoon), getString(R.string.tab_evening)};
        //private int tabIcons[] = {R.drawable.ic_service, R.drawable.ic_contact, R.drawable.ic_chat_s, R.drawable.ic_image};

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TimeSlotFragment();


            Bundle args = new Bundle();
            args.putString("date", currentdate);
            if (position == 0) {
                args.putString("slot", timeslotdata.get("morning"));
            }
            if (position == 1) {
                args.putString("slot", timeslotdata.get("afternoon"));
            }
            if (position == 2) {
                args.putString("slot", timeslotdata.get("evening"));
            }
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            /*Drawable image =  ContextCompat.getDrawable(getApplicationContext(), tabIcons[0]) ;//getApplicationContext().getResources().getDrawable(tabIcons[0],getApplicationContext().getTheme()); //ContextCompat.getDrawable(getApplicationContext(), tabIcons[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            // Replace blank spaces with image icon
            SpannableString sb = new SpannableString("   " + tabTitles[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
            */

            return tabTitles[position];
        }
    }

}
