package com.aseanfan.worldcafe.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.FollowModel;
import com.aseanfan.worldcafe.Model.RequestUserModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.UI.Adapter.RequestMemberAdapter;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MemberRequestActivity extends AppCompatActivity {

    private List<RequestUserModel> listuser;

    private RecyclerView rcyview;

    private RequestMemberAdapter mAdapter;

    public void Listuser(Long accountid , Long eventid)
    {
        String url =  String.format(RestAPI.GET_LISTREQUESTMEMBER, accountid,eventid);


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
                    java.lang.reflect.Type type = new TypeToken<List<RequestUserModel>>(){}.getType();
                    listuser = gson.fromJson(jsonArray, type);
                    mAdapter.setData(listuser);


                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
                finally {

                }
            }
        });
    }

    public void AcceptRejectMember(final int pos , final int status)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("id", listuser.get(pos).getRequestId());
        dataJson.addProperty("status",status);

        RestAPI.PostDataMasterWithToken(this,dataJson,RestAPI.POST_ACCEPTREJECTMEMBER, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    listuser.get(pos).setStatus(status);
                    mAdapter.setData(listuser);
                    mAdapter.notifyDataSetChanged();
                   /* JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<UserModel>>(){}.getType();
                    listuser = gson.fromJson(jsonArray, type);
                    mAdapter.setData(listuser);*/


                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
                finally {

                }
            }
        });
    }

    void init()
    {
        listuser= new ArrayList<>();
        rcyview = (RecyclerView)this.findViewById(R.id.listRequsetmember);

        mAdapter = new RequestMemberAdapter(null);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL,false);

        //  mLayoutManager.setStackFromEnd(true);
        rcyview.setLayoutManager(mLayoutManager);
        rcyview.setItemAnimator(new DefaultItemAnimator());
        rcyview.setAdapter(mAdapter);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_request);

        init();
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {

            Listuser(AccountController.getInstance().getAccount().getId(),extras.getLong("eventid"));

        }

        mAdapter.setOnItemClickListener(new RequestMemberAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, int type) {
                if(type == Constants.MEMBER_ACCEPT)
                {
                    AcceptRejectMember(position,Constants.MEMBER_ACCEPT);

                }
                if(type == Constants.MEMBER_REJECT)
                {
                    AcceptRejectMember(position,Constants.MEMBER_REJECT);
                }
            }
        });


    }
}
