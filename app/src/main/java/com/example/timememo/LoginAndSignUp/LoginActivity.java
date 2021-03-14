package com.example.timememo.LoginAndSignUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timememo.HttpRequest.HttpUtil;
import com.example.timememo.MainActivity;
import com.example.timememo.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.example.timememo.Entity.User;
import com.google.gson.reflect.TypeToken;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailEt = null;
    private EditText passwordEt = null;
    private Button signUpBt = null;
    private Button signInBt = null;
    private CheckBox checkBox = null;
    private List<User> userList = new ArrayList<>();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    private final String url = "http://192.168.137.1:8000/person.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_layout);
        super.onCreate(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        signUpBt = findViewById(R.id.signUpBt);
        signInBt = findViewById(R.id.signInBt);
        checkBox = findViewById(R.id.checkbox);

        signInBt.setOnClickListener(this);
        signUpBt.setOnClickListener(this);
        boolean isRemenber=pref.getBoolean("remember_password",false);

        if (isRemenber){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            emailEt.setText(account);
            passwordEt.setText(password);
            checkBox.setChecked(true);
        }
    }


    //解析Json数据格式
    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        userList = gson.fromJson(jsonData,new TypeToken<List<User>>(){}.getType());
        for (User user: userList){
            Log.d("LoginActivity","id: "+user.getId());
            Log.d("LoginActivity","name: "+user.getName());
            Log.d("LoginActivity","emial: "+user.getEmail());
            Log.d("LoginActivity","password: "+user.getPassword());
            Log.d("LoginActivity","signature: "+user.getSignature());
        }
    }

    private boolean isExisted(String email,String password){
        for (User user : userList){
            if (user.getEmail().equals(email)){
                if (user.getPassword().equals(password)){
                    editor = pref.edit();
                    if (checkBox.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",email);
                        editor.putString("password",password);
                    }else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(this,MainActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    this.finish();
                }
                else Toast.makeText(LoginActivity.this,"password is error",Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //登陆
            case R.id.signInBt:
                HttpUtil.sendOkHttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        parseJSONWithGSON(responseData);

                        //创建更新UI的线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!isExisted(emailEt.getText().toString(),passwordEt.getText().toString())){
                                    Toast.makeText(LoginActivity.this,"user is not existence",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                break;
            //注册
            case R.id.signUpBt:
                Intent intent = new Intent(this,SignUpActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}
