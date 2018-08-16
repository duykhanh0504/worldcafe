package com.aseanfan.worldcafe.UI.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


public class DetailCommunityFragment extends android.support.v4.app.Fragment {


    private FrameLayout imageEvent;
    private EventModel event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_community, container, false);
        imageEvent = (FrameLayout)view.findViewById(R.id.imageEvent);
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

        return view;
    }
}
