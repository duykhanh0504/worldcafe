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
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.UI.Adapter.ConctactChatAdapter;
import com.aseanfan.worldcafe.UI.ChatActivity;
import com.aseanfan.worldcafe.UI.ContactChatActivity;
import com.aseanfan.worldcafe.UI.CreateEventActivity;
import com.aseanfan.worldcafe.UI.DirectMessageActivity;
import com.aseanfan.worldcafe.UI.EditProfileActivity;
import com.aseanfan.worldcafe.UI.ListFollowActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.koushikdutta.async.Util;

public class MyPageDetailFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private TextView titleintroduce;
    private TextView titleinterested;
   // private TextView titlerating;
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
    private String name;
    private String interested;
    private int givethanks =0;
    private String school;
    private String company;

    private TextView radfriend;
    private TextView radlanguage;
    private TextView radlocal;
    private TextView radbusiness;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage_detail, container, false);
        Bundle bundle = this.getArguments();

        if(bundle != null){
            friendid = bundle.getLong("chat_id");
            friendavatar = bundle.getString("chat_avarta");
            introduce = bundle.getString("introduce");
            name = bundle.getString("name");
            school = bundle.getString("school");
            company = bundle.getString("company");
            givethanks = bundle.getInt("numberthanks");
            interested = bundle.getString("interested");

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

    public  void  update(UserModel userModel)
    {

            friendid = Long.valueOf(-1);
            introduce = userModel.getIntroduction();
            interested =userModel.getInterest();;
            givethanks = userModel.getTotalLike();
            school = userModel.getSchool();
            company = userModel.getCompany();
            setdata();

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
        radbusiness.setVisibility(View.GONE);
        radfriend.setVisibility(View.GONE);
        radlanguage.setVisibility(View.GONE);
        radlocal.setVisibility(View.GONE);

        if(interested!=null && !interested.isEmpty()) {
            String[] separated = interested.split(",");

            for (int i = 0; i < separated.length; i++) {
                if(Integer.valueOf(separated[i]) == Constants.EVENT_LOCAL +1)
                {
                    radlocal.setVisibility(View.VISIBLE);
                }
                else if(Integer.valueOf(separated[i]) == Constants.EVENT_LANGUAGE +1)
                {
                    radlanguage.setVisibility(View.VISIBLE);
                }
                else if(Integer.valueOf(separated[i]) == Constants.EVENT_BUSSINESS +1)
                {
                    radbusiness.setVisibility(View.VISIBLE);
                }
                else if(Integer.valueOf(separated[i]) == Constants.EVENT_FRIEND +1)
                {
                    radfriend.setVisibility(View.VISIBLE);
                }
            }
        }



    }

    void initView(View view)
    {
        titleintroduce =(TextView) view.findViewById(R.id.titleIntroduce);


        titleinterested =(TextView) view.findViewById(R.id.titleInterested);


        titlenumberthanks =(TextView) view.findViewById(R.id.titlenumberthanks);



        titleschool =(TextView) view.findViewById(R.id.titleschool);


        titlecompany =(TextView) view.findViewById(R.id.titlecompany);

        directMessage =(Button) view.findViewById(R.id.btn_directmessage);
        createEvent =(Button) view.findViewById(R.id.btn_createevent);

        directMessage.setOnClickListener(this);
        createEvent.setOnClickListener(this);

        txtcompany =(TextView) view.findViewById(R.id.txtcompany);
        txtgive =(TextView) view.findViewById(R.id.txtgive);
        txtschool =(TextView) view.findViewById(R.id.txtschool);
        txtintroduction =(TextView) view.findViewById(R.id.txtintroduce);

        radfriend =(TextView) view.findViewById(R.id.radfriend);
        radlanguage =(TextView) view.findViewById(R.id.radlanguage);
        radlocal =(TextView) view.findViewById(R.id.radlocal);
        radbusiness =(TextView) view.findViewById(R.id.radbusiness);

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
                if(friendid!=-1&&!friendid.equals(AccountController.getInstance().getAccount().getId()))
                {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("chat_id",friendid);
                    intent.putExtra("avatarurl",friendavatar);
                    intent.putExtra("name",name);
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
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
