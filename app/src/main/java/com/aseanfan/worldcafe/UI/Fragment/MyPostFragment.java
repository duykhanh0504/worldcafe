package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

    private Long account_id;

    private int current_pos;


    public static MyPostFragment newInstance() {
        MyPostFragment firstFrag = new MyPostFragment();
        return firstFrag;
    }

    public void loadmore(Long account_id)
    {
        current_pos++;
        LoadListMyPost(account_id);
    }

    public void setData(List<PostTimelineModel> data)
    {
        posttimeline =data;
        Adapter.setPostList(posttimeline);

        Adapter.notifyDataSetChanged();
    }

    public void DeletePost(final int pos)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("newfeed_id", posttimeline.get(pos).getTimelineid());
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_DELETE_TIMELINE, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    posttimeline.remove(pos);
                    Adapter.setPostList(posttimeline);

                }
                catch (Exception ex) {
                }
            }
        });
    }

    public void EditPost(Long account_id)
    {

    }

    public void openOptionMenu(View v,final int position){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.menu_item_timline, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.item_delete:
                            DeletePost(position);
                            break;
                        case R.id.item_edit:

                            break;
                    }
                return true;
            }
        });
        popup.show();
    }

    public void LoadListMyPost(Long account_id)
    {
        String url =  String.format(RestAPI.GET_LISTPOSTMYPAGE,account_id,current_pos);

        RestAPI.GetDataMaster(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
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
                    if(current_pos == 0) {
                        posttimeline = gson.fromJson(jsonArray, type);
                        Adapter.setPostList(posttimeline);
                    }
                    else
                    {
                        List<PostTimelineModel> temp  = new ArrayList<>();
                        temp = gson.fromJson(jsonArray, type);
                        if(temp.size()>0) {
                            posttimeline.addAll(temp);
                            Adapter.setPostList(posttimeline);
                        }
                        else
                        {
                            current_pos--;
                        }
                    }


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
        Bundle bundle = this.getArguments();
        current_pos = 0;

        if(bundle != null){
            account_id = bundle.getLong("chat_id");
        }
        else
            account_id = AccountController.getInstance().getAccount().getId();

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
                        loadmore(account_id);
                        isloading = true;
                    }
                }

                if (((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                    if(isloading==false) {
                        current_pos = 0;
                      //  Toast.makeText(getContext(), "Top", Toast.LENGTH_LONG).show();
                        LoadListMyPost(account_id);
                        isloading = true;
                    }

                }


            }
        });
      // LoadListMyPost(account_id);

        return view;
    }

    public void LikePost(Long Postid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("newfeed_id",Postid);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_LIKEPOST, new RestAPI.RestAPIListenner() {
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
            LikePost( posttimeline.get(position).getTimelineid());
            Adapter.setPostList(posttimeline);

        }
        else if(type == Constants.CLICK_IMAGE_MENU)
        {
            openOptionMenu(v,position);
        }

    }
}