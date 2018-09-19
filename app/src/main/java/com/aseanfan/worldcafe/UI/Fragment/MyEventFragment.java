package com.aseanfan.worldcafe.UI.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Helper.NotificationCenter;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.Adapter.FragmentEventPageAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerEventAdapter;
import com.aseanfan.worldcafe.UI.CommentEventActivity;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyEventFragment  extends Fragment implements NotificationCenter.NotificationCenterDelegate {

    List<EventModel> listEvent;
    RecyclerView recycleviewevent;
    private CommunityAdapter mAdapter;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void LikeEvent(Long Eventid, final int pos)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("event_id",Eventid);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_LIKEEVENT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    if(listEvent.get(pos).getIslike() ==0) {
                        listEvent.get(pos).setNumberLike(listEvent.get(pos).getNumberLike()+1);
                        listEvent.get(pos).setIslike(1);

                    }
                    else
                    {
                        listEvent.get(pos).setNumberLike(listEvent.get(pos).getNumberLike()-1);
                        listEvent.get(pos).setIslike(0);
                    }
                    mAdapter.setData(listEvent);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //do when hidden
        } else {
            //do when show
        }
    }


    public void LoadListEvent()
    {
        String url =  String.format(RestAPI.GET_LISTMYEVENT, AccountController.getInstance().getAccount().getId(),0);

        url = url + "&is_my_event=" + 1;

        RestAPI.GetDataMasterWithToken(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);
                        return;
                    }
                    if(RestAPI.checkExpiredtoken(s))
                    {
                        ViewDialog dialog = new ViewDialog();
                        dialog.showDialogOK(getActivity(), "invalid token", new ViewDialog.DialogListenner() {
                            @Override
                            public void OnClickConfirm() {
                                App.mApp.Logout();
                            }
                        });

                        return;
                    }

                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<EventModel>>(){}.getType();
                    listEvent = gson.fromJson(jsonArray, type);
                    mAdapter.setData(listEvent);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myevent, container, false);


        listEvent = new ArrayList<>();

        LoadListEvent();

        mAdapter = new CommunityAdapter(null);
        recycleviewevent = (RecyclerView) view.findViewById(R.id.list_event);;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recycleviewevent.setLayoutManager(mLayoutManager);
        recycleviewevent.setItemAnimator(new DefaultItemAnimator());
        recycleviewevent.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CommunityAdapter.ClickListener() {


            @Override
            public void onItemClick(int position, View v,int Type, List<EventModel> event, int pos) {
                if(Type == Constants.CLICK_EVENT) {
                    ((MainActivity)activity).callDetailEvent(event.get(pos));
                }
                if(Type == Constants.CLICK_IMAGE_LIKE) {
                    // ((MainActivity)getActivity()).callDetailEvent(event);
                    LikeEvent(event.get(pos).getEventid(), position);
                }
                if(Type == Constants.CLICK_IMAGE_COMMENT) {
                    // ((MainActivity)getActivity()).callDetailEvent(event);
                    Intent intent = new Intent(getActivity(), CommentEventActivity.class);
                    intent.putExtra("Event_id",event.get(pos).getEventid());
                    startActivity(intent);
                    //  ((MainActivity)getActivity()).callDetailEvent(event);
                }
            }
        });


        return view;
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if(id == NotificationCenter.callbacksearch)
        {
            String i = (String)args[0];
        }

    }


}

