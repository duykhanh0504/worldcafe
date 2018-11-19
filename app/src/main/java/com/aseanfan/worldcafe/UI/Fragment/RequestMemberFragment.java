package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.RequestUserModel;
import com.aseanfan.worldcafe.UI.Adapter.RequestMemberAdapter;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class RequestMemberFragment extends android.support.v4.app.Fragment  {

    private List<RequestUserModel> listuser;

    private RecyclerView rcyview;

    private RequestMemberAdapter mAdapter;

    private ImageView btncancel;

    public void Listuser(Long accountid , Long eventid)
    {
        String url =  String.format(RestAPI.GET_LISTREQUESTMEMBER, accountid,eventid);


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

        RestAPI.PostDataMasterWithToken(getActivity(),dataJson,RestAPI.POST_ACCEPTREJECTMEMBER, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    //listuser.get(pos).setStatus(status);
                    listuser.remove(listuser.get(pos));
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

    void init(View view)
    {
        listuser= new ArrayList<>();
        rcyview = (RecyclerView)view.findViewById(R.id.listRequsetmember);

        btncancel = (ImageView)view.findViewById(R.id.btncancel);

        mAdapter = new RequestMemberAdapter(null);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false);

        //  mLayoutManager.setStackFromEnd(true);
        rcyview.setLayoutManager(mLayoutManager);
        rcyview.setItemAnimator(new DefaultItemAnimator());
        rcyview.setAdapter(mAdapter);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_member_request, container, false);


            init(view);

            if( getArguments() != null)
            {

                Listuser(AccountController.getInstance().getAccount().getId(),getArguments().getLong("eventid"));

            }

            mAdapter.setOnItemClickListener(new RequestMemberAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v, int type) {
                    if(type == Constants.MEMBER_ACCEPT)
                    {
                        AcceptRejectMember(position,Constants.MEMBER_ACCEPT);

                    }
                    else if(type == Constants.MEMBER_REJECT)
                    {
                        AcceptRejectMember(position,Constants.MEMBER_REJECT);
                    }
                    else if(type == -1)
                    {
                        ((MainActivity) getActivity()).callFriendPage(listuser.get(position).getId());
                    }
                }
            });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).GoToback();
            }
        });


        return view;
    }


}
