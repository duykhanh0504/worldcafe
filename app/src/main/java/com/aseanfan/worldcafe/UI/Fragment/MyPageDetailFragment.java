package com.aseanfan.worldcafe.UI.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.UI.Adapter.ConctactChatAdapter;
import com.aseanfan.worldcafe.UI.ChatActivity;
import com.aseanfan.worldcafe.UI.ContactChatActivity;
import com.aseanfan.worldcafe.UI.CreateEventActivity;
import com.aseanfan.worldcafe.UI.DirectMessageActivity;
import com.aseanfan.worldcafe.UI.ListFollowActivity;
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

    private TextView txtintroduction;
    private TextView txtgive;
    private TextView txtschool;
    private TextView txtcompany;

    private Button directMessage;
    private Button createEvent;

    private Long friendid;
    private String friendavatar;
    private String introduce;
    private String interested;
    private int givethanks =0;
    private String school;
    private String company;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage_detail, container, false);
        Bundle bundle = this.getArguments();

        if(bundle != null){
            friendid = bundle.getLong("chat_id");
            friendavatar = bundle.getString("chat_avarta");
            introduce = bundle.getString("introduce");
            school = bundle.getString("school");
            company = bundle.getString("company");
            givethanks = bundle.getInt("numberthanks");

        }
        else {
            friendid = Long.valueOf(-1);
           /* introduce = AccountController.getInstance().getAccount().getIntroduction();
            interested = AccountController.getInstance().getAccount().getIntroduction();
            givethanks = AccountController.getInstance().getAccount().getIntroduction();
            school = AccountController.getInstance().getAccount().getIntroduction();
            company = AccountController.getInstance().getAccount().getIntroduction();*/
        }

        initView(view);
        setdata();
        return view;
    }

    void  setdata()
    {

        if(company!=null && !company.isEmpty()) {
            txtcompany.setText(company);
        }
       // txtgive.setText(company);
        if(company!=null && !company.isEmpty()) {
            txtschool.setText(school);
        }
        if(company!=null && !company.isEmpty()) {
            txtintroduction.setText(introduce);
        }

         txtgive.setText("give: " + givethanks);

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

        txtcompany =(TextView) view.findViewById(R.id.txtcompany);
        txtgive =(TextView) view.findViewById(R.id.txtgive);
        txtschool =(TextView) view.findViewById(R.id.txtschool);
        txtintroduction =(TextView) view.findViewById(R.id.txtintroduce);


        if(!friendid.equals(AccountController.getInstance().getAccount().getId()))
        {
            createEvent.setVisibility(View.GONE);

        }
        else
        {
            createEvent.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_directmessage: {
              //  Intent intent = new Intent(getContext(), DirectMessageActivity.class);
               // startActivity(intent);
                if(!friendid.equals(AccountController.getInstance().getAccount().getId()))
                {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("chat_id",friendid);
                    intent.putExtra("avatarurl",friendavatar);
                    startActivity(intent);

                }
                else
                {
                    Intent intent = new Intent(getContext(), ContactChatActivity.class);
                    startActivity(intent);

                }

                break;
            }
            case R.id.btn_createevent: {
                Intent intent = new Intent(getContext(), CreateEventActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
