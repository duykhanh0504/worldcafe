package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class MypageFragment extends android.support.v4.app.Fragment {

    private ViewPager viewPager;
    private FrameLayout avatar;
    private CardView background;
    private TextView name;
    private ProgressBar loading;
    private List<PostTimelineModel> posttimeline;
    private ImageView rankImage;
    private Long accountid;
    private FragmentMyPagerAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        LoadListMyPost(accountid);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void LoadListMyPost(Long account)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", account);
        dataJson.addProperty("index",0);
        loading.setVisibility(View.VISIBLE);

        RestAPI.PostDataMaster(getActivity().getApplicationContext(),dataJson,RestAPI.GET_LISTPOSTMYPAGE, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result1");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<PostTimelineModel>>(){}.getType();
                    posttimeline = gson.fromJson(jsonArray, type);
                   // mAdapter.setPostList(posttimeline);
                    if(  viewPager.getCurrentItem() == FragmentMyPagerAdapter.MYPPOST_PAGE)
                    {
                        adapter.updateFragmentPost(posttimeline);
                    }

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
                finally {
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        if(getArguments()!=null) {
            accountid = getArguments().getLong("account_id");
        }
        else
        {
            accountid = AccountController.getInstance().getAccount().getId();
        }


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(null);

        name = view.findViewById(R.id.Name);
        name.setText(AccountController.getInstance().getAccount().getUsername());

        rankImage= (ImageView) view.findViewById(R.id.image_rank);

        avatar = view.findViewById(R.id.avatar);
        background = view.findViewById(R.id.background);
        loading = view.findViewById(R.id.loading_spinner);
        Glide.with(getContext()).load( "https://png.pngtree.com/thumb_back/fh260/back_pic/00/15/30/4656e81f6dc57c5.jpg").into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                background.setBackgroundDrawable(resource);
            }
        });
        Glide.with(getContext()).load( AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
               avatar.setBackgroundDrawable(resource);
            }
        });
        AppBarLayout appBarLayout = view.findViewById(R.id.appBar);

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

        rankImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        adapter = new FragmentMyPagerAdapter(accountid, getActivity(),getChildFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_mypage);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position ==FragmentMyPagerAdapter.ALBUM_PAGE)
                {
                    adapter.updateFragmentAlbum(posttimeline);
                }
                if(position ==FragmentMyPagerAdapter.MYPPOST_PAGE)
                {
                    adapter.updateFragmentPost(posttimeline);
                }
            }

            @Override
            public void onPageSelected(int position) {
                int i=0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int i=0;
            }
        });

        return view;
    }
}
