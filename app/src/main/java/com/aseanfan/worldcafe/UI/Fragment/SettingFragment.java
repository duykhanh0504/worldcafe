package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.UI.Adapter.PostTimelineAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SettingAdapter;
import com.aseanfan.worldcafe.UI.IntroActivity;
import com.aseanfan.worldcafe.UI.LoginActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.facebook.login.LoginManager;

public class SettingFragment extends android.support.v4.app.Fragment {


    Button _logout;
    private LocalBroadcastManager mLocalBroadcastManager;

    private int applicationrow =0;
    private int contactusrow = 1;
    private int changepasswordrow =2;
    private int contract =3;
    private int version =4;
    private int logout =5;
    private int count =  logout;

    private RecyclerView recyclerView;

    private SettingAdapter mAdapter;

    public static SettingFragment newInstance() {
        SettingFragment firstFrag = new SettingFragment();
        return firstFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        _logout = (Button)view.findViewById(R.id.Logout);
        final App app = (App)getActivity().getApplication();

        recyclerView = (RecyclerView) view.findViewById(R.id.listsetting);
        mAdapter = new SettingAdapter(count);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                app.Logout();
            }
        });

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getContext());

        return view;
    }

}
