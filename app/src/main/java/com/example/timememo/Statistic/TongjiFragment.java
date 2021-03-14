package com.example.timememo.Statistic;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timememo.BlankFragment;
import com.example.timememo.Entity.Activity;
import com.example.timememo.Entity.Backlog;
import com.example.timememo.Entity.User;
import com.example.timememo.HttpRequest.HttpUtil;
import com.example.timememo.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TongjiFragment extends BlankFragment {
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;


    private TextView all_times = null;
    private TextView all_timelength = null;

    private TextView day_times = null;
    private TextView day_timel = null;

    private TextView Ddate = null;
    private TextView Ddate2  = null;

    private PieChart mPieChart = null;
    private PieChart mPieChart_type = null;

    private SwipeRefreshLayout swipeRefresh = null;

    private List<Backlog> backlogList = new ArrayList<>();

    private List<Activity> activityList = new ArrayList<>();

    private List<String> typeList = new ArrayList<>();

    private String url_b = "http://192.168.137.1:8000/backlog";

    private String url_a = "http://192.168.137.1:8000/activity";

    private User mUser = null;
    private String nowDate = null;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.tongji_layout, container, false);
            mPieChart_type = mView.findViewById(R.id.mPieChart_type);
            //获取系统时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            nowDate = simpleDateFormat.format(date);
            mPieChart = mView.findViewById(R.id.mPieChart);

            mPieChart.clear();
            mPieChart_type.clear();
            HttpGet();

            //刷新
            swipeRefresh = mView.findViewById(R.id.swip_refresh);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPieChart.clear();
                    mPieChart_type.clear();
                    HttpGet();
                    swipeRefresh.setRefreshing(false);
                }
            });

            isPrepared = true;
            //实现懒加载
            lazyLoad();
        }
        //缓存的mView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }


    private void parseJSONWithGSON_B(String jsonData) {
        Gson gson = new Gson();
        backlogList = gson.fromJson(jsonData, new TypeToken<List<Backlog>>() {
        }.getType());
    }

    private void parseJSONWithGSON_A(String jsonData) {
        Gson gson = new Gson();
        activityList = gson.fromJson(jsonData, new TypeToken<List<Activity>>() {
        }.getType());
    }
    //获取backlog 和 activity
    private void HttpGet(){
        HttpUtil.sendOkHttpRequest(url_b, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseJSONWithGSON_B(responseData);
                HttpUtil.sendOkHttpRequest(url_a, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        parseJSONWithGSON_A(responseData);
                        Log.d("responseData",responseData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initView();
                                initType();
                            }
                        });
                    }
                });
            }
        });
    }
    /**
     * 初始化控件
     */
    private void initView() {
        mUser = getArguments().getParcelable("user");

        int alltimes = 0;
        int alltimel = 0;
        int daytimes = 0;
        int daytimel = 0;

        day_times = mView.findViewById(R.id.day_times);
        day_timel = mView.findViewById(R.id.day_timel);
        Ddate = mView.findViewById(R.id.Ddate);
        Ddate2 = mView.findViewById(R.id.Ddate2);
        Ddate.setText(nowDate);
        Ddate2.setText(nowDate);
        Log.d("nowDate",nowDate);

        for (Activity activity:activityList){
            if (activity.getUid() == mUser.getId()){
                alltimes ++;
                alltimel += Integer.parseInt(activity.getaTime());
            }

            if (activity.getaDate().equals(nowDate)){
                daytimes ++;
                daytimel += Integer.parseInt(activity.getaTime());
            }
        }

        all_times = mView.findViewById(R.id.all_times);
        all_timelength = mView.findViewById(R.id.all_timelength);


        Log.d("alltimes",String.valueOf(alltimes));
        all_times.setText(String.valueOf(alltimes));
        all_timelength.setText(String.valueOf(alltimel/60));
        day_times.setText(String.valueOf(daytimes));
        day_timel.setText(String.valueOf(daytimel/60));

        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);

    //设置中间文字
        mPieChart.setCenterText(new SpannableString("活动"));

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
    // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

    //模拟数据
    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

    //将activity时间进行合计后添加到entries中
        for(Backlog backlog : backlogList){
        int time = 0;
        if (backlog.getUid() == mUser.getId()){
            for (Activity activity:activityList){
                if (activity.getaDate().equals(nowDate)){
                    if(activity.getaTitle().equals(backlog.getTitle())){
                        time += Integer.parseInt(activity.getaTime());
                    }
                }
            }
            entries.add(new PieEntry(time,backlog.getTitle()));
        }
    }

    setData(entries);

        mPieChart.animateXY(1000, 1000);

    Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.LINE);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

    //输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);

}

    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries,"活动统计");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for (Backlog backlog:backlogList){
//            colors.add(Color.parseColor(backlog.getRgb()));
//        }
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }



    //初始化类型图
    private void initType(){
        mPieChart_type.setUsePercentValues(true);
        mPieChart_type.getDescription().setEnabled(false);
        mPieChart_type.setExtraOffsets(5, 10, 5, 5);
        mPieChart_type.setDragDecelerationFrictionCoef(0.95f);

        //设置中间文字
        mPieChart_type.setCenterText(new SpannableString("类型"));

        mPieChart_type.setDrawHoleEnabled(true);
        mPieChart_type.setHoleColor(Color.WHITE);

        mPieChart_type.setTransparentCircleColor(Color.WHITE);
        mPieChart_type.setTransparentCircleAlpha(110);

        mPieChart_type.setHoleRadius(58f);
        mPieChart_type.setTransparentCircleRadius(61f);

        mPieChart_type.setDrawCenterText(true);

        mPieChart_type.setRotationAngle(0);
        // 触摸旋转
        mPieChart_type.setRotationEnabled(true);
        mPieChart_type.setHighlightPerTapEnabled(true);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (Backlog backlog:backlogList){
            if (backlog.getUid() == mUser.getId()) {
                if (!typeList.contains(backlog.getBacklogType())) {
                    typeList.add(backlog.getBacklogType());
                }
            }
        }


        //将activity时间进行合计后添加到entries中
        for(String type : typeList){
            int time = 0;
            for (Activity activity:activityList){
                if (activity.getaDate().equals(nowDate)){
                    if(activity.getaType().equals(type)){
                        time += Integer.parseInt(activity.getaTime());
                    }
                }
            }
            entries.add(new PieEntry(time,type));
        }


        setTypeData(entries);

        mPieChart_type.animateXY(1000, 1000);

        Legend l = mPieChart_type.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.LINE);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        //输入标签样式
        mPieChart_type.setEntryLabelColor(Color.WHITE);
        mPieChart_type.setEntryLabelTextSize(12f);
    }


    //设置类型的数据
    private void setTypeData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries,"类型统计");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for (Backlog backlog:backlogList){
//            colors.add(Color.parseColor(backlog.getRgb()));
//        }
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart_type.setData(data);
        mPieChart_type.highlightValues(null);
        //刷新
        mPieChart_type.invalidate();
    }

    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        //填充各控件的数据
        mHasLoadedOnce = true;
    }
    public static Fragment newInstance(String param1) {
        TongjiFragment fragment = new TongjiFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
}
