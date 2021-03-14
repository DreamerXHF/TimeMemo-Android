package com.example.timememo.LockPhone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timememo.BlankFragment;
import com.example.timememo.R;

public class LockFragment extends BlankFragment {
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    private TextView onem = null;
    private TextView fivem =null;
    private TextView tenm = null;
    private TextView selftime = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.lock_layout, container, false);
            initView();

            onem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   LockDialog lockDialog = new LockDialog("1");
                   lockDialog.show(getFragmentManager(),"LockDialog");
                }
            });

            fivem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LockDialog lockDialog = new LockDialog("5");
                    lockDialog.show(getFragmentManager(),"LockDialog");
                }
            });

            tenm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LockDialog lockDialog = new LockDialog("10");
                    lockDialog.show(getFragmentManager(),"LockDialog");
                }
            });

            selftime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LockDialog lockDialog = new LockDialog();
                    lockDialog.show(getFragmentManager(),"LockDialog");
                }
            });

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
    /**
     * 初始化控件
     */
    private void initView() {
        onem = mView.findViewById(R.id.onem);
        fivem = mView.findViewById(R.id.fivem);
        tenm = mView.findViewById(R.id.tenm);
        selftime = mView.findViewById(R.id.selftime);

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
        LockFragment fragment = new LockFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
}
