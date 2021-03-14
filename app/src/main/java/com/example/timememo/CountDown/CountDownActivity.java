package com.example.timememo.CountDown;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.timememo.R;

public class CountDownActivity extends AppCompatActivity {
    private TextView tvtime1, tvtime2, tvtime3 = null;
    private long time = 0;
    private boolean isPause = false;
    private Button count_finish = null;
    private Button count_pause = null;
    private ChooseStateDialog chooseStateDialog= null;

    private String title = null;
    private int uid = 0;
    private String type =null;
    private String startTime = null;

    private long timeLength = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        tvtime1 = findViewById(R.id.tvtime1);
        tvtime2 = findViewById(R.id.tvtime2);
        tvtime3 = findViewById(R.id.tvtime3);
        count_finish = findViewById(R.id.count_finish);
        count_pause = findViewById(R.id.count_pause);

        title = getIntent().getStringExtra("backlog_title");
        uid = getIntent().getIntExtra("backlog_uid",-1);
        type = getIntent().getStringExtra("backlog_type");
        startTime = getIntent().getStringExtra("starttime");

        String timestr = getIntent().getStringExtra("time");

        time = Long.parseLong(timestr)*60;

        timeLength = 0;
        handler.postDelayed(runnable, 1000);


        count_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPause = !isPause;

                if (isPause){
                    count_pause.setText("继续");
                }else{
                    count_pause.setText("暂停");
                    handler.postDelayed(runnable, 1000);
                }
            }
        });

        count_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPause = true;
                count_pause.setText("继续");
                chooseStateDialog = new ChooseStateDialog();
                chooseStateDialog.show(getSupportFragmentManager(),"ChooseState");
                Bundle bundle = new Bundle();
                bundle.putInt("uid",uid);
                bundle.putString("title",title);
                bundle.putLong("timeLength",timeLength);
                bundle.putString("type",type);
                bundle.putString("startTime",startTime);
                chooseStateDialog.setArguments(bundle);
            }
        });
    }

    @Override
    public void onBackPressed() {
        isPause = true;
        count_pause.setText("继续");
        chooseStateDialog = new ChooseStateDialog();
        chooseStateDialog.show(getSupportFragmentManager(),"ChooseState");
        Bundle bundle = new Bundle();
        bundle.putInt("uid",uid);
        bundle.putString("title",title);
        bundle.putLong("timeLength",timeLength);
        bundle.putString("type",type);
        bundle.putString("startTime",startTime);
        chooseStateDialog.setArguments(bundle);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String formatLongToTimeStr = formatLongToTimeStr(time);
            String[] split = formatLongToTimeStr.split("：");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    tvtime1.setText(split[0] + "小时");
                }
                if (i == 1) {
                    tvtime2.setText(split[1] + "分钟");
                }
                if (i == 2) {
                    tvtime3.setText(split[2] + "秒");
                }
            }
            time--;
            timeLength++;
            if (time >= 0) {
                if (!isPause){
                    handler.postDelayed(this, 1000);
                }
            }else {
                //结束计时后
                //播放提示音和震动
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);

                rt.play();
                Vibrator vibrator = (Vibrator)CountDownActivity.this.getSystemService(CountDownActivity.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);


                //弹出窗口
                isPause = true;
                count_pause.setText("继续");
                chooseStateDialog = new ChooseStateDialog();
                chooseStateDialog.show(getSupportFragmentManager(),"ChooseState");
                Bundle bundle = new Bundle();
                bundle.putInt("uid",uid);
                bundle.putString("title",title);
                bundle.putLong("timeLength",timeLength);
                bundle.putString("type",type);
                bundle.putString("startTime",startTime);
                chooseStateDialog.setArguments(bundle);
            }
        }
    };

    public String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue();
        if (second > 60) {
            minute = second / 60;         //取整
            second = second % 60;         //取余
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = hour + "：" + minute + "：" + second;
        return strtime;

    }
}