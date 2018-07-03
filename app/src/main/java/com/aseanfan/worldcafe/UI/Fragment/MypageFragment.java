package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Adapter.FragmentMyPagerAdapter;
import com.aseanfan.worldcafe.worldcafe.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class MypageFragment extends android.support.v4.app.Fragment {

    ViewPager viewPager;
    ImageView avatar;

    List<PostTimelineModel> posttimeline;


    public void LoadListMyPost()
    {
        RestAPI.GetDataMaster(getActivity().getApplicationContext(),RestAPI.GET_LISTPOSTMYPAGE, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<PostTimelineModel>>(){}.getType();
                    posttimeline = gson.fromJson(jsonArray, type);
                    //     mAdapter.setPostList(posttimeline);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public static MypageFragment newInstance() {
        MypageFragment firstFrag = new MypageFragment();
        return firstFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        LoadListMyPost();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(null);

        avatar = view.findViewById(R.id.avatar);
        Glide.with(getContext()).load( AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform()).into( avatar);
        AppBarLayout appBarLayout = view.findViewById(R.id.appBar);
        final ImageView avatar = view.findViewById(R.id.avatar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //visible image view
                   // avatar.setVisibility(View.INVISIBLE);
                    isShow = true;
                } else if (isShow) {
                    //invisible image view
                   // avatar.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

      //  ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      /*  CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        if(AccountController.getInstance().getAccount().getUsername()!=null) {
            collapsingToolbar.setTitle(AccountController.getInstance().getAccount().getUsername());
        }
        else
        {
            collapsingToolbar.setTitle(AccountController.getInstance().getAccount().getEmail());
        }*/

        viewPager = (ViewPager)view.findViewById(R.id.view_mypage);

        final FragmentMyPagerAdapter adapter = new FragmentMyPagerAdapter(getActivity(),getChildFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_mypage);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position ==2)
                {
                    adapter.updateFragmentAlbum(posttimeline);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }
}
