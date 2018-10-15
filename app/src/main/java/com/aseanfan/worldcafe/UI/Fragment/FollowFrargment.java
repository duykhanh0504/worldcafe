package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.UI.Adapter.FragmentEventPageAdapter;
import com.aseanfan.worldcafe.UI.Adapter.FragmentFollowAdapter;
import com.aseanfan.worldcafe.worldcafe.R;

public class FollowFrargment  extends Fragment {

    private TabLayout tabfollow;
    private ViewPager viewPager;
    private FragmentFollowAdapter adapter;


    public static FollowFrargment newInstance() {
        FollowFrargment firstFrag = new FollowFrargment();
        return firstFrag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setTitle(null);

        viewPager = (ViewPager)view.findViewById(R.id.view_follow);
        adapter = new FragmentFollowAdapter(getActivity(),getChildFragmentManager());

        viewPager.setAdapter(adapter);



        tabfollow = (TabLayout)view.findViewById(R.id.tab_follow);

        tabfollow.setupWithViewPager(viewPager);


        return view;

    }
}
