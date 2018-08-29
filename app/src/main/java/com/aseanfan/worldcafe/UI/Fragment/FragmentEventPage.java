package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.CommentEventActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.JsonObject;

import java.util.List;

public class FragmentEventPage extends android.support.v4.app.Fragment {

    RecyclerView list_community;

    private CommunityAdapter mAdapter;
    List<EventModel> listevent;

    public void setData(List<EventModel> data)
    {
        listevent =data;
        mAdapter.setData(listevent);
    }


    public void LikeEvent(Long Eventid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("event_id",Eventid);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_LIKEEVENT, new RestAPI.RestAPIListenner() {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_view_layout, container, false);
        Bundle bundle = this.getArguments();
        mAdapter = new CommunityAdapter(null);
        list_community = (RecyclerView) view.findViewById(R.id.list_community);;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list_community.setLayoutManager(mLayoutManager);
        list_community.setItemAnimator(new DefaultItemAnimator());
        list_community.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CommunityAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v,int Type,EventModel event) {
                if(Type == Constants.CLICK_EVENT) {
                    ((MainActivity)getActivity()).callDetailEvent(event);
                }
                if(Type == Constants.CLICK_IMAGE_LIKE) {
                   // ((MainActivity)getActivity()).callDetailEvent(event);
                    LikeEvent(event.getEventid());
                }
                if(Type == Constants.CLICK_IMAGE_COMMENT) {
                   // ((MainActivity)getActivity()).callDetailEvent(event);
                    Intent intent = new Intent(getActivity(), CommentEventActivity.class);
                    intent.putExtra("Event_id",event.getEventid());
                    startActivity(intent);
                  //  ((MainActivity)getActivity()).callDetailEvent(event);
                }
            }
        });

        return view;
    }


}
