package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.Adapter.PostTimelineAdapter;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyPostFragment  extends android.support.v4.app.Fragment {

    RecyclerView list_mypost;

    private PostTimelineAdapter mAdapter;

    List<PostTimelineModel> posttimeline;

    public static MyPostFragment newInstance() {
        MyPostFragment firstFrag = new MyPostFragment();
        return firstFrag;
    }


    public void LoadListMyPost()
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("index",0);

        RestAPI.PostDataMaster(getActivity().getApplicationContext(),dataJson,RestAPI.GET_LISTPOSTMYPAGE, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result1");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<PostTimelineModel>>(){}.getType();
                    posttimeline = gson.fromJson(jsonArray, type);
                    mAdapter.setPostList(posttimeline);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_mypage, container, false);

        posttimeline = new ArrayList<>();

        mAdapter = new PostTimelineAdapter(null);
        list_mypost = (RecyclerView) view.findViewById(R.id.list_post_mypage);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list_mypost.setLayoutManager(mLayoutManager);
        list_mypost.setItemAnimator(new DefaultItemAnimator());
        list_mypost.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PostTimelineAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v,int type) {
                if(type == Constants.CLICK_IMAGE_LIKE)
                {
                    //posttimeline.get(position).getNumberLike() =
                }
            }
        });

        LoadListMyPost();

        return view;
    }
}