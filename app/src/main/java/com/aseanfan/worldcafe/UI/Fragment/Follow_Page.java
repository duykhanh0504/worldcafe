package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.FollowModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.UI.Adapter.FollowAdapter;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Follow_Page extends android.support.v4.app.Fragment  {

    Long accountID;

    private RecyclerView rcyfollow;

    private ProgressBar loading;

    private List<FollowModel> listFollow = new ArrayList<>();

    private FollowAdapter mAdapter;

    private int type =0;

    public void setid(Long id)
    {
        this.accountID =id;
    }

    public void setFollowType(int type)
    {
        this.type =type;
    }

    public void Listfollower(Long accountid,Long id)
    {
        String url =  String.format(RestAPI.GET_LISTFOLLOWER, accountid, id);
        loading.setVisibility(View.VISIBLE);

        RestAPI.GetDataMasterWithToken(getActivity(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<FollowModel>>(){}.getType();
                    listFollow = gson.fromJson(jsonArray, type);
                    mAdapter.setFollowlist(listFollow);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
                finally {
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    public void Listfollow(Long accountid,Long id)
    {
        String url =  String.format(RestAPI.GET_LISTFOLLOW, accountid, id);
        loading.setVisibility(View.VISIBLE);

        RestAPI.GetDataMasterWithToken(getActivity(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<FollowModel>>(){}.getType();
                    listFollow = gson.fromJson(jsonArray, type);
                    mAdapter.setFollowlist(listFollow);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
                finally {
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_follow, container, false);

        rcyfollow = (RecyclerView)view.findViewById(R.id.listFollow);
        loading = (ProgressBar) view.findViewById(R.id.loading);

        mAdapter = new FollowAdapter(null);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        rcyfollow.setLayoutManager(mLayoutManager);
        rcyfollow.setItemAnimator(new DefaultItemAnimator());
        rcyfollow.setAdapter(mAdapter);

        if(type ==0) {
            Listfollow(AccountController.getInstance().getAccount().getId(),accountID);
        }
        if(type == 1 )
        {
            Listfollower(AccountController.getInstance().getAccount().getId(),accountID);
        }

        mAdapter.setOnItemClickListener(new FollowAdapter.ClickListener() {
            @Override
            public void onItemClick(Long position, View v) {
                ((MainActivity)getActivity()).callFriendPage(position);
            }
        });


        return view;
    }

}
