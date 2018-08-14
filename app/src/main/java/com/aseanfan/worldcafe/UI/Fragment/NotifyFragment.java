package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.NotificationModel;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.UI.Adapter.NotifyAdapter;
import com.aseanfan.worldcafe.worldcafe.R;

import java.util.List;

public class NotifyFragment extends android.support.v4.app.Fragment {

    private RecyclerView rcynotify;

    private NotifyAdapter mAdapter;

    private List<NotificationModel> listnotify;

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
