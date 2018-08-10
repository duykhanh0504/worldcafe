package com.aseanfan.worldcafe.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.ChatModel;
import com.aseanfan.worldcafe.Model.FollowModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.UI.Adapter.ConctactChatAdapter;
import com.aseanfan.worldcafe.UI.Adapter.FollowAdapter;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ContactChatActivity extends AppCompatActivity implements ConctactChatAdapter.ClickListener {


    private RecyclerView rcycontactchat;

    private ProgressBar loading;

    private List<ChatModel> listcontactchat = new ArrayList<>();

    private ConctactChatAdapter mAdapter;


    public void Listfollow(Long accountid)
    {
        String url =  String.format(RestAPI.GET_CONTACT_CHAT, accountid);
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
                    java.lang.reflect.Type type = new TypeToken<List<ChatModel>>(){}.getType();
                    listcontactchat = gson.fromJson(jsonArray, type);
                    mAdapter.setFollowlist(listcontactchat);

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
        setContentView(R.layout.activity_contact_chat);
        rcycontactchat = (RecyclerView)this.findViewById(R.id.listFollow);
        loading = (ProgressBar) this.findViewById(R.id.loading);

        mAdapter = new ConctactChatAdapter(null);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcycontactchat.setLayoutManager(mLayoutManager);
        rcycontactchat.setItemAnimator(new DefaultItemAnimator());
        rcycontactchat.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

        Listfollow(AccountController.getInstance().getAccount().getId());
    }

    @Override
    public void onItemClick(int position, View v) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_id",listcontactchat.get(position).getAccount_id());
        intent.putExtra("avatarurl",listcontactchat.get(position).getAvarta());
        startActivity(intent);
        finish();
    }
}
