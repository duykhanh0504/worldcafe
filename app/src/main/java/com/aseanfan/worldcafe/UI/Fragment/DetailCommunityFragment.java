package com.aseanfan.worldcafe.UI.Fragment;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.AccountModel;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.UI.Adapter.RequestMemberAdapter;
import com.aseanfan.worldcafe.UI.CommentEventActivity;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.UI.CreateEventActivity;
import com.aseanfan.worldcafe.UI.EditEventActivity;
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
    private LinearLayout containList;
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
    private TextView txtprivate;
    private TextView txtprice;

    String[] listreport ;


    public void LoadReport()
    {
        String url;
        url = String.format(RestAPI.GET_LIST_REPORT, 0);

        RestAPI.GetDataMaster(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<String>>(){}.getType();
                    List<String> listEvent = gson.fromJson(jsonArray, type);
                    dialogReport(listEvent);


                }
                catch (Exception ex) {
                }
            }
        });
    }
    public void dialogReport(final List<String> list)
    {
        listreport = new String[list.size()];
        listreport = list.toArray(listreport);
        final int[] mpos = {0};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.choice_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getActivity(), R.layout.choice_item, listreport);

        builder.setCancelable(true);
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mpos[0] = i;
            }
        });

        /*builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // LoadListEvent(typegenre);
                dialog.dismiss();
                if(mpos[0]!=-1) {
                    Report(listreport[mpos[0]]);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);*/

        Button cancel = dialogView.findViewById(R.id.btn_cancel);
        Button report = dialogView.findViewById(R.id.btn_report);

        final AlertDialog dialog = builder.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(mpos[0]!=-1) {
                    Report(listreport[mpos[0]]);
                }
            }
        });
        dialog.show();
    }

  /*  public void dialogReport(final List<String> list)
    {
        final String[] spinner_item = new String[1];

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report);
        dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
        final EditText edittext = (EditText) dialog.findViewById(R.id.editText1);
        Button button = (Button) dialog.findViewById(R.id.report);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                spinner_item[0] = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                Report(spinner_item[0]);

                //   Toast.makeText(getApplicationContext(), spinner_item + " - " + edittext.getText().toString().trim(), Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }*/


    public void Report(String reporttext)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("type", 1);
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("object_id", event.getEventid());
        dataJson.addProperty("content", reporttext);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_REPORT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    ViewDialog dialog = new ViewDialog();
                    dialog.showDialogCancel(getActivity(),"Report Successful");
                    //   posttimeline.remove(pos);
                    //  Adapter.setPostList(posttimeline);

                }
                catch (Exception ex) {
                }
            }
        });
    }

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

    public void JoinEvent(Long eventid,String comment)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("event_id",eventid);
        dataJson.addProperty("comment", comment);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_JOINEVENT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);
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

    @Override
    public void onResume() {
        super.onResume();

    }

    public void getDetailEvent(Long eventid)
    {
        String url =  String.format(RestAPI.GET_EVENTDETAIL,AccountController.getInstance().getAccount().getId(),eventid);

        RestAPI.GetDataMasterWithToken(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonObject json = (new JsonParser()).parse(s).getAsJsonObject();
                    Gson gson = new Gson();
                    event = gson.fromJson(json.getAsJsonObject("result"), EventModel.class);
                    loaddata();
                    getListAccount(event.getEventid());
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
                        containList.setOrientation(LinearLayout.HORIZONTAL);
                        int number = listaccount.size()>5?5:listaccount.size();
                        for( int i = 0 ; i < number ; i++) {
                            ImageView avatarImage = new ImageView(getContext());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.convertDpToPixel(40, getContext()), Utils.convertDpToPixel(40, getContext()));
                            layoutParams.height = Utils.convertDpToPixel(40, getContext());
                            layoutParams.width = Utils.convertDpToPixel(40, getContext());
                            layoutParams.setMargins(i * Utils.convertDpToPixel(2, getContext()), 0, 0, 0);
                            avatarImage.setLayoutParams(layoutParams);
                            avatarImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            if(i==4)
                            {
                                Bitmap image = Utils.createImage(Utils.convertDpToPixel(40, getContext()),
                                        Utils.convertDpToPixel(40, getContext()),getResources().getColor(R.color.colorPrimary),
                                        String.valueOf(listaccount.size() - 4),getContext());
                                Glide.with(getContext()).load(image).apply(RequestOptions.circleCropTransform()).into(avatarImage);
                            }
                            else {
                                Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
                                Glide.with(getContext()).load(listaccount.get(i).getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(avatarImage);

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
        containList = (LinearLayout) view.findViewById(R.id.list_account);
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
        txtprivate = (TextView)  view.findViewById(R.id.txtprivate);
        txtprice = (TextView)  view.findViewById(R.id.txtprice);


        btnJoin.setOnClickListener(this);
        containList.setOnClickListener(this);
        imagemenu.setOnClickListener(this);
        imagelike.setOnClickListener(this);
        imagecomment.setOnClickListener(this);
        content_info.setOnClickListener(this);

    }
    public void loaddata()
    {
        txtprice.setText(event.getPrice().toString() + " VND");

        if(event.getIslike()==0)
        {
            imagelike.setBackgroundResource(R.drawable.unlike);
        }
        else
        {
            imagelike.setBackgroundResource(R.drawable.like);
        }
        if(event.getPrivate() ==0)
        {
            txtprivate.setText("Public");
        }
        else
        {
            txtprivate.setText(getResources().getString(R.string.Private));
        }



        if(event.getAccountid().equals(AccountController.getInstance().getAccount().getId())) {
            setHasOptionsMenu(true);
            // imagemenu.setVisibility(View.VISIBLE);
            imagemenu.setImageResource(R.drawable.event_header_right);
        }
        else
        {
            setHasOptionsMenu(false);
            imagemenu.setImageResource(R.drawable.ic_report_header);
            // imagemenu.setVisibility(View.GONE);
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
            btnJoin.setBackgroundResource(R.drawable.button_gray);
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

        starttime.setText(Utils.ConvertDateEvent(event.getStarttime()));
        updatetime.setText(Utils.ConvertDateEventNonDetail(event.getUpdatetime()));
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
        if( event.getNumber()==0 ) {
            //scheduel.setText(event.getNumber() + " / " + timetype);
            scheduel.setText("No repeat");
        }
        else
        {
            scheduel.setText(event.getNumber() + " / " + timetype);
        }
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
        Glide.with(getContext()).load(event.getUrlAvatar()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(avatar);
        username.setText(event.getUsername());
    }

    public void initdata()
    {
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
            event.setSchedule_type(getArguments().getInt("schedule_type"));
            event.setPrivate(getArguments().getInt("is_private"));
            event.setPrice(getArguments().getLong("price"));

            loaddata();
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
                        Intent intent = new Intent(getContext(), EditEventActivity.class);

                        Bundle bundle = new Bundle();
                        intent.putExtra("eventid",event.getEventid());
                        intent.putExtra("title",event.getTitle());
                        intent.putExtra("content",event.getContent());
                        intent.putExtra("isJoin",event.getIsjoin());
                        intent.putExtra("type",event.getType());
                        intent.putExtra("startime",event.getStarttime());
                        intent.putExtra("updatetime", event.getUpdatetime());
                        intent.putExtra("place",event.getCityname());
                        intent.putExtra("numberlike",event.getNumberLike());
                        intent.putExtra("numbercomment",event.getNumberComment());
                        intent.putExtra("avatar",event.getUrlAvatar());
                        intent.putExtra("username",event.getUsername());
                        intent.putExtra("accountid",event.getAccountid());
                        intent.putExtra("number",event.getNumber());
                        intent.putExtra("pertime",event.getPertime());
                        intent.putExtra("limitperson",event.getLimitpersons());
                        intent.putExtra("note",event.getNote());
                        intent.putExtra("islike",event.getIslike());
                        intent.putExtra("schedule_type",event.getSchedule_type());
                        intent.putExtra("is_private",event.getPrivate());
                        intent.putExtra("price",event.getPrice());
                        intent.putExtra("urlimage",getArguments().getString("image"));

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

        if (getArguments() != null) {
            if (getArguments().getBoolean("frompush", false) == false) {
                initdata();
            } else {
                getDetailEvent(getArguments().getLong("eventid"));
            }
        }


        getListAccount(event.getEventid());

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagemenu:
                if(event.getAccountid().equals(AccountController.getInstance().getAccount().getId())) {
                    openOptionMenu(view);
                }
                else
                {
                    LoadReport();
                }
                break;
            case R.id.btnJoin:
                ViewDialog dialog = new ViewDialog();
                if(event.getPrivate() ==0) {
                    dialog.showDialogJoinEvent(getActivity(), AccountController.getInstance().getAccount().getAvarta(), AccountController.getInstance().getAccount().getUsername()
                            , new ViewDialog.DialogListennerJoinEvent() {
                                @Override
                                public void OnClickConfirm(String text) {
                                    JoinEvent(event.getEventid(), text);
                                }
                            });
                }
                else
                {
                    dialog.showDialogCancel(getActivity(),"Event is private");
                }
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
