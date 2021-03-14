package com.example.timememo.CountDown;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.timememo.HttpRequest.HttpUtil;
import com.example.timememo.R;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChooseStateDialog extends DialogFragment {
    private Button confirm = null;
    private RadioGroup chooseGroup = null;
    private String state = null;
    private String nowTime = null;
    private String title = null;
    private String type = null;
    private String startTime = null;
    private int uid = 0;
    private long timelength;
    private String url = "http://192.168.137.1:8000/activity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_state_layout,container);
        confirm = view.findViewById(R.id.confirm);
        chooseGroup = view.findViewById(R.id.chooseGruop);

        title = getArguments().getString("title");
        uid = getArguments().getInt("uid");
        timelength = getArguments().getLong("timeLength");
        type = getArguments().getString("type");
        startTime = getArguments().getString("startTime");

        chooseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.comrbt:
                        state = "complete";
                        break;
                    case R.id.incomrbt:
                        state = "incomplete";
                        break;
                        default:
                            break;
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //获取系统时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
                //获取当前时间
                Date date = new Date(System.currentTimeMillis());
                nowTime = simpleDateFormat.format(date);
                final String json = "{"+
                        "\"aTitle\":"+"\""+title +"\""+
                        ",\"aTime\":"+ timelength +
                        ",\"aType\":"+"\""+type +"\""+
                        ",\"aStartTime\":"+"\""+startTime +"\""+
                        ",\"aEndTime\":"+"\""+nowTime +"\""+
                        ",\"aState\":"+"\""+state +"\""+
                        ",\"uid\":"+uid +
                        "}";
                Log.d("json:",json);
                MediaType JSON1 = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody=RequestBody.create(JSON1,json);
                HttpUtil.SendOkhttpRequest_POST(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Log.d("response",response.body().string());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"Activity create successfully",Toast.LENGTH_SHORT).show();
                                dismiss();
                                getActivity().finish();
                            }
                        });
                    }
                });

            }
        });
        return view;
    }
}
