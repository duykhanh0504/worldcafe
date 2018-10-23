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
import android.widget.ImageView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.UI.Adapter.FragmentEventPageAdapter;
import com.aseanfan.worldcafe.UI.Adapter.FragmentFollowAdapter;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.worldcafe.R;

public class FollowFrargment  extends Fragment {

    private TabLayout tabfollow;
    private ViewPager viewPager;
    private FragmentFollowAdapter adapter;
    private ImageView backbutton;


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
        Long  accountid = getArguments().getLong("accountid", AccountController.getInstance().getAccount().getId());
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setTitle(null);

        backbutton = (ImageView) view.findViewById(R.id.btncancel);

        viewPager = (ViewPager)view.findViewById(R.id.view_follow);
        adapter = new FragmentFollowAdapter(getActivity(),getChildFragmentManager(),accountid);

        viewPager.setAdapter(adapter);



        tabfollow = (TabLayout)view.findViewById(R.id.tab_follow);

        tabfollow.setupWithViewPager(viewPager);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).GoToback();
            }
        });


        return view;

    }
}
