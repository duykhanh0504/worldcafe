package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.UI.Adapter.PostTimelineAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SettingAdapter;
import com.aseanfan.worldcafe.UI.ChangePassActivity;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.UI.ContactUsActivity;
import com.aseanfan.worldcafe.UI.EditProfileActivity;
import com.aseanfan.worldcafe.UI.IntroActivity;
import com.aseanfan.worldcafe.UI.LoginActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;

public class SettingFragment extends android.support.v4.app.Fragment implements SettingAdapter.ClickListener {


    private LocalBroadcastManager mLocalBroadcastManager;

    private int applicationrow =0;
    private int contactusrow = 1;
    private int changepasswordrow =2;
    private int contract =3;
    private int version =4;
    private int empty =5;
    private int logout =6;
    private int count =  logout;

    private ImageView imageAvatar;
    private TextView txtusername;
    private LinearLayout contentheader;

    private RecyclerView recyclerView;

    private SettingAdapter mAdapter;
    App app;

    public static SettingFragment newInstance() {
        SettingFragment firstFrag = new SettingFragment();
        return firstFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

         app = (App)getActivity().getApplication();

        recyclerView = (RecyclerView) view.findViewById(R.id.listsetting);
        imageAvatar = (ImageView) view.findViewById(R.id.imageAvatar);
        txtusername = (TextView) view.findViewById(R.id.txtusername);
        contentheader = (LinearLayout) view.findViewById(R.id.content_header);

        Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(getContext()).load(AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(imageAvatar);

        txtusername.setText(AccountController.getInstance().getAccount().getUsername());

        mAdapter = new SettingAdapter(count);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getContext());

        contentheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent   = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position, View v) {
        if(position == Constants.SETTING_LOGOUT_ROW)
        {
            ViewDialog dialog = new ViewDialog();
            dialog.showDialog(getActivity(), "Are you sure?", new ViewDialog.DialogListenner() {
                @Override
                public void OnClickConfirm() {
                    app.Logout();
                }
            });

        }
        else if(position == Constants.SETTING_CHANGEPASS_ROW)
        {
           Intent intent = new Intent(getActivity() , ChangePassActivity.class);
           startActivity(intent);
        }
        else if (position == Constants.SETTING_CONTACT_ROW)
        {

            Intent intent = new Intent(getActivity() , ContactUsActivity.class);
            startActivity(intent);

        }
    }
}
