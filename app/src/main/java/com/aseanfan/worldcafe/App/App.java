package com.aseanfan.worldcafe.App;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.multidex.MultiDex;


import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.Service.MyFirebaseInstanceIDService;
import com.aseanfan.worldcafe.Service.SocketService;
import com.aseanfan.worldcafe.UI.IntroActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.UI.MediaLoader;

import com.aseanfan.worldcafe.Utils.Constants;
import com.facebook.login.LoginManager;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.util.List;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;

public class App  extends Application {

    public static volatile Handler applicationHandler;
    public static App mApp;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void Logout()
    {
        stopService(new Intent(getApplicationContext(), SocketService.class));
        Store.putBooleanData(this,Store.LOGGED,false);
        DBHelper.getInstance(this).deletePerson(AccountController.getInstance().getAccount().getId());
        DBHelper.getInstance(this).deleteTableChat();
        DBHelper.getInstance(this).deleteTableNotify();
        AccountController.getInstance().SetAccount(null);
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this , IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationHandler = new Handler(getApplicationContext().getMainLooper());
        mApp =this;
    }


    public Context getContext() {
        return this.getContext();
    }
}
