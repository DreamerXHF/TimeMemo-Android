package com.example.timememo.Backlog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timememo.BlankFragment;
import com.example.timememo.Entity.Backlog;
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
import okhttp3.Response;

public class BacklogFragment extends BlankFragment {
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    private RecyclerView backupView = null;

    private List<Backlog> backlogList = new ArrayList<>();

    private List<Backlog> mybacklogList = new ArrayList<>();

    private User muser = null;

    private BacklogAdapter adapter;

    private SwipeRefreshLayout swipeRefresh;


    private String url = "http://192.168.137.1:8000/backlog.json";


    public BacklogAdapter getAdapter() {
        return adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.backlog_layout, container, false);
            //HttpRequest();
            refreshBacklog();
            //  initView();
            isPrepared = true;
//        实现懒加载
            lazyLoad();
        }
        //缓存的mView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }

    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        mybacklogList.clear();
        muser = getArguments().getParcelable("user");
        backlogList = gson.fromJson(jsonData,new TypeToken<List<Backlog>>(){}.getType());
        for (Backlog backlog: backlogList){
            Log.d("BacklogFragment","title: "+backlog.getTitle());
            Log.d("BacklogFragment","time: "+backlog.getTime());
            Log.d("BacklogFragment","rgb: "+backlog.getRgb());
            Log.d("BacklogFragment","type: "+backlog.getBacklogType());
        }
        for (Backlog backlog : backlogList){
            if (backlog.getUid() == muser.getId()){
                mybacklogList.add(backlog);
            }
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        swipeRefresh = mView.findViewById(R.id.swip_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBacklog();
            }
        });
        backupView = (RecyclerView)mView.findViewById(R.id.backlogView);
        backupView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new BacklogAdapter(getActivity(),mybacklogList,getFragmentManager(),muser);
        backupView.setAdapter(adapter);
    }

    //更新列表数据
    private void refreshBacklog() {
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseJSONWithGSON(responseData);
                //Log.d("responseData:",responseData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

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
        BacklogFragment fragment = new BacklogFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
}
