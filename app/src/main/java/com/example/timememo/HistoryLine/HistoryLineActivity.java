package com.example.timememo.HistoryLine;


import android.annotation.TargetApi;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.timememo.Entity.Activity;
import com.example.timememo.HttpRequest.HttpUtil;
import com.example.timememo.R;
import com.example.timememo.SaveUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryLineActivity extends AppCompatActivity {
    private RecyclerView activityView = null;
    private CalendarView calendarView = null;

    private List<Activity> mActivityList = new ArrayList<>();

    private List<Activity> activities = new ArrayList<>();

    private String seleteDate = null;

    private String url = "http://192.168.137.1:8000/activity";

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_line);
        calendarView = findViewById(R.id.calendarView);
        activityView = findViewById(R.id.activityView);

        //获取系统时间
        android.icu.text.SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        seleteDate = simpleDateFormat.format(date);
        Log.d("SelectDate",seleteDate);
        HttpGet();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@androidx.annotation.NonNull CalendarView view, int year, int month, int dayOfMonth) {
                seleteDate = year+"-"+String.format("%02d",month+1)+"-"+String.format("%02d",dayOfMonth);
                HttpGet();
            }
        });

    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        mActivityList = gson.fromJson(jsonData,new TypeToken<List<Activity>>(){}.getType());
        String userId = SaveUtils.getSettingNote(HistoryLineActivity.this,"userID","userID");
        int id = Integer.parseInt(userId);
        activities.clear();
        for (Activity activity:mActivityList){
            if (activity.getUid() == id){
                if(activity.getaDate().equals(seleteDate)){
                    activities.add(activity);
                }
            }
        }
    }

    private void HttpGet(){
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseJSONWithGSON(responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(HistoryLineActivity.this);
                        activityView.setLayoutManager(layoutManager);
                        ActivityAdapter adapter = new ActivityAdapter(activities);
                        activityView.setAdapter(adapter);
                    }
                });
            }
        });
    }

}
