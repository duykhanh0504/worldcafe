package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.worldcafe.R;


public class DetailCommunityFragment extends android.support.v4.app.Fragment {

    public static DetailCommunityFragment newInstance() {
        DetailCommunityFragment detail = new DetailCommunityFragment();
        return detail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        return view;
    }
}
