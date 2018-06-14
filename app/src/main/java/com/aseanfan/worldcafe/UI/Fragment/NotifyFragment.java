package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.worldcafe.R;

public class NotifyFragment extends android.support.v4.app.Fragment {

    public static NotifyFragment newInstance() {
        NotifyFragment firstFrag = new NotifyFragment();
        return firstFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);

        return view;
    }
}
