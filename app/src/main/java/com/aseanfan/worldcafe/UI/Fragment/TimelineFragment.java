package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Adapter.PostTimelineAdapter;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.UI.PostTimeLineActivity;
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

public class TimelineFragment extends android.support.v4.app.Fragment implements PostTimelineAdapter.ClickListener{

    private RecyclerView list_post;

    private PostTimelineAdapter mAdapter;

    private List<PostTimelineModel> timeline;

    private TextView txtPost;

    private boolean isloading = false;



    public void LoadListTimeLinePost()
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("index",0);

        RestAPI.PostDataMaster(getActivity().getApplicationContext(),dataJson,RestAPI.GET_LISTPOSTTIMELINE, new RestAPI.RestAPIListenner() {
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
                    timeline = gson.fromJson(jsonArray, type);
                    mAdapter.setPostList(timeline);

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
        txtPost = (TextView) view.findViewById(R.id.txtpost);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list_post.setLayoutManager(mLayoutManager);
        list_post.setItemAnimator(new DefaultItemAnimator());
        list_post.setAdapter(mAdapter);

        list_post.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(getContext(),"LAst",Toast.LENGTH_LONG).show();

                }
            }
        });

        mAdapter.setOnItemClickListener(this);
        txtPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PostTimeLineActivity.class);
                startActivity(intent);
            }
        });

        list_post.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();;
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if(!recyclerView.canScrollVertically(1)) {
                        Toast.makeText(getContext(), "LAst", Toast.LENGTH_LONG).show();
                    }
                }

                if (((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                    if(isloading==false) {
                        Toast.makeText(getContext(), "Top", Toast.LENGTH_LONG).show();
                        LoadListTimeLinePost();
                        isloading = true;
                    }

                }


            }
        });

        LoadListTimeLinePost();

        return view;
    }

    @Override
    public void onItemClick(int position, View v, int type) {
        if(type == Constants.CLICK_IMAGE_LIKE)
        {
            if(timeline.get(position).getIslike() == 0)
            {
                timeline.get(position).setIslike(1);
                timeline.get(position).setNumberLike(timeline.get(position).getNumberLike() + 1);
            }
            else
            {

                timeline.get(position).setIslike(0);
                timeline.get(position).setNumberLike(timeline.get(position).getNumberLike() -1);

            }
            LikePost( timeline.get(position).getEventid());
            mAdapter.setPostList(timeline);

        }
        if(type == Constants.CLICK_AVATAR) {

            if(timeline.get(position).getAccountid().compareTo(AccountController.getInstance().getAccount().getId()) != 0)
            {
              //  ((MainActivity) getActivity()).callFriendPage(timeline.get(position).getEventid());

            }

        }
    }
}
