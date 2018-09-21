package com.aseanfan.worldcafe.UI;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.NotificationCenter;
import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.NotificationModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.Service.MyFirebaseInstanceIDService;
import com.aseanfan.worldcafe.Service.SocketService;
import com.aseanfan.worldcafe.Service.SyncDataService;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.UI.Fragment.CommunityFragment;
import com.aseanfan.worldcafe.UI.Fragment.DetailCommunityFragment;
import com.aseanfan.worldcafe.UI.Fragment.DetailTimelineFragment;
import com.aseanfan.worldcafe.UI.Fragment.MemberEventFragment;
import com.aseanfan.worldcafe.UI.Fragment.MyPageDetailFragment;
import com.aseanfan.worldcafe.UI.Fragment.MypageFragment;
import com.aseanfan.worldcafe.UI.Fragment.NotifyFragment;
import com.aseanfan.worldcafe.UI.Fragment.RequestMemberFragment;
import com.aseanfan.worldcafe.UI.Fragment.SettingFragment;
import com.aseanfan.worldcafe.UI.Fragment.TimelineFragment;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements NotificationCenter.NotificationCenterDelegate {

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
    private String TAG_REQUEST_MEMBER = "request_member";
    private String TAG_MEMBEREVENT = "member_of_event";
    private String TAG_TIMELINE_DETAIL = "timeline_detail";

    private DetailCommunityFragment detailcomunityfragment;
    private RequestMemberFragment requestMemberFragment;
    private MemberEventFragment memberEventFragment;
    private DetailTimelineFragment detailTimelineFragment;


    private MypageFragment friendPage;

    private Socket mSocket ;

    private Toolbar mToolbar;

    private SearchView searchView;
    private  BottomNavigationView navigation;
    private View badge ;
    private LocalBroadcastManager mLocalBroadcastManager;

   /* private Emitter.Listener onConnectError = new Emitter.Listener() {
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
    };*/

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
      //  NotificationCenter.getInstance().addObserver(this, NotificationCenter.callbackEventDetail);

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

       // App app = (App) getApplication();
       // mSocket = app.getSocket();

       /* mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Socket.EVENT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_DISCONNECT, onConnectError);
        mSocket.on(Socket.EVENT_RECONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, onConnectError);*/

        int fromlogin = getIntent().getIntExtra("fromlogin" , 0);

        communityFragment = new CommunityFragment();
        timelineFragment = new TimelineFragment();
        mypageFragment = new MypageFragment();
        notifyFragment = new NotifyFragment();
        settingFragment = new SettingFragment();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, bottomNavigationMenuView, false);
        itemView.addView(badge);
        if(DBHelper.getInstance(this).checkNotify() > 0) {
           badge.setVisibility(View.VISIBLE);
        }
        else
        {
            badge.setVisibility(View.GONE);
        }

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SEND_PUSH);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);

        showFirstFragment();

        SyncDataService.listmessage(AccountController.getInstance().getAccount().getId(),getApplicationContext());
        SyncDataService.syncPush(AccountController.getInstance().getAccount().getId(),getApplicationContext(), fromlogin);

       // new SyncDataService(getApplicationContext()).execute(AccountController.getInstance().getAccount().getId());

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }*/

    private final BroadcastReceiver mBroadcastReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.SEND_PUSH)) {
                checkbadge();

            }
        }
    };


    public void gotoMypage()
    {
        Menu menu = navigation.getMenu();
        mOnNavigationItemSelectedListener.onNavigationItemSelected(menu.findItem(R.id.navigation_mypage));

        navigation.setSelectedItemId(R.id.navigation_mypage);
        showThirdFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_MYPAGE);
        fragment.onActivityResult(requestCode, resultCode, data);
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
        timelineFragment.onResume();
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
        DBHelper.getInstance(this).updateStatusNotify();
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

    public void checkbadge()
    {
        if(DBHelper.getInstance(this).checkNotify() > 0) {
            badge.setVisibility(View.VISIBLE);
        }
        else
        {
            badge.setVisibility(View.GONE);
        }

    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if(id == NotificationCenter.callbacksearch)
        {
            String i = (String)args[0];
        }

    }

    public void callMemberRequest(EventModel eventid) {
        requestMemberFragment = new RequestMemberFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("eventid",eventid.getEventid());
        requestMemberFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, requestMemberFragment,TAG_REQUEST_MEMBER).commit();

    }

    public void callMemberEvent(EventModel eventid) {
        memberEventFragment = new MemberEventFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("eventid",eventid.getEventid());
        memberEventFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, memberEventFragment,TAG_MEMBEREVENT).commit();

    }

    public void callDetailTimeline(PostTimelineModel timeline) {
        detailTimelineFragment = new DetailTimelineFragment();
        Bundle bundle = new Bundle();

        ArrayList<String> listimage = new ArrayList<>(timeline.getUrlImage().size());
        listimage.addAll(timeline.getUrlImage());

        bundle.putLong("timelineid",timeline.getTimelineid());
        bundle.putString("username",timeline.getUsername());
        bundle.putString("detail",timeline.getDetail());
        bundle.putInt("numberlike",timeline.getNumberLike());
        bundle.putInt("numbercomment",timeline.getNumberComment());
        bundle.putInt("islike",timeline.getIslike());
        bundle.putLong("accountid",timeline.getAccountid());
        bundle.putStringArrayList("listimage", listimage);
        detailTimelineFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, detailTimelineFragment,TAG_TIMELINE_DETAIL).commit();

    }

    public void callDetailEvent(EventModel eventid) {

          detailcomunityfragment = new DetailCommunityFragment();
          Bundle bundle = new Bundle();
          bundle.putLong("eventid",eventid.getEventid());
          bundle.putString("title",eventid.getTitle());
          bundle.putString("content",eventid.getContent());
          bundle.putInt("isJoin",eventid.getIsjoin());
          bundle.putInt("type",eventid.getType());
          bundle.putString("startime",eventid.getStarttime());
          bundle.putString("updatetime", eventid.getUpdatetime());
          bundle.putString("place",eventid.getCityname());
          bundle.putInt("numberlike",eventid.getNumberLike());
          bundle.putInt("numbercomment",eventid.getNumberComment());
          bundle.putString("avatar",eventid.getUrlAvatar());
          bundle.putString("username",eventid.getUsername());
          bundle.putLong("accountid",eventid.getAccountid());
          bundle.putInt("number",eventid.getNumber());
          bundle.putInt("pertime",eventid.getPertime());
          bundle.putInt("limitperson",eventid.getLimitpersons());
          bundle.putString("note",eventid.getNote());
          bundle.putInt("islike",eventid.getIslike());

          if(eventid.getUrlImage() !=null && eventid.getUrlImage().size() >0) {
            bundle.putString("image", eventid.getUrlImage().get(0));
          }

          detailcomunityfragment.setArguments(bundle);
          FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
          ft.add(R.id.content, detailcomunityfragment,TAG_COMMUNITY_DETAIL).commit();
        //  communityFragment.onPause();
        //  ft.show(detailcomunityfragment);
        //  ft.hide(communityFragment);
    }

    @Override
    public void onResume() {
        super.onResume();

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        if (f instanceof MypageFragment)
        {
            int i=0;
        }
    }

    public void BackKey()
    {
        ViewDialog dialog = new ViewDialog();
        dialog.showDialog(MainActivity.this, "Are you sure exit", new ViewDialog.DialogListenner() {
            @Override
            public void OnClickConfirm() {
                finish();
                System.exit(0);
            }
        });
    }
    public void GoToback()
    {   Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        getSupportFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        if ( f instanceof CommunityFragment||
             f instanceof TimelineFragment|| f instanceof NotifyFragment|| f instanceof SettingFragment)
        {
            BackKey();
        }
        else if(f instanceof MypageFragment)
        {
           /// NotificationCenter.getInstance().postNotificationName(NotificationCenter.mypagebackpress);
            if(getSupportFragmentManager().getFragments().size() <=2)
            {
                BackKey();
            }
            else
            {
                getSupportFragmentManager().beginTransaction().remove(f).commit();
                getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size()-2).onResume();
            }
        }
        else if(f instanceof DetailTimelineFragment ||f instanceof DetailCommunityFragment  ){

            getSupportFragmentManager().beginTransaction().remove(f).commit();

        }
        //Execute your code here
      /*  if( _viewfliper.getDisplayedChild () == Constants.PAGE_LOGIN ||
                _viewfliper.getDisplayedChild () == Constants.PAGE_REGISTER)
        {
            Intent intent = new Intent(this , IntroActivity.class);
            startActivity(intent);
        }
        finish();*/

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
