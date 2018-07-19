package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

public class MyPostFragment  extends android.support.v4.app.Fragment implements PostTimelineAdapter.ClickListener {

    RecyclerView list_mypost;

    private PostTimelineAdapter Adapter;

    List<PostTimelineModel> posttimeline;

    private boolean isloading = false;



    public static MyPostFragment newInstance() {
        MyPostFragment firstFrag = new MyPostFragment();
        return firstFrag;
    }

    public void setData(List<PostTimelineModel> data)
    {
        posttimeline =data;
        Adapter.setPostList(posttimeline);

        Adapter.notifyDataSetChanged();
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
                    Adapter.setPostList(posttimeline);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isloading = false;
                        }
                    }, 3000);


                }
                catch (Exception ex) {

                    ex.printStackTrace();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isloading = false;
                        }
                    }, 3000);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_mypage, container, false);

        posttimeline = new ArrayList<>();

        Adapter = new PostTimelineAdapter(null);
        Adapter.setOnItemClickListener(this);
        list_mypost = (RecyclerView) view.findViewById(R.id.list_post_mypage);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list_mypost.setLayoutManager(mLayoutManager);
        list_mypost.setItemAnimator(new DefaultItemAnimator());
        list_mypost.setAdapter(Adapter);

        list_mypost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();;
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if(!recyclerView.canScrollVertically(1)) {
                       // Toast.makeText(getContext(), "LAst", Toast.LENGTH_LONG).show();
                    }
                }

                if (((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                    if(isloading==false) {
                      //  Toast.makeText(getContext(), "Top", Toast.LENGTH_LONG).show();
                        LoadListMyPost();
                        isloading = true;
                    }

                }


            }
        });
       // LoadListMyPost();

        return view;
    }

    public void LikePost(Long Postid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("newfeed_id",Postid);

        RestAPI.PostDataMaster(getActivity().getApplicationContext(),dataJson,RestAPI.POST_LIKEPOST, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position, View v, int type) {
        if(type == Constants.CLICK_IMAGE_LIKE)
        {
            if(posttimeline.get(position).getIslike() == 0)
            {
                posttimeline.get(position).setIslike(1);
                posttimeline.get(position).setNumberLike(posttimeline.get(position).getNumberLike() + 1);
            }
            else
            {

                posttimeline.get(position).setIslike(0);
                posttimeline.get(position).setNumberLike(posttimeline.get(position).getNumberLike() -1);

            }
            LikePost( posttimeline.get(position).getEventid());
            Adapter.setPostList(posttimeline);

        }
    }
}