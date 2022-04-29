package com.hua.myapplication;


import android.app.Application;

class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //OkGo.getInstance().getInit(this);
    }
}
