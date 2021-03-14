package com.example.timememo.LockPhone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeListener {
    public static HomeKeyListener getInstance() {
        HomeKeyListener listener = HomeKeyListener.sListener;
        listener.init();
        return listener;
    }

    static class HomeKeyListener {
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";//home键旁边的最近程序列表键
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";//按下home键
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";//某些三星手机的程序列表键

        private static AtomicBoolean isDestroy = new AtomicBoolean(true);
        private static AtomicBoolean isRegister = new AtomicBoolean(false);
        private static HomeKeyListener sListener = new HomeKeyListener();
        private List<HomePressListener> mPressListeners = new ArrayList<HomePressListener>();
        private HomeReceiver mReceiver;
        private IntentFilter mHomeFileter;
        private HomePressListener mHomePressListener;
        public void init() {
            if (isDestroy.get()) {
                this.mPressListeners = new ArrayList<HomePressListener>();
                this.mReceiver = new HomeReceiver();
                this.mHomeFileter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                isDestroy.set(false);
            }
        }

        public synchronized void start(Context context) {
            if (!isRegister.get()) {
                context.registerReceiver(mReceiver, mHomeFileter);
                isRegister.set(true);
            }
        }

        public synchronized void stop(Context context) {
            if (isRegister.get()) {
                context.unregisterReceiver(mReceiver);
                isRegister.set(false);
            }
        }

        public HomeListener setHomeKeylistener(HomePressListener listener) {
            this.mHomePressListener = listener;
            return null;
        }

        public void addHomeKeyListener(HomePressListener listener) {
            mPressListeners.add(listener);
        }

        public void removeHomeKeyListener(HomePressListener listener) {
            mPressListeners.add(listener);
        }

        public void removeAllHomeKeyListener() {
            mPressListeners.clear();
        }

        public void destroy() {
            this.mPressListeners.clear();
            this.mPressListeners = null;
            this.mReceiver = null;
            this.mHomeFileter = null;
            this.mHomePressListener = null;
            isDestroy.set(true);
        }

        public interface HomePressListener {
            void onHomePress();
            void onHomeRecentAppsPress();
        }

        class HomeReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                receive(intent);
            }
        }

        private void receive(Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra( "reason" );
                dispatchPress(reason);
            }
        }

        private void dispatchPress(String reason) {
            switch (reason) {
                case SYSTEM_DIALOG_REASON_HOME_KEY:
                    if (mHomePressListener != null) mHomePressListener.onHomePress();
                    for (HomePressListener listener : mPressListeners) listener.onHomePress();
                    break;

                case SYSTEM_DIALOG_REASON_RECENT_APPS:
                case SYSTEM_DIALOG_REASON_ASSIST:
                    if (mHomePressListener != null) mHomePressListener.onHomeRecentAppsPress();
                    for (HomePressListener listener : mPressListeners) listener.onHomeRecentAppsPress();
                    break;

            }
        }
    }
}