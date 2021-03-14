package com.example.timememo.Backlog;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timememo.CountDown.CountDownActivity;
import com.example.timememo.Entity.Backlog;
import com.example.timememo.Entity.User;
import com.example.timememo.HttpRequest.HttpUtil;
import com.example.timememo.LoginAndSignUp.SignUpActivity;
import com.example.timememo.R;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView title = null;
    private Button start = null;
    private Button edit = null;
    private Button save = null;
    private Button delete = null;
    private EditText prompt_time = null;
    private EditText prompt_title = null;
    private EditText prompt_type = null;
    private LinearLayout prompt_linel = null;
    private View prompt_chooseColor = null;

    private String colorHex = null;


    private Backlog backlog = null;
    public static final int DIALGE_ID = 0;

    private String nowTime = null;
    private String url = "http://192.168.137.1:8000/backlog";




    //获取 Backlog
    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }

    public Backlog getBacklog(){
        return backlog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prompt_layout, container,false);

        start = rootView.findViewById(R.id.prompt_start);
        edit = rootView.findViewById(R.id.prompt_edit);
        save = rootView.findViewById(R.id.prompt_save);
        delete = rootView.findViewById(R.id.prompt_delete);
        prompt_linel = rootView.findViewById(R.id.prompt_linel);
        prompt_chooseColor = rootView.findViewById(R.id.prompt_chooseColor);
        prompt_chooseColor.setBackgroundColor(Color.parseColor(backlog.getRgb()));

        title = rootView.findViewById(R.id.BacklogTitle);

        prompt_title = rootView.findViewById(R.id.prompt_title);
        prompt_title.setText(backlog.getTitle());
        prompt_type = rootView.findViewById(R.id.prompt_type);
        prompt_type.setText(backlog.getBacklogType());
        title.setText(backlog.getTitle());
        prompt_time = rootView.findViewById(R.id.prompt_time);
        prompt_time.setText(backlog.getTime());

        colorHex = backlog.getRgb();

        start.setOnClickListener(this);
        edit.setOnClickListener(this);
        save.setOnClickListener(this);
        delete.setOnClickListener(this);
        prompt_chooseColor.setOnClickListener(this);

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.prompt_start:
                //获取系统时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
                //获取当前时间
                Date date = new Date(System.currentTimeMillis());
                nowTime = simpleDateFormat.format(date);
                Log.d("time",nowTime);
                Intent intent = new Intent(getActivity(),CountDownActivity.class);
                intent.putExtra("time",backlog.getTime());
                intent.putExtra("backlog_uid",backlog.getUid());
                intent.putExtra("backlog_title",backlog.getTitle());
                intent.putExtra("backlog_type",backlog.getBacklogType());
                intent.putExtra("starttime",nowTime);
                startActivity(intent);

                break;
            case R.id.prompt_edit:
                prompt_linel.setVisibility(View.VISIBLE);
                break;
            case R.id.prompt_save:
                String title = prompt_title.getText().toString();
                String time = prompt_time.getText().toString();
                String type = prompt_type.getText().toString();
                if (title.isEmpty()||time.isEmpty()||type.isEmpty()){
                    Toast.makeText(getActivity(),"  请将信息填写完整",Toast.LENGTH_SHORT).show();
                } else {
                    final String json = "{"+
                            "\"id\":"+backlog.getId() +
                            ",\"title\":"+"\""+title +"\""+
                            ",\"time\":"+ time +
                            ",\"rgb\":"+"\""+colorHex +"\""+
                            ",\"backlogType\":"+"\""+type +"\""+
                            ",\"uid\":"+backlog.getUid() +
                            "}";
                    Log.d("json:",json);
                    MediaType JSON1 = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody1=RequestBody.create(JSON1,json);
                    HttpUtil.SendOkhttpRequest_POST(url, requestBody1, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();

                            Log.d("response",responseData);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                Toast.makeText(getActivity(),"待办信息保存成功",Toast.LENGTH_SHORT).show();
                                dismiss();
                                }
                            });
                        }
                    });
                }
                break;
            case R.id.prompt_delete:
                //删除数据库中的数据的post请求
                String url_d = "http://192.168.137.1:8000/backlog/"+backlog.getId()+"/";
                final String json = "";
                Log.d("json:",json);
                MediaType JSON2 = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody2=RequestBody.create(JSON2,json);
                HttpUtil.SendOkhttpRequest_POST(url_d, requestBody2, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"待办删除成功",Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        });
                    }
                });
                break;
            case R.id.prompt_chooseColor:
                opeAdvancenDialog();
                break;
                default:
                    break;
        }
    }

    private void opeAdvancenDialog() {
        int color = Color.parseColor(backlog.getRgb());
        //传入的默认color
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder().setColor(color)
                .setDialogTitle(R.string.choose_color)
            //设置dialog标题
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
            //设置为自定义模式
                .setShowAlphaSlider(true)
            //设置有透明度模式，默认没有透明度
                .setDialogId(DIALGE_ID)
            //设置Id,回调时传回用于判断
                .setAllowPresets(false)
            //不显示预知模式
                .create();
            //Buider创建
        colorPickerDialog.setColorPickerDialogListener(pickerDialogListener);
        //设置回调，用于获取选择的颜色
        colorPickerDialog.show(getActivity().getFragmentManager(), "color-picker-dialog");
    }

    private ColorPickerDialogListener pickerDialogListener = new ColorPickerDialogListener() {
        @Override
        public void onColorSelected(int dialogId, @ColorInt int color) {
            if (dialogId == DIALGE_ID) {
                prompt_chooseColor.setBackgroundColor(color);
                colorHex = String.format("#%x",color);
            }
        }

        @Override
        public void onDialogDismissed(int dialogId) {

        }
    };
}
