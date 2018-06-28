package com.aseanfan.worldcafe.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.worldcafe.R;

public class MyPostFragment  extends android.support.v4.app.Fragment {

    public static MyPostFragment newInstance() {
        MyPostFragment firstFrag = new MyPostFragment();
        return firstFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_community, container, false);

        return view;
    }
}