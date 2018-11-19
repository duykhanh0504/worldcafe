package com.aseanfan.worldcafe.UI;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CommentEventActivity extends AppCompatActivity implements CommentAdapter.ClickListener {

    private ImageButton btnPost;
    private Long eventid;
    private Long AccountId;
    private EditText edtComent;

    private RecyclerView rcycoment;

    private CommentAdapter mAdapter;

    private List<CommentModel> listcomment;
    private ImageView avatar;

    private LinearLayout reply;
    private TextView txtreply;
    private int typecoment =0;
    private int subCommentid;
    private ImageView btncancel;


    public void PostEventSubComment(final int commentid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("comment_id",commentid);
        dataJson.addProperty("content",edtComent.getText().toString());

        RestAPI.PostDataMasterWithToken(this,dataJson,RestAPI.POST_EVENTSUBCOMMENT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    ListComment(eventid);
                    edtComent.setText("");
                    reply.setVisibility(View.GONE);
                    typecoment =0;

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void PostEventComment(final Long eventid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("event_id",eventid);
        dataJson.addProperty("content",edtComent.getText().toString());

        RestAPI.PostDataMasterWithToken(this,dataJson,RestAPI.POST_EVENTCOMMENT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    ListComment(eventid);
                    edtComent.setText("");


                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void ListComment(Long eventid)
    {
        String url;
        url = String.format(RestAPI.GET_EVENTCOMMENT, eventid,AccountController.getInstance().getAccount().getId());

        RestAPI.GetDataMasterWithToken(this,url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<CommentModel>>(){}.getType();
                    listcomment = gson.fromJson(jsonArray, type);
                    mAdapter.setCommentList(listcomment);
                    rcycoment.smoothScrollToPosition(listcomment.size());

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_event);

        btnPost = (ImageButton) this.findViewById(R.id.btn_postcomment);
        edtComent = (EditText)this.findViewById(R.id.input_comment);
        rcycoment = (RecyclerView)this.findViewById(R.id.listComment);
        avatar = (ImageView)this.findViewById(R.id.imageAvatar);

        reply  = (LinearLayout)this.findViewById(R.id.reply);
        txtreply   = (TextView) this.findViewById(R.id.txtreply);
        btncancel = (ImageView) this.findViewById(R.id.btncancel);

        Drawable mDefaultBackground = this.getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(this).load(AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(avatar);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {

        } else {
            eventid= extras.getLong("Event_id");
           // AccountId= extras.getLong("Account_id");

        }


        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reply.setVisibility(View.GONE);
                typecoment =0;
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtComent.getText().toString().trim().isEmpty()) {
                    if(typecoment ==0) {
                        PostEventComment(eventid);
                    }
                    else
                    {
                        PostEventSubComment(subCommentid);
                    }
                }
            }
        });

        mAdapter = new CommentAdapter(null, Constants.EVENT);
        mAdapter.setOnItemClickListener(this);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcycoment.setLayoutManager(mLayoutManager);
        rcycoment.setItemAnimator(new DefaultItemAnimator());
        rcycoment.setAdapter(mAdapter);

        ListComment(eventid);
    }

    @Override
    public void onItemClick(int position, View v, int Type) {
        if(Type == Constants.CLICK_IMAGE_COMMENT)
        {
            reply.setVisibility(View.VISIBLE);
            typecoment =1;
            subCommentid = listcomment.get(position).getCommentId();
            String sourceString = "Reply to :" + "<b>" + listcomment.get(position).getUsername() + "</b> ";
            txtreply.setText(Html.fromHtml(sourceString));
        }
    }

    @Override
    public void onItemSubClick(int position, View v, int subpos) {

    }
}