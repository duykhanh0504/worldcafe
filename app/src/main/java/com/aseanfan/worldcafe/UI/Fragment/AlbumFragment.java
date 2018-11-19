package com.aseanfan.worldcafe.UI.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aseanfan.worldcafe.UI.Adapter.AlbumAdapter;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.Adapter.FragmentMyPagerAdapter;
import com.aseanfan.worldcafe.UI.Component.DIalogImagePreview;
import com.aseanfan.worldcafe.UI.Component.ItemDecorationAlbumColumns;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;

import java.util.List;

public class AlbumFragment  extends android.support.v4.app.Fragment {

    private ViewPager viewPager;
    private RecyclerView listalbum;
    private AlbumAdapter mAdapter ;
    private List<String> imagelist;

    public static AlbumFragment newInstance() {
        AlbumFragment albumFrag = new AlbumFragment();
        return albumFrag;
    }

    public void setAlbumData(List<String> data)
    {
        mAdapter.setData(data);
        imagelist = data;
    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        listalbum = (RecyclerView)view.findViewById(R.id.list_album);

        mAdapter = new AlbumAdapter(null);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(container.getContext(),2);
       // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
        listalbum.setLayoutManager(mLayoutManager);
        listalbum.setItemAnimator(new DefaultItemAnimator());
        listalbum.setAdapter(mAdapter);
        listalbum.addItemDecoration(new ItemDecorationAlbumColumns(
                Utils.convertDpToPixel(5,container.getContext()), 2));
        mAdapter.setOnItemClickListener(new AlbumAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                DialogFragment fragment =  DIalogImagePreview.newInstance(imagelist,position);
                fragment.show(getFragmentManager(), "image preview");
            }
        });


        return view;
    }
}