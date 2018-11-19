package com.aseanfan.worldcafe.UI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.Service.MyFirebaseInstanceIDService;
import com.aseanfan.worldcafe.Service.SocketService;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.android.gms.ads.MobileAds;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // MobileAds.initialize(this, this.getString(R.string.banner_ad_unit_id));

        setContentView(R.layout.activity_splash_screen);


        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                DBHelper.getInstance(getApplicationContext());
              //  if(isAppOnForeground(this) == true) {
                    Cursor cursor = DBHelper.getInstance(getApplicationContext()).getAllPersons();
                    UserModel user = AccountController.getInstance().getAccount();
                    if (Store.getBooleanData(getApplicationContext(), Store.LOGGED) == true) {
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
                            user.setInterest(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_INTEREST)));
                            user.setJob(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_JOB)));
                            AccountController.getInstance().SetAccount(user);
                            cursor.moveToFirst();
                            while (cursor.isAfterLast() == false) {
                                Long i = cursor.getLong(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_ID));
                                cursor.moveToNext();
                            }
                        }

                        startService(new Intent(getApplicationContext(), SocketService.class));

                        startService(new Intent(getApplicationContext(), MyFirebaseInstanceIDService.class));
                    }

                    Album.initialize(AlbumConfig.newBuilder(getApplicationContext())
                            .setAlbumLoader(new MediaLoader())
                            .setLocale(Locale.getDefault())
                            .build()
                    );
               // }

                startActivity(new Intent(SplashScreen.this, IntroActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);

    }
}
