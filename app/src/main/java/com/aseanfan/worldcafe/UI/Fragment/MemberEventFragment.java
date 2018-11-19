package com.aseanfan.worldcafe.UI.Fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.UI.Adapter.MemberEventAdapter;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MemberEventFragment extends android.support.v4.app.Fragment  {

    private List<UserModel> listuser;

    private RecyclerView rcyview;

    private MemberEventAdapter mAdapter;

    private ImageView btncancel;

    public void Listuser(Long accountid , Long eventid)
    {
        String url =  String.format(RestAPI.GET_LISTEVENTACCOUNT, accountid,eventid);


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
                    java.lang.reflect.Type type = new TypeToken<List<UserModel>>(){}.getType();
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



    void init(View view)
    {
        listuser= new ArrayList<>();
        rcyview = (RecyclerView)view.findViewById(R.id.listRequsetmember);
        btncancel = (ImageView)view.findViewById(R.id.btncancel);

        mAdapter = new MemberEventAdapter(null);

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

        mAdapter.setOnItemClickListener(new MemberEventAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ((MainActivity) getActivity()).callFriendPage(listuser.get(position).getId());
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
