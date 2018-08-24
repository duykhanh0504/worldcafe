package com.aseanfan.worldcafe.UI.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.UI.Adapter.PostImageAdapter;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class DetailTimelineFragment  extends android.support.v4.app.Fragment implements View.OnClickListener {


    private ImageView avatar;
    private TextView username;
    public PostImageAdapter mAdapter;
    public RecyclerView imagePost;
    public TextView like;
    public TextView comment;
    public TextView detail;
    public ImageView imagelike;

    private RecyclerView rcycoment;

    private CommentAdapter commentAdapter;

    private List<CommentModel> listcomment;

    public void ListComment(Long timelineid)
    {
        String url;
        url = String.format(RestAPI.GET_COMMENT, timelineid);

        RestAPI.GetDataMasterWithToken(getActivity(),url, new RestAPI.RestAPIListenner() {
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
                    commentAdapter.setCommentList(listcomment);
                    rcycoment.smoothScrollToPosition(listcomment.size());

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void initview(View view )
    {
        avatar = (ImageView)view.findViewById(R.id.imageAvatar);
        username = (TextView)view.findViewById(R.id.namePost);
        imagePost = (RecyclerView) view.findViewById(R.id.list_image);
        like = (TextView) view.findViewById(R.id.textLike);
        comment = (TextView) view.findViewById(R.id.textComment);
        detail = (TextView) view.findViewById(R.id.detailPost);
        imagelike = (ImageView) view.findViewById(R.id.imageLike) ;

        mAdapter = new PostImageAdapter(null);

        rcycoment = (RecyclerView)view.findViewById(R.id.listComment);

        // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(),3);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        imagePost.setLayoutManager(mLayoutManager);
        imagePost.setItemAnimator(new DefaultItemAnimator());
        imagePost.setAdapter(mAdapter);

    }

    public void initdata()
    {
        if (getArguments() != null) {
            PostTimelineModel timeline = new PostTimelineModel();
            timeline.setUrlImage((List<String>)getArguments().getStringArrayList("listimage"));
            timeline.setUsername(getArguments().getString("username"));
            timeline.setNumberLike(getArguments().getInt("numberlike"));
            timeline.setNumberComment(getArguments().getInt("numbercomment"));
            timeline.setTimelineid(getArguments().getLong("timelineid"));
            timeline.setDetail(getArguments().getString("detail"));
            timeline.setIslike(getArguments().getInt("islike"));


            Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
            Glide.with(getContext()).load(timeline.getUrlAvatar()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(avatar);
            username.setText(timeline.getUsername());
            detail.setText(timeline.getDetail());

            mAdapter.setData(timeline.getUrlImage());
            like.setText(String.valueOf(timeline.getNumberLike()));
            comment.setText(String.valueOf(timeline.getNumberComment()));

            if(timeline.getIslike() == 0)
            {
                imagelike.setBackgroundResource(R.drawable.unlike);
            }
            else
            {
                imagelike.setBackgroundResource(R.drawable.like);
            }


            commentAdapter = new CommentAdapter(null);


            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rcycoment.setLayoutManager(mLayoutManager);
            rcycoment.setItemAnimator(new DefaultItemAnimator());
            rcycoment.setAdapter(commentAdapter);

            ListComment(timeline.getTimelineid());


        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_timeline_fragment, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        initview(view);

        initdata();


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
