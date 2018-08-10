package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.worldcafe.R;

import java.util.List;

public class FragmentEventPage extends android.support.v4.app.Fragment {

    RecyclerView list_community;



    private CommunityAdapter mAdapter;

    public void setData(List<EventModel> data)
    {
        mAdapter.setData(data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_view_layout, container, false);
        Bundle bundle = this.getArguments();
        mAdapter = new CommunityAdapter(null);
        list_community = (RecyclerView) view.findViewById(R.id.list_community);;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list_community.setLayoutManager(mLayoutManager);
        list_community.setItemAnimator(new DefaultItemAnimator());
        list_community.setAdapter(mAdapter);

        return view;
    }


}
