package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.UI.CreateEventActivity;
import com.aseanfan.worldcafe.UI.DirectMessageActivity;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.koushikdutta.async.Util;

public class MyPageDetailFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private TextView titleintroduce;
    private TextView titleinterested;
    private TextView titlerating;
    private TextView titlenumberthanks;
    private TextView titleschool;
    private TextView titlecompany;

    private Button directMessage;
    private Button createEvent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage_detail, container, false);

        initView(view);

        return view;
    }

    void initView(View view)
    {
        titleintroduce =(TextView) view.findViewById(R.id.titleIntroduce);
        Drawable iconProfile = getResources().getDrawable( R.drawable.icon_profile );
        iconProfile.setBounds(0,0, Utils.convertDpToPixel(20,getContext()),Utils.convertDpToPixel(20,getContext()));
        titleintroduce.setCompoundDrawables(iconProfile, null, null, null);

        titleinterested =(TextView) view.findViewById(R.id.titleInterested);
        Drawable iconInterested = getResources().getDrawable( R.drawable.icon_profile_intered );
        iconInterested.setBounds(0,0, Utils.convertDpToPixel(20,getContext()),Utils.convertDpToPixel(20,getContext()));
        titleinterested.setCompoundDrawables(iconInterested, null, null, null);

        titlerating =(TextView) view.findViewById(R.id.titleRating);
        Drawable iconRating = getResources().getDrawable( R.drawable.icon_profile_rating );
        iconRating.setBounds(0,0, Utils.convertDpToPixel(20,getContext()),Utils.convertDpToPixel(20,getContext()));
        titlerating.setCompoundDrawables(iconRating, null, null, null);

        titlenumberthanks =(TextView) view.findViewById(R.id.titlenumberthanks);
        Drawable iconNumber = getResources().getDrawable( R.drawable.icon_profile_like );
        iconNumber.setBounds(0,0, Utils.convertDpToPixel(20,getContext()),Utils.convertDpToPixel(20,getContext()));
        titlenumberthanks.setCompoundDrawables(iconNumber, null, null, null);

        titleschool =(TextView) view.findViewById(R.id.titleschool);
        Drawable iconSchool = getResources().getDrawable( R.drawable.icon_profile_school );
        iconSchool.setBounds(0,0, Utils.convertDpToPixel(20,getContext()),Utils.convertDpToPixel(20,getContext()));
        titleschool.setCompoundDrawables(iconSchool, null, null, null);

        titlecompany =(TextView) view.findViewById(R.id.titlecompany);
        Drawable iconCompany = getResources().getDrawable( R.drawable.icon_profile_company );
        iconCompany.setBounds(0,0, Utils.convertDpToPixel(20,getContext()),Utils.convertDpToPixel(20,getContext()));
        titlecompany.setCompoundDrawables(iconCompany, null, null, null);

        directMessage =(Button) view.findViewById(R.id.btn_directmessage);
        createEvent =(Button) view.findViewById(R.id.btn_createevent);

        directMessage.setOnClickListener(this);
        createEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_directmessage: {
                Intent intent = new Intent(getContext(), CreateEventActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_createevent: {
                Intent intent = new Intent(getContext(), DirectMessageActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
