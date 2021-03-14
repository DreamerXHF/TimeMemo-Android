package com.example.timememo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.timememo.Backlog.BacklogFragment;
import com.example.timememo.Backlog.MyDialogFragment;
import com.example.timememo.Entity.User;
import com.example.timememo.HistoryLine.HistoryLineActivity;
import com.example.timememo.LockPhone.LockFragment;
import com.example.timememo.Statistic.TongjiFragment;
import com.example.timememo.Users.MessageFragment;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    private Toolbar toolbar = null;

    private BottomNavigationBar bottomNavigationBar = null;
    private FrameLayout frameLayout = null;

    private Fragment backlogFragment = null;
    private Fragment lockFragment = null;
    private Fragment tongjiFragment = null;
    private Fragment messageFragment = null;
    private MyDialogFragment myDialogFragment = null;

    private User myuser = null;

    Bundle bundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        frameLayout = findViewById(R.id.fragment_container);

        backlogFragment = BacklogFragment.newInstance("待办");
        lockFragment = LockFragment.newInstance("锁机");
        tongjiFragment = TongjiFragment.newInstance("统计");
        messageFragment = MessageFragment.newInstance("我的");
        myDialogFragment = new MyDialogFragment();

        myuser = getIntent().getParcelableExtra("user");

        bundle = new Bundle();
        bundle.putParcelable("user",myuser);
        messageFragment.setArguments(bundle);
        backlogFragment.setArguments(bundle);
        myDialogFragment.setArguments(bundle);
        tongjiFragment.setArguments(bundle);

        Map<String,String> map = new HashMap<>();
        map.put("userID",String.valueOf(myuser.getId()));
        SaveUtils.saveSettingNote(MainActivity.this,"userID",map);

        InitNavigationBar();
        setDefaultFragment();

        ActivityManagerApplication.addDestoryActivity(this,"main");
    }

    //toolbar 编辑
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
//        Log.d("menu","memu create");
        return true;
    }

    //toolbar动态显示
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (bottomNavigationBar.getCurrentSelectedPosition()){
            case 0:
                menu.findItem(R.id.setting).setVisible(false);
                menu.findItem(R.id.add).setVisible(true);
                break;
            case 1:
                menu.findItem(R.id.setting).setVisible(false);
                menu.findItem(R.id.add).setVisible(false);
                break;
            case 2:
                menu.findItem(R.id.setting).setVisible(false);
                menu.findItem(R.id.add).setVisible(false);
                break;
            case 3:
                menu.findItem(R.id.setting).setVisible(true);
                menu.findItem(R.id.add).setVisible(false);
                break;
                default:
                    break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //菜单响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.timeLine:
                Intent intent1 = new Intent(MainActivity.this, HistoryLineActivity.class);
                startActivity(intent1);
                break;
            case R.id.add:
                Intent intent = new Intent(MainActivity.this,addBacklogActivity.class);
                intent.putExtra("userID",myuser.getId());
                startActivity(intent);
                break;
            case R.id.setting:
                break;
                default:
                    break;
        }
        return true;
    }

    //初始化底部导航栏
    private void InitNavigationBar(){
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_backlog_g,"待办").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.ic_lock_g,"锁机").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.ic_tongji_g,"统计").setActiveColorResource(R.color.green))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_g,"我的").setActiveColorResource(R.color.purple))
                .setFirstSelectedPosition(0)
                .initialise();

    }
    /*
    * 设置默认的
    */
    private void setDefaultFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container,backlogFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
       // Log.d("onTabSeleted","onTabSeleted:"+position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position){
            case 0:
                transaction.replace(R.id.fragment_container,backlogFragment);
                toolbar.setTitle("待办");
                break;
            case 1:
                transaction.replace(R.id.fragment_container,lockFragment);
                toolbar.setTitle("锁机");
                break;
            case 2:
                transaction.replace(R.id.fragment_container,tongjiFragment);
                toolbar.setTitle("统计");
                break;
            case 3:
                transaction.replace(R.id.fragment_container,messageFragment);
                toolbar.setTitle("我的");
                break;
                default:
                    break;
        }
        //重新绘制Menu
        invalidateOptionsMenu();
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
  //      Log.d("onTabUnseleted","onTabUnseleted:"+position);
    }

    @Override
    public void onTabReselected(int position) {
  //      Log.d("onTabReseleted","onTabReseleted:"+position);
    }


}
