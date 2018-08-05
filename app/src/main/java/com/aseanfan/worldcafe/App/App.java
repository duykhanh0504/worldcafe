package com.aseanfan.worldcafe.App;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;


import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.Service.SocketService;
import com.aseanfan.worldcafe.UI.IntroActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.UI.MediaLoader;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;

public class App  extends Application {

    public static volatile Handler applicationHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance(getApplicationContext());
        applicationHandler = new Handler(getApplicationContext().getMainLooper());

        Cursor cursor = DBHelper.getInstance(getApplicationContext()).getAllPersons();
        UserModel user = AccountController.getInstance().getAccount();
        if(Store.getBooleanData(this , Store.LOGGED) == true) {
            if (cursor != null) {
                cursor.moveToFirst();
                user.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_EMAIL)));
                user.setPhonenumber(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_PHONE_NUMBER)));
                user.setAvarta(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_AVATAR_URL)));
                user.setSex(cursor.getInt(cursor.getColumnIndex(DBHelper.PERSON_SEX)));
                user.setBirthday(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_BIRTHDAY)));
                user.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_ADDRESS)));
                user.setDistrict(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_DISTRICT)));
                user.setCity(cursor.getInt(cursor.getColumnIndex(DBHelper.PERSON_CITY)));
                user.setCountry(cursor.getInt(cursor.getColumnIndex(DBHelper.PERSON_COUNTRY)));
                user.setCompany(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_COMPANY)));
                user.setSchool(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_SCHOOL)));
                user.setIntroduction(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_INTRODUCTION)));
                AccountController.getInstance().SetAccount(user);
            }

            startService(new Intent(getApplicationContext(), SocketService.class));
        }

        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        );



    }


    public Context getContext() {
        return this.getContext();
    }
}
