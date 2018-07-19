package com.aseanfan.worldcafe.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private ImageButton btnPost;
    private Long timelineId;
    private Long AccountId;
    private EditText edtComent;

    private RecyclerView rcycoment;

    private CommentAdapter mAdapter;

    private List<CommentModel> listcomment;



    public void PostComment(final Long timelineid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("newfeed_id",timelineid);
        dataJson.addProperty("content",edtComent.getText().toString());

        RestAPI.PostDataMaster(this,dataJson,RestAPI.POST_COMMENT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    ListComment(timelineid);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void ListComment(Long timelineid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("newfeed_id",timelineid);

        RestAPI.PostDataMaster(this,dataJson,RestAPI.GET_COMMENT, new RestAPI.RestAPIListenner() {
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
        setContentView(R.layout.activity_comment);

        btnPost = (ImageButton) this.findViewById(R.id.btn_postcomment);
        edtComent = (EditText)this.findViewById(R.id.input_comment);
        rcycoment = (RecyclerView)this.findViewById(R.id.listComment);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {

        } else {
            timelineId= extras.getLong("Timeline_id");
            AccountId= extras.getLong("Account_id");

        }

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostComment(timelineId);
            }
        });

        mAdapter = new CommentAdapter(null);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcycoment.setLayoutManager(mLayoutManager);
        rcycoment.setItemAnimator(new DefaultItemAnimator());
        rcycoment.setAdapter(mAdapter);

        ListComment(timelineId);
    }
}
