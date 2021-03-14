package com.example.timememo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timememo.Backlog.BacklogAdapter;
import com.example.timememo.HttpRequest.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class addBacklogActivity extends AppCompatActivity {
    private EditText add_title = null;
    private EditText add_time = null;
    private EditText add_type = null;
    private Button add_add  = null;

    private String url = "http://192.168.137.1:8000/backlog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_backlog);
        add_title = findViewById(R.id.add_title);
        add_time = findViewById(R.id.add_time);
        add_type = findViewById(R.id.add_type);
        add_add = findViewById(R.id.add_add);

        add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = add_title.getText().toString();
                String time = add_time.getText().toString();
                String type = add_type.getText().toString();
                int id = getIntent().getIntExtra("userID",-1);
                if (id == -1){
                    Toast.makeText(addBacklogActivity.this,"没有此用户",Toast.LENGTH_SHORT).show();

                }else {
                    Log.d("title:",title);
                    if(!title.isEmpty() && !time.isEmpty() && !type.isEmpty()){
                        final String json = "{"+
                                "\"title\":"+"\""+title +"\""+
                                ",\"time\":"+"\""+time +"\""+
                                ",\"backlogType\":"+"\""+type +"\""+
                                ",\"uid\":" + id +
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(addBacklogActivity.this,"待办添加成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                    else{
                        Toast.makeText(addBacklogActivity.this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
