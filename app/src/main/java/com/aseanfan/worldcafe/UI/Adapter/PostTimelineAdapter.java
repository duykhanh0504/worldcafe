package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class PostTimelineAdapter extends RecyclerView.Adapter<PostTimelineAdapter.MyViewHolder> {


    private List<PostTimelineModel> postList;

    private static PostTimelineAdapter.ClickListener clickListener;
    private boolean requestads = false;

    public void setOnItemClickListener(PostTimelineAdapter.ClickListener clickListener) {
        PostTimelineAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v ,int type);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,PostImageAdapter.ClickListener {
        public TextView username;
        public TextView detail;
        public TextView like;
        public TextView comment;
        public RecyclerView imagePost;
        public Context context;
        public ImageView avatar;
        public ImageView imagelike;
        public ImageView imageComment;
        public ImageView image_menu;
        public PostImageAdapter mAdapter;


        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.namePost);
            detail = (TextView) view.findViewById(R.id.detailPost);
            like = (TextView) view.findViewById(R.id.textLike);
            comment = (TextView) view.findViewById(R.id.textComment);
            imagePost = (RecyclerView) view.findViewById(R.id.list_image);
            avatar = (ImageView) view.findViewById(R.id.imageAvatar);
            imagelike = (ImageView) view.findViewById(R.id.imageLike) ;
            image_menu = (ImageView) view.findViewById(R.id.image_menu) ;
            imageComment = (ImageView)view.findViewById(R.id.imageComment) ;
            view.setOnClickListener(this);
            imagelike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_LIKE);
                }
            });

            image_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_MENU);
                }
            });

            imageComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_COMMENT);
                }
            });

            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_AVATAR);
                }
            });

            mAdapter = new PostImageAdapter(null);

           // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(),3);
             RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
            imagePost.setLayoutManager(mLayoutManager);
            imagePost.setItemAnimator(new DefaultItemAnimator());
            imagePost.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);


        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view ,Constants.CLICK_TIMELINE);
        }

        @Override
        public void onItemClick(int position, View v) {
            //clickListener.onItemClick(getAdapterPosition(), v ,Constants.CLICK_TIMELINE);
        }
    }


    public PostTimelineAdapter(List<PostTimelineModel> postList , boolean requestads) {
        this.requestads = requestads;
        this.postList = postList;
    }

    public void setPostList (List<PostTimelineModel> postList) {
        this.postList = postList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostTimelineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_row, viewGroup, false);

        return new PostTimelineAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        PostTimelineModel post = postList.get(i);
        String urlimg = null;
        myViewHolder.username.setText(post.getUsername());
        myViewHolder.like.setText(String.valueOf(post.getNumberLike()));
        myViewHolder.comment.setText(String.valueOf(post.getNumberComment()));
        if(post.getAccountid().equals(AccountController.getInstance().getAccount().getId()))
        {
            myViewHolder.image_menu.setVisibility(View.VISIBLE);
        }
        else
        {
            myViewHolder.image_menu.setVisibility(View.GONE);
        }
        if(post.getIslike() == 0)
        {
            myViewHolder.imagelike.setBackgroundResource(R.drawable.unlike);
        }
        else
        {
            myViewHolder.imagelike.setBackgroundResource(R.drawable.like);
        }
        Drawable mDefaultBackground = myViewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(myViewHolder.avatar.getContext()).load(post.getUrlAvatar()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(myViewHolder.avatar);

        if(post.getDetail()!=null) {
            myViewHolder.detail.setText(post.getDetail());
        }

            if(post.getUrlImage()!=null) {
                myViewHolder.mAdapter.setData(post.getUrlImage());
               /* for (String url : post.getUrlImage()) {
                    //listImage.add(url);
                    urlimg = url;
                }
                if(urlimg!=null) {
                    Glide.with(myViewHolder.context).load(post.getUrlImage().get(0)).into(myViewHolder.imagePost);
                }*/
        }


    }



    @Override
    public int getItemCount() {
        if (postList == null)
        {
            return 0;
        }
        return postList.size();
    }
}