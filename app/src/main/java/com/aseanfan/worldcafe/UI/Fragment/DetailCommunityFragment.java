package com.aseanfan.worldcafe.UI.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
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
import com.aseanfan.worldcafe.UI.Adapter.RequestMemberAdapter;
import com.aseanfan.worldcafe.UI.CommentEventActivity;
import com.aseanfan.worldcafe.UI.CreateEventActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.UI.MemberRequestActivity;
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
    private TextView updatetime;
    private TextView title;
    private TextView content;
    private TextView type;
    private TextView starttime;
    private TextView place;
    private TextView numberlike;
    private TextView numbercomment;
    private ImageView avatar;
    private TextView username;
    private TextView scheduel;
    private TextView number_attendess;
    private TextView note;
    private ImageView imagemenu;
    private ImageView imagelike;
    private ImageView imagecomment;
    private LinearLayout content_info;


    public void LikeEvent(final Long  eventid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("event_id",eventid);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_LIKEEVENT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    if(event.getIslike() ==0) {
                        event.setNumberLike(event.getNumberLike()+1);
                        numberlike.setText(String.valueOf(event.getNumberLike()));
                        event.setIslike(1);
                        imagelike.setBackgroundResource(R.drawable.like);

                    }
                    else
                    {
                        event.setNumberLike(event.getNumberLike()-1);
                        numberlike.setText(String.valueOf(event.getNumberLike()));
                        event.setIslike(0);
                        imagelike.setBackgroundResource(R.drawable.unlike);
                    }

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

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
                        btnJoin.setText("Request");
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
                        int number = listaccount.size()>1?2:listaccount.size();
                        for( int i = 0 ; i < number ; i++) {
                            ImageView avatarImage = new ImageView(getContext());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.convertDpToPixel(40, getContext()), Utils.convertDpToPixel(40, getContext()));
                            layoutParams.height = Utils.convertDpToPixel(40, getContext());
                            layoutParams.width = Utils.convertDpToPixel(40, getContext());
                            layoutParams.setMargins(i * Utils.convertDpToPixel(30, getContext()), 0, 0, 0);
                            avatarImage.setLayoutParams(layoutParams);
                            avatarImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            if(i==1)
                            {
                                Bitmap image = Utils.createImage(Utils.convertDpToPixel(40, getContext()),
                                        Utils.convertDpToPixel(40, getContext()),getResources().getColor(R.color.colorPrimary),
                                        String.valueOf(listaccount.size()),getContext());
                                Glide.with(getContext()).load(image).apply(RequestOptions.circleCropTransform()).into(avatarImage);
                            }
                            else {
                                Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
                                Glide.with(getContext()).load(listaccount.get(i).getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(avatarImage);

                            }
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
        numberlike = (TextView) view.findViewById(R.id.textLike);
        numbercomment = (TextView) view.findViewById(R.id.textComment);
        avatar = (ImageView)view.findViewById(R.id.imageAvatar);
        username = (TextView)view.findViewById(R.id.username);
        updatetime = (TextView) view.findViewById(R.id.txtcreatetime);
        scheduel = (TextView) view.findViewById(R.id.txtscheduel);
        number_attendess = (TextView) view.findViewById(R.id.txtquatity);
        note = (TextView) view.findViewById(R.id.Attention);
        imagemenu = (ImageView) view.findViewById(R.id.imagemenu);
        imagelike = (ImageView) view.findViewById(R.id.imageLike);
        imagecomment = (ImageView) view.findViewById(R.id.imageComment);
        content_info = (LinearLayout)  view.findViewById(R.id.content_info);

        btnJoin.setOnClickListener(this);
        containList.setOnClickListener(this);
        imagemenu.setOnClickListener(this);
        imagelike.setOnClickListener(this);
        imagecomment.setOnClickListener(this);
        content_info.setOnClickListener(this);

    }

    public void initdata()
    {
        if (getArguments() != null) {
            event.setIsJoin(getArguments().getInt("isJoin"));
            event.setEventid(getArguments().getLong("eventid"));
            event.setTitle(getArguments().getString("title"));
            event.setContent(getArguments().getString("content"));
            event.setType(getArguments().getInt("type"));
            event.setStarttime(getArguments().getString("startime"));
            event.setUpdatetime(getArguments().getString("updatetime"));
            event.setCityname(getArguments().getString("place"));
            event.setNumberComment(getArguments().getInt("numbercomment"));
            event.setNumberLike(getArguments().getInt("numberlike"));
            event.setUrlAvatar(getArguments().getString("avatar"));
            event.setUsername(getArguments().getString("username"));
            event.setAccount_id(getArguments().getLong("accountid"));
            event.setNumber(getArguments().getInt("number"));
            event.setPertime(getArguments().getInt("pertime"));
            event.setLimit_personse(getArguments().getInt("limitperson"));
            event.setNote(getArguments().getString("note"));
            event.setIslike(getArguments().getInt("islike"));

            if(event.getIslike()==0)
            {
                imagelike.setBackgroundResource(R.drawable.unlike);
            }
            else
            {
                imagelike.setBackgroundResource(R.drawable.like);
            }



            if(event.getAccountid().equals(AccountController.getInstance().getAccount().getId())) {
                setHasOptionsMenu(true);
                imagemenu.setVisibility(View.VISIBLE);
            }
            else
            {
                setHasOptionsMenu(false);
                imagemenu.setVisibility(View.GONE);
            }
            if(!event.getAccountid().equals(AccountController.getInstance().getAccount().getId())) {
                if (event.getIsjoin() == 1) {
                    btnJoin.setText("Request");
                    btnJoin.setEnabled(false);
                } else if (event.getIsjoin() == 2) {
                    btnJoin.setText("Joined");
                    btnJoin.setEnabled(false);
                } else {
                    btnJoin.setText("Join");
                    btnJoin.setEnabled(true);
                }
            }
            else
            {
                btnJoin.setText("Owner");
                btnJoin.setEnabled(false);
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
            updatetime.setText(event.getUpdatetime());
            place.setText(event.getCityname());
            String timetype= "";
            if(event.getPertime() ==0)
            {
                timetype = "Week";
            }
            else if(event.getPertime() ==1)
            {
                timetype = "Month";
            }
            else if(event.getPertime() ==2)
            {
                timetype = "Year";
            }
            scheduel.setText(event.getNumber() + " / " + timetype);
            number_attendess.setText(event.getLimitpersons()+"");
            if(event.getNote() != null && !event.getNote().isEmpty())
            {
                note.setText(event.getNote());
            }
            else
            {
                note.setText("Nothing to show");
            }
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
        //    Intent intent = new Intent(getActivity() , MemberRequestActivity.class);
           // intent.putExtra("eventid",event.getEventid());
           // startActivity(intent);
            ((MainActivity)getActivity()).callMemberRequest(event);
        }
        return super.onOptionsItemSelected(item);
    }

    public void openOptionMenu(View v){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.toolbar_event, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item_listmember:
                       // DeletePost(position);
                        ((MainActivity)getActivity()).callMemberRequest(event);
                        break;
                    case R.id.item_edit:
                        Intent intent = new Intent(getContext(), CreateEventActivity.class);

                        intent.putExtra("isedit",1);
                        intent.putExtra("type",event.getType());
                        intent.putExtra("place",event.getCityid());
                        intent.putExtra("price",event.getPrice());
                        intent.putExtra("limitperson",event.getLimitpersons());
                        intent.putExtra("title",event.getTitle());
                        intent.putExtra("detail",event.getContent());
                        intent.putExtra("note",event.getNote());


                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_community, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        initview(view);

        event = new EventModel();

        initdata();

        getListAccount(event.getEventid());

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagemenu:
                openOptionMenu(view);
                break;
            case R.id.btnJoin:
                JoinEvent(event.getEventid());
                break;
            case  R.id.list_account:
            {
                ((MainActivity)getActivity()).callMemberEvent(event);
                break;
            }
            case R.id.imageComment:
            {
                Intent intent = new Intent(getActivity(), CommentEventActivity.class);
                intent.putExtra("Event_id",event.getEventid());
                startActivity(intent);
                break;
            }
            case R.id.imageLike:
            {
                LikeEvent(event.getEventid());
                break;
            }
            case R.id.content_info:
            {
                ((MainActivity)getActivity()).callFriendPage(event.getAccountid());
                break;
            }
        }
    }
}
