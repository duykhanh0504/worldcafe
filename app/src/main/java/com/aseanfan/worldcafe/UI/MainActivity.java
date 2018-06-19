package com.aseanfan.worldcafe.UI;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.UI.Fragment.CommunityFragment;
import com.aseanfan.worldcafe.UI.Fragment.MypageFragment;
import com.aseanfan.worldcafe.UI.Fragment.NotifyFragment;
import com.aseanfan.worldcafe.UI.Fragment.SettingFragment;
import com.aseanfan.worldcafe.UI.Fragment.TimelineFragment;
import com.aseanfan.worldcafe.worldcafe.R;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    CommunityFragment firstFragment;
    TimelineFragment secondFragment;
    MypageFragment thirdFragment;
    NotifyFragment fourFragment;
    SettingFragment fifthFragment;
    String TAG_FIRST="first";
    String TAG_SECOND="second";
    String TAG_THIRD="third";
    String TAG_FOUR="four";
    String TAG_FIFTH="fifth";

    public static Socket mSocket;
    {
        try {
            mSocket = IO.socket("8080");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "connect error", Toast.LENGTH_LONG).show();
                    MainActivity.mSocket.disconnect();
                    Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    getApplicationContext().startActivity(intent);
                }
            });
        }
    };

    private Emitter.Listener onReceiveMessgeFromServer = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_community:
                    showFirstFragment();
                    break;
                case R.id.navigation_timeline:
                    showSecondFragment();
                    break;
                case R.id.navigation_mypage:
                    showThirdFragment();
                    break;
                case R.id.navigation_notify:
                    showfourFragment();
                    break;
                case R.id.navigation_setting:
                    showfifthFragment();
                    break;
            }
            return true;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserModel user = new UserModel();

        Cursor cursor = DBHelper.getInstance(getBaseContext()).getAllPersons();
        if(cursor!=null) {
            cursor.moveToFirst();
            user.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_EMAIL)));
            user.setPhonenumber(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_PHONE_NUMBER)));
            AccountController.getInstance().SetAccount(user);
        }

        firstFragment = new CommunityFragment();
        secondFragment = new TimelineFragment();
        thirdFragment = new MypageFragment();
        fourFragment = new NotifyFragment();
        fifthFragment = new SettingFragment();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        showFirstFragment();

    }

    public void showFirstFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (firstFragment.isAdded()) {
            ft.show(firstFragment);
        } else {
            ft.add(R.id.content, firstFragment, TAG_FIRST);
        }
        if (secondFragment.isAdded()) {
            ft.hide(secondFragment);
        }
        if (thirdFragment.isAdded()) {
            ft.hide(thirdFragment);
        }
        if (fourFragment.isAdded()) {
            ft.hide(fourFragment);
        }
        if (fifthFragment.isAdded()) {
            ft.hide(fifthFragment);
        }
        ft.commit();
    }

    public void showSecondFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (secondFragment.isAdded()) {
            ft.show(secondFragment);
        } else {
            ft.add(R.id.content, secondFragment, TAG_SECOND);
        }
        if (firstFragment.isAdded()) {
            ft.hide(firstFragment);
        }
        if (thirdFragment.isAdded()) {
            ft.hide(thirdFragment);
        }
        if (fourFragment.isAdded()) {
            ft.hide(fourFragment);
        }
        if (fifthFragment.isAdded()) {
            ft.hide(fifthFragment);
        }
        ft.commit();
    }

    public void showThirdFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (thirdFragment.isAdded()) {
            ft.show(thirdFragment);
        } else {
            ft.add(R.id.content, thirdFragment, TAG_THIRD);
        }
        if (secondFragment.isAdded()) {
            ft.hide(secondFragment);
        }
        if (firstFragment.isAdded()) {
            ft.hide(firstFragment);
        }
        if (fourFragment.isAdded()) {
            ft.hide(fourFragment);
        }
        if (fifthFragment.isAdded()) {
            ft.hide(fifthFragment);
        }
        ft.commit();
    }

    public void showfourFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fourFragment.isAdded()) {
            ft.show(fourFragment);
        } else {
            ft.add(R.id.content, fourFragment, TAG_FOUR);
        }
        if (secondFragment.isAdded()) {
            ft.hide(secondFragment);
        }
        if (firstFragment.isAdded()) {
            ft.hide(firstFragment);
        }
        if (thirdFragment.isAdded()) {
            ft.hide(thirdFragment);
        }
        if (fifthFragment.isAdded()) {
            ft.hide(fifthFragment);
        }
        ft.commit();
    }

    public void showfifthFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fifthFragment.isAdded()) {
            ft.show(fifthFragment);
        } else {
            ft.add(R.id.content, fifthFragment, TAG_FIFTH);
        }
        if (secondFragment.isAdded()) {
            ft.hide(secondFragment);
        }
        if (firstFragment.isAdded()) {
            ft.hide(firstFragment);
        }
        if (fourFragment.isAdded()) {
            ft.hide(fourFragment);
        }
        if (thirdFragment.isAdded()) {
            ft.hide(thirdFragment);
        }
        ft.commit();
    }
}
