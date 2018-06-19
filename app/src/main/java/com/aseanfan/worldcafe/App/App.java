package com.aseanfan.worldcafe.App;

import android.app.Application;

import com.aseanfan.worldcafe.Helper.DBHelper;

public class App  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance(this);
    }
}
