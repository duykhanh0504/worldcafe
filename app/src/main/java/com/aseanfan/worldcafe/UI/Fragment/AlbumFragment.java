package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aseanfan.worldcafe.UI.Adapter.AlbumAdapter;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.Adapter.FragmentMyPagerAdapter;
import com.aseanfan.worldcafe.worldcafe.R;

public class AlbumFragment  extends android.support.v4.app.Fragment {

    private ViewPager viewPager;
    private RecyclerView listalbum;
    private AlbumAdapter mAdapter ;

    public static AlbumFragment newInstance() {
        AlbumFragment albumFrag = new AlbumFragment();
        return albumFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        listalbum = (RecyclerView)view.findViewById(R.id.list_album);

        mAdapter = new AlbumAdapter(null);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(container.getContext(),3);
        listalbum.setLayoutManager(mLayoutManager);
        listalbum.setItemAnimator(new DefaultItemAnimator());
        listalbum.setAdapter(mAdapter);

        return view;
    }
}