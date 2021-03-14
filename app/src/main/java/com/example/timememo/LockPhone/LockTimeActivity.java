package com.example.timememo.LockPhone;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timememo.CountDown.CountDownActivity;
import com.example.timememo.R;
import com.example.timememo.Statistic.TongjiFragment;

public class LockTimeActivity extends AppCompatActivity {

    private TextView lock_hour = null;
    private TextView lock_minute = null;
    private TextView lock_second = null;
    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_time);

        lock_hour = findViewById(R.id.lock_hour);
        lock_minute = findViewById(R.id.lock_minute);
        lock_second = findViewById(R.id.lock_second);
        String t = getIntent().getStringExtra("time");
        time = Long.parseLong(t)*60;

        handler.postDelayed(runnable, 1000);

        //Home键监听
        HomeListener.getInstance().setHomeKeylistener(new HomeListener.HomeKeyListener.HomePressListener() {
            @Override
            public void onHomePress() {
                Toast.makeText(LockTimeActivity.this,"别想退出去",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHomeRecentAppsPress() {

                Toast.makeText(LockTimeActivity.this,"别想退出去",Toast.LENGTH_SHORT).show();

            }
        });
    }


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String formatLongToTimeStr = formatLongToTimeStr(time);
            String[] split = formatLongToTimeStr.split("：");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    lock_hour.setText(split[0] + "小时");
                }
                if (i == 1) {
                    lock_minute.setText(split[1] + "分钟");
                }
                if (i == 2) {
                    lock_second.setText(split[2] + "秒");
                }
            }
            time--;
            if (time >= 0) {
                handler.postDelayed(this, 1000);
            }else{
                //结束计时后
                //播放提示音和震动
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);

                rt.play();
                Vibrator vibrator = (Vibrator) LockTimeActivity.this.getSystemService(LockTimeActivity.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                finish();
            }
        }

    };

    //后退键监听
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"别想退出去",Toast.LENGTH_SHORT).show();
    }



//时间格式转换
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
