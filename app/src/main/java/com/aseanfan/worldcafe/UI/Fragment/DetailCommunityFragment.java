package com.aseanfan.worldcafe.UI.Fragment;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.AccountModel;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class DetailCommunityFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    private FrameLayout imageEvent;
    private EventModel event;
    private Button btnJoin;
    private FrameLayout containList;
    private List<AccountModel> listaccount = new ArrayList<>();
    private TextView title;
    private TextView content;
    private TextView type;
    private TextView starttime;
    private TextView place;
    private TextView numberlike;
    private TextView numbercomment;
    private ImageView avatar;
    private TextView username;

    public void JoinEvent(Long eventid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("event_id",eventid);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_JOINEVENT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if (statuscode == RestAPI.STATUS_SUCCESS) {
                        btnJoin.setText("Joined");
                        btnJoin.setEnabled(false);
                    }

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void getListAccount(Long eventid)
    {
        String url =  String.format(RestAPI.GET_LISTEVENTACCOUNT,AccountController.getInstance().getAccount().getId(),eventid);

        RestAPI.GetDataMasterWithToken(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    listaccount.clear();
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<AccountModel>>(){}.getType();
                    listaccount = gson.fromJson(jsonArray, type);
                    if(listaccount!=null && listaccount.size()>0)
                    {

                        for( int i = 0 ; i < 7 ; i++) {
                            ImageView avatarImage = new ImageView(getContext());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.convertDpToPixel(40,getContext()),Utils.convertDpToPixel(40,getContext()));
                            layoutParams.height = Utils.convertDpToPixel(40,getContext());
                            layoutParams.width = Utils.convertDpToPixel(40,getContext());
                            layoutParams.setMargins( i * Utils.convertDpToPixel(30 , getContext()) , 0, 0, 0);
                            avatarImage.setLayoutParams(layoutParams);
                            avatarImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
                            Glide.with(getContext()).load(listaccount.get(i).getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(avatarImage);

                            containList.addView(avatarImage);
                        }

                    }

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void initview(View view )
    {
        imageEvent = (FrameLayout)view.findViewById(R.id.imageEvent);
        btnJoin = (Button) view.findViewById(R.id.btnJoin);
        containList = (FrameLayout) view.findViewById(R.id.list_account);
        title = (TextView) view.findViewById(R.id.txttitle);
        content = (TextView) view.findViewById(R.id.txtcontent);
        type = (TextView) view.findViewById(R.id.txttype);
        starttime = (TextView) view.findViewById(R.id.txtdate);
        place = (TextView) view.findViewById(R.id.txtplace);
        numberlike = (TextView) view.findViewById(R.id.txtlike);
        numbercomment = (TextView) view.findViewById(R.id.txtcomment);
        avatar = (ImageView)view.findViewById(R.id.imageAvatar);
        username = (TextView)view.findViewById(R.id.username);

        btnJoin.setOnClickListener(this);
    }

    public void initdata()
    {
        if (getArguments() != null) {
            event.setEventid(getArguments().getLong("eventid"));
            event.setIsJoin(getArguments().getInt("isJoin"));
            event.setTitle(getArguments().getString("title"));
            event.setContent(getArguments().getString("content"));
            event.setType(getArguments().getInt("type"));
            event.setStarttime(getArguments().getString("startime"));
            event.setCityname(getArguments().getString("place"));
            event.setNumberComment(getArguments().getInt("numbercomment"));
            event.setNumberLike(getArguments().getInt("numberlike"));
            event.setUrlAvatar(getArguments().getString("avatar"));
            event.setUsername(getArguments().getString("username"));

            if(event.getIsjoin() == 1)
            {
                btnJoin.setText("Joined");
                btnJoin.setEnabled(false);
            }
            else
            {
                btnJoin.setText("Join");
                btnJoin.setEnabled(true);
            }
            Glide.with(getContext()).load( getArguments().getString("image")).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageEvent.setBackgroundDrawable(resource);
                }
            });

            title.setText(event.getTitle());
            content.setText(event.getContent());

            if(event.getType() == Constants.EVENT_FRIEND)
            {
                type.setText(getContext().getText(R.string.Friend));
            }
            else if (event.getType() == Constants.EVENT_BUSSINESS)
            {
                type.setText(getContext().getText(R.string.Business));
            }
            else if (event.getType() == Constants.EVENT_LOCAL)
            {
                type.setText(getContext().getText(R.string.Local));
            }
            else
            {
               type.setText(getContext().getText(R.string.Language));
            }

            starttime.setText(Utils.ConvertDate(event.getStarttime()));
            place.setText(event.getCityname());
            numberlike.setText(String.valueOf(event.getNumberLike()));
            numbercomment.setText(String.valueOf(event.getNumberComment()));
            Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
            Glide.with(getContext()).load(event.getUrlAvatar()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(avatar);
            username.setText(event.getUsername());

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_event, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_edit) {

        }
        else if(id == R.id.item_listmember)
        {

        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_community, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        initview(view);

        event = new EventModel();

        initdata();

        getListAccount(event.getEventid());

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnJoin:
                JoinEvent(event.getEventid());
                break;
        }
    }
}
