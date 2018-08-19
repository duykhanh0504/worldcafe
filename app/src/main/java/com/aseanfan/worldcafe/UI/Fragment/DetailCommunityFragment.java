package com.aseanfan.worldcafe.UI.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonObject;


public class DetailCommunityFragment extends android.support.v4.app.Fragment {


    private FrameLayout imageEvent;
    private EventModel event;
    private Button btnJoin;

    public void JoinEvent(Long eventid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("event_id",eventid);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_JOINEVENT, new RestAPI.RestAPIListenner() {
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
        View view = inflater.inflate(R.layout.fragment_detail_community, container, false);
        imageEvent = (FrameLayout)view.findViewById(R.id.imageEvent);
        btnJoin = (Button) view.findViewById(R.id.btnJoin);

        event = new EventModel();

        if (getArguments() != null) {
            event.setEventid(getArguments().getLong("eventid"));
            Glide.with(getContext()).load( getArguments().getString("image")).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageEvent.setBackgroundDrawable(resource);
                }
            });

        }

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinEvent(event.getEventid());
            }
        });

        return view;
    }
}
