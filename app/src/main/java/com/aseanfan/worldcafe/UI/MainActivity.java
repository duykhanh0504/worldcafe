package com.aseanfan.worldcafe.UI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.NotificationCenter;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.Fragment.CommunityFragment;
import com.aseanfan.worldcafe.UI.Fragment.DetailCommunityFragment;
import com.aseanfan.worldcafe.UI.Fragment.MyPageDetailFragment;
import com.aseanfan.worldcafe.UI.Fragment.MypageFragment;
import com.aseanfan.worldcafe.UI.Fragment.NotifyFragment;
import com.aseanfan.worldcafe.UI.Fragment.SettingFragment;
import com.aseanfan.worldcafe.UI.Fragment.TimelineFragment;
import com.aseanfan.worldcafe.worldcafe.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private CommunityFragment communityFragment;
    private TimelineFragment timelineFragment;
    private MypageFragment mypageFragment;
    private NotifyFragment notifyFragment;
    private SettingFragment settingFragment;
    private String TAG_COMMUNITY="community";
    private String TAG_TIMELINE="second";
    private String TAG_MYPAGE="mypage";
    private String TAG_NOTIFY="notify";
    private String TAG_SETTING="setting";

    private String TAG_COMMUNITY_DETAIL = "community_detail";
    private String TAG_FRIENDPAGE = "friend_page";

    private DetailCommunityFragment detailcomunityfragment;
    private MypageFragment friendPage;

    private Socket mSocket ;

    private Toolbar mToolbar;

    private SearchView searchView;
    private  BottomNavigationView navigation;

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "connect error", Toast.LENGTH_LONG).show();
                    mSocket.disconnect();
                 //   Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                   // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                   // getApplicationContext().startActivity(intent);
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

  /*  @Override
    public boolean  onPrepareOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_tool_bar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                NotificationCenter.getInstance().postNotificationName(NotificationCenter.callbacksearch,s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // NotificationCenter.getInstance().addObserver(this, NotificationCenter.callbackEventDetail);

      /*  mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Crosea");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {
                                                      FragmentManager ft = getSupportFragmentManager();
                                                      Fragment currentFragment = ft.findFragmentById(R.id.content);
                                                      if (currentFragment.getTag().equals(TAG_COMMUNITY_DETAIL))
                                                      {
                                                         ft.beginTransaction().show(communityFragment).commit();
                                                         ft.beginTransaction().remove(detailcomunityfragment).commit();

                                                      }
                                                  }
                                              });*/

        App app = (App) getApplication();
        mSocket = app.getSocket();

        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Socket.EVENT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_DISCONNECT, onConnectError);
        mSocket.on(Socket.EVENT_RECONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, onConnectError);


        UserModel user = AccountController.getInstance().getAccount();

        Cursor cursor = DBHelper.getInstance(getApplicationContext()).getAllPersons();
        if(cursor!=null) {
            cursor.moveToFirst();
            user.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_COLUMN_EMAIL)));
            user.setPhonenumber(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_PHONE_NUMBER)));
            user.setAvarta(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_AVATAR_URL)));
            user.setSex(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_SEX)));
            user.setBirthday(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_BIRTHDAY)));
            user.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_ADDRESS)));
            user.setDistrict(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_DISTRICT)));
            user.setCity(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_CITY)));
            user.setCountry(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_COUNTRY)));
            user.setCompany(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_COMPANY)));
            user.setSchool(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_SCHOOL)));
            user.setIntroduction(cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_INTRODUCTION)));
            AccountController.getInstance().SetAccount(user);
        }

        communityFragment = new CommunityFragment();
        timelineFragment = new TimelineFragment();
        mypageFragment = new MypageFragment();
        notifyFragment = new NotifyFragment();
        settingFragment = new SettingFragment();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        showFirstFragment();

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }*/

    public void gotoMypage()
    {
        Menu menu = navigation.getMenu();
        mOnNavigationItemSelectedListener.onNavigationItemSelected(menu.findItem(R.id.navigation_mypage));
    }

    public void showFirstFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, communityFragment, TAG_COMMUNITY);
       /* FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (communityFragment.isAdded()) {
            ft.show(communityFragment);
        } else {
            ft.add(R.id.content, communityFragment, TAG_COMMUNITY);
        }
        if (timelineFragment.isAdded()) {
            ft.hide(timelineFragment);
        }
        if (mypageFragment.isAdded()) {
            ft.hide(mypageFragment);
        }
        if (notifyFragment.isAdded()) {
            ft.hide(notifyFragment);
        }
        if (settingFragment.isAdded()) {
            ft.hide(settingFragment);
        }*/
        ft.commit();
    }

    public void showSecondFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, timelineFragment, TAG_TIMELINE);
       /* FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (timelineFragment.isAdded()) {
            ft.show(timelineFragment);
        } else {
            ft.add(R.id.content, timelineFragment, TAG_TIMELINE);
        }
        if (communityFragment.isAdded()) {
            ft.hide(communityFragment);
        }
        if (mypageFragment.isAdded()) {
            ft.hide(mypageFragment);
        }
        if (notifyFragment.isAdded()) {
            ft.hide(notifyFragment);
        }
        if (settingFragment.isAdded()) {
            ft.hide(settingFragment);
        }*/
        ft.commit();
    }

    public void showThirdFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, mypageFragment, TAG_MYPAGE);
       /* if (mypageFragment.isAdded()) {
            ft.show(mypageFragment);
        } else {
            ft.add(R.id.content, mypageFragment, TAG_MYPAGE);
        }
        if (timelineFragment.isAdded()) {
            ft.hide(timelineFragment);
        }
        if (communityFragment.isAdded()) {
            ft.hide(communityFragment);
        }
        if (notifyFragment.isAdded()) {
            ft.hide(notifyFragment);
        }
        if (settingFragment.isAdded()) {
            ft.hide(settingFragment);
        }*/
        ft.commit();
    }

    public void showfourFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, notifyFragment, TAG_NOTIFY);
      /*  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (notifyFragment.isAdded()) {
            ft.show(notifyFragment);
        } else {
            ft.add(R.id.content, notifyFragment, TAG_NOTIFY);
        }
        if (timelineFragment.isAdded()) {
            ft.hide(timelineFragment);
        }
        if (communityFragment.isAdded()) {
            ft.hide(communityFragment);
        }
        if (mypageFragment.isAdded()) {
            ft.hide(mypageFragment);
        }
        if (settingFragment.isAdded()) {
            ft.hide(settingFragment);
        }*/
        ft.commit();
    }

    public void showfifthFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, settingFragment, TAG_SETTING);
        /*
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (settingFragment.isAdded()) {
            ft.show(settingFragment);
        } else {
            ft.add(R.id.content, settingFragment, TAG_SETTING);
        }
        if (timelineFragment.isAdded()) {
            ft.hide(timelineFragment);
        }
        if (communityFragment.isAdded()) {
            ft.hide(communityFragment);
        }
        if (notifyFragment.isAdded()) {
            ft.hide(notifyFragment);
        }
        if (mypageFragment.isAdded()) {
            ft.hide(mypageFragment);
        }*/
        ft.commit();
    }


    public void callDetailEvent(int id) {

         detailcomunityfragment = new DetailCommunityFragment();
          FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
          ft.add(R.id.content, detailcomunityfragment,TAG_COMMUNITY_DETAIL).commit();
        //  ft.show(detailcomunityfragment);
        //  ft.hide(communityFragment);
    }

    public void callFriendPage(Long id) {

        Bundle bundle = new Bundle();
        bundle.putLong("account_id",id);

        friendPage = new MypageFragment();
        friendPage.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, friendPage,TAG_FRIENDPAGE).commit();
      //  ft.show(friendPage);
       // ft.hide(timelineFragment);
    }
}
