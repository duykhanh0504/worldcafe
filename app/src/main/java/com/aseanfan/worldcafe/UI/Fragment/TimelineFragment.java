package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Adapter.PostTimelineAdapter;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends android.support.v4.app.Fragment {

    private RecyclerView list_post;

    private PostTimelineAdapter mAdapter;

    private List<PostTimelineModel> timeline;


    public void LoadListMyPost()
    {
        RestAPI.GetDataMaster(getActivity().getApplicationContext(),RestAPI.GET_LISTPOSTMYPAGE, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<PostTimelineModel>>(){}.getType();
                    timeline = gson.fromJson(jsonArray, type);
                    mAdapter.setPostList(timeline);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public static TimelineFragment newInstance() {
        TimelineFragment firstFrag = new TimelineFragment();
        return firstFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        timeline = new ArrayList<>();

        mAdapter = new PostTimelineAdapter(null);
        list_post = (RecyclerView) view.findViewById(R.id.listtimeline);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list_post.setLayoutManager(mLayoutManager);
        list_post.setItemAnimator(new DefaultItemAnimator());
        list_post.setAdapter(mAdapter);

        LoadListMyPost();
        return view;
    }
}
