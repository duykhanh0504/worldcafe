package com.aseanfan.worldcafe.UI.Fragment;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.koushikdutta.async.Util;

public class MyPageDetailFragment extends android.support.v4.app.Fragment {

    private TextView titleintroduce;
    private TextView titleinterested;
    private TextView titlerating;
    private TextView titlenumberthanks;
    private TextView titleschool;
    private TextView titlecompany;

    public static MyPageDetailFragment newInstance() {
        MyPageDetailFragment firstFrag = new MyPageDetailFragment();
        return firstFrag;
    }

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

    }
}
