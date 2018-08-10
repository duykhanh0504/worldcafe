package com.aseanfan.worldcafe.UI;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.FollowModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.Service.SyncDataService;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.UI.Adapter.FollowAdapter;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ListFollowActivity extends Activity implements FollowAdapter.ClickListener {

    private RecyclerView rcyfollow;

    private ProgressBar loading;

    private List<FollowModel> listFollow = new ArrayList<>();

    private FollowAdapter mAdapter;

    public void Listfollow(Long accountid)
    {
        String url =  String.format(RestAPI.GET_LISTFOLLOW, accountid, Store.getStringData(this,Store.ACCESSTOKEN));
        loading.setVisibility(View.VISIBLE);

        RestAPI.GetDataMasterWithToken(this,url, new RestAPI.RestAPIListenner() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_follow);

        rcyfollow = (RecyclerView)this.findViewById(R.id.listFollow);
        loading = (ProgressBar) this.findViewById(R.id.loading);

        mAdapter = new FollowAdapter(null);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcyfollow.setLayoutManager(mLayoutManager);
        rcyfollow.setItemAnimator(new DefaultItemAnimator());
        rcyfollow.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

        Listfollow(AccountController.getInstance().getAccount().getId());
    }


    @Override
    public void onItemClick(int position, View v) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_id",listFollow.get(position).getAccount_id());
        intent.putExtra("avatarurl",listFollow.get(position).getAvarta());
        startActivity(intent);
        finish();
    }
}
