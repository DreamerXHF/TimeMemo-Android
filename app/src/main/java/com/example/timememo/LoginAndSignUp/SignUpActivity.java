package com.example.timememo.LoginAndSignUp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timememo.Entity.User;
import com.example.timememo.HttpRequest.HttpUtil;
import com.example.timememo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignUpActivity extends AppCompatActivity {
    private TextView nameSU = null;
    private TextView emailSU = null;
    private TextView passwordSU = null;
    private Button signUp = null;
    private List<User> userList = new ArrayList<>();
    private final String url = "http://192.168.137.1:8000/person";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameSU = findViewById(R.id.nameSU);
        emailSU = findViewById(R.id.emailSU);
        passwordSU = findViewById(R.id.passwordSU);
        signUp = findViewById(R.id.signUp);

        //获取用户列表
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseJSONWithGSON(responseData);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameSU.getText().toString();
                String email = emailSU.getText().toString();
                String password = passwordSU.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    if (isExisted(email)){
                        Toast.makeText(SignUpActivity.this,"user is existence",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final String json = "{"+
                                "\"name\":"+"\""+name +"\""+
                                ",\"email\":"+"\""+email +"\""+
                                ",\"password\":"+"\""+password +"\""+
                                "}";
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                        RequestBody requestBody=RequestBody.create(JSON,json);
                        HttpUtil.SendOkhttpRequest_POST(url, requestBody, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                Log.d("response",responseData);
                                // Log.d("json:",json);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignUpActivity.this,"user created successfully",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(SignUpActivity.this,"please complete infomation",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        userList = gson.fromJson(jsonData,new TypeToken<List<User>>(){}.getType());
//        for (User user: userList){
//            Log.d("LoginActivity","name: "+user.getName());
//            Log.d("LoginActivity","emial: "+user.getEmail());
//            Log.d("LoginActivity","password: "+user.getPassword());
//            Log.d("LoginActivity","signature: "+user.getSignature());
//        }
    }

    private boolean isExisted(String emial){
        for (User user:userList){
            if (user.getEmail().equals(emial)){

                return true;
            }
        }
        return false;
    }

}
