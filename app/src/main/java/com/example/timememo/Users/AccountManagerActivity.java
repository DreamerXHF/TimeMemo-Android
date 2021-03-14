package com.example.timememo.Users;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timememo.ActivityManagerApplication;
import com.example.timememo.Entity.User;
import com.example.timememo.HttpRequest.HttpUtil;
import com.example.timememo.LoginAndSignUp.LoginActivity;
import com.example.timememo.MainActivity;
import com.example.timememo.R;
import com.example.timememo.SaveUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText change_name = null;
    private EditText change_signature = null;
    private EditText change_password = null;
    private Button manage_save = null;
    private Button manage_logout = null;

    private User mUser  = null;
    private String url = "http://192.168.137.1:8000/person";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        Bundle bundle = getIntent().getBundleExtra("userbundle");
        change_name = findViewById(R.id.change_name);
        change_signature = findViewById(R.id.change_signature);
        change_password = findViewById(R.id.change_password);
        manage_logout = findViewById(R.id.manage_logout);
        manage_save = findViewById(R.id.manage_save);

        mUser = bundle.getParcelable("user");
        Log.d("AccountManager:",mUser.getName());
        change_name.setText(mUser.getName());
        change_signature.setText(mUser.getSignature());
        change_password.setText(mUser.getPassword());

        manage_save.setOnClickListener(this);
        manage_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.manage_save:
                final String name = change_name.getText().toString();
                final String signature = change_signature.getText().toString();
                String password = change_password.getText().toString();
                if (!name.isEmpty() && !password.isEmpty()){
                    final String json = "{"+
                            "\"id\":"+mUser.getId() +
                            ",\"name\":"+"\""+name+"\""+
                            ",\"email\":"+"\""+mUser.getEmail()+"\""+
                            ",\"password\":"+"\""+password +"\""+
                            ",\"signature\":"+"\""+signature +"\""+
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AccountManagerActivity.this,"用户保存成功",Toast.LENGTH_SHORT).show();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("returnName",name);
//                                    bundle.putString("returnSignature",signature);
//                                    Fragment messageFragment = MessageFragment.newInstance("hello");
//                                    messageFragment.setArguments(bundle);
                                    Map<String,String> map = new HashMap<String,String>();
                                    map.put("returnName",name);
                                    map.put("returnSignature",signature);
                                    SaveUtils.saveSettingNote(AccountManagerActivity.this,"userInfo",map);
                                    finish();
                                }
                            });
                        }
                    });
                }
                else{
                    Toast.makeText(AccountManagerActivity.this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.manage_logout:
                new AlertDialog.Builder(this)
                        .setTitle("确认")
                        .setMessage("确认退出？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(AccountManagerActivity.this, LoginActivity.class);
                                startActivity(intent);
                                ActivityManagerApplication.destoryActivity("main");
                                finish();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
                default:
                    break;
        }
    }
}
