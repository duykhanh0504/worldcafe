package com.aseanfan.worldcafe.UI;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Adapter.ImageTimelineAdapter;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

import java.util.ArrayList;
import java.util.List;

public class EditPostTimeline extends AppCompatActivity {

    private RecyclerView listimage;
    private Button btnshare;
    private EditText edtShare;
    private String listImageBase64 = "";

    private ImageView avatar;
    private TextView username;

    private RadioGroup radgroup;
    private RadioGroup radgroupquestion;
    private PostTimelineModel post;

    public void EditPost(final int pos)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("newfeed_id", post.getTimelineid());
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());

        RestAPI.PostDataMasterWithToken(EditPostTimeline.this.getApplicationContext(),dataJson,RestAPI.POST_DELETE_TIMELINE, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                   // posttimeline.remove(pos);
                  //  Adapter.setPostList(posttimeline);

                }
                catch (Exception ex) {
                }
            }
        });
    }
    private void changeGenre(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if(checkedRadioId== R.id.friend) {
            post.setGenre(Constants.EVENT_FRIEND);
        } else if(checkedRadioId== R.id.business ) {
            post.setGenre(Constants.EVENT_BUSSINESS);
        } else if(checkedRadioId== R.id.language) {
            post.setGenre(Constants.EVENT_LANGUAGE);
        }
        else if(checkedRadioId== R.id.local) {
            post.setGenre(Constants.EVENT_LOCAL);
        }
    }

    private void changeType(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if(checkedRadioId== R.id.radtimeline) {
           post.setType(0);
            radgroupquestion.setVisibility(View.GONE);
            post.setGenre(-1);
        } else if(checkedRadioId== R.id.radquestion ) {
            post.setGenre(1);
            radgroupquestion.setVisibility(View.VISIBLE);
            radgroupquestion.check(R.id.radfriend);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_timeline);
        post= new PostTimelineModel();

        listimage= findViewById(R.id.list_image);
        btnshare= findViewById(R.id.btnshare);
        edtShare= findViewById(R.id.input_detail);

        radgroup = findViewById(R.id.radgroup);
        radgroupquestion = findViewById(R.id.radgroupquestion);
        radgroupquestion.setVisibility(View.GONE);
        radgroup.check(R.id.radtimeline);

        avatar = findViewById(R.id.imageAvatar);
        username = findViewById(R.id.txtusername);

        post.setTimelineid(getIntent().getLongExtra("timelineis",-1));
        post.setUrlImage((List<String>) getIntent().getStringArrayListExtra("listimage"));
        post.setDetail(getIntent().getStringExtra("detail"));
        post.setType(getIntent().getIntExtra("type",0));
        post.setGenre(getIntent().getIntExtra("genre",-1));


        edtShare.setText(post.getDetail());

        if(post.getType() == 0)
        {
            radgroup.check(R.id.radtimeline);
            radgroupquestion.setVisibility(View.GONE);
        }
        else
        {
            radgroup.check(R.id.radquestion);
            radgroupquestion.setVisibility(View.VISIBLE);
            if(post.getGenre() == (Constants.EVENT_FRIEND)) {
                radgroupquestion.check(R.id.friend);
            } else if(post.getGenre() == (Constants.EVENT_BUSSINESS) ) {
                radgroupquestion.check(R.id.business);
            } else if(post.getGenre() == (Constants.EVENT_LANGUAGE)) {
                radgroupquestion.check(R.id.language);
            }
            else if(post.getGenre() == (Constants.EVENT_LOCAL)) {
                radgroupquestion.check(R.id.local);
            }
        }

        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeType(group, checkedId);
            }
        });

        radgroupquestion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeGenre(group, checkedId);
            }
        });

        username.setText(AccountController.getInstance().getAccount().getUsername());
        Drawable mDefaultBackground = this.getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(this).load(AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(avatar);

        listimage.setLayoutManager(new GridLayoutManager(this, 3));
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        listimage.addItemDecoration(divider);


        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listImageBase64.length() == 0 && edtShare.getText().toString().isEmpty() ) {
                    ViewDialog dialog = new ViewDialog();
                    dialog.showDialogCancel(EditPostTimeline.this,"no information to share");
                }
                else
                {
                   // PostTimeline(listImageBase64);
                }
            }
        });
    }
}
