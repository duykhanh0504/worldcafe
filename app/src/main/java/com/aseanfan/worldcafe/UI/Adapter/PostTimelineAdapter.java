package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class PostTimelineAdapter extends RecyclerView.Adapter<PostTimelineAdapter.MyViewHolder> {


    private List<PostTimelineModel> postList;

    private static PostTimelineAdapter.ClickListener clickListener;

    public void setOnItemClickListener(PostTimelineAdapter.ClickListener clickListener) {
        PostTimelineAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v ,int type);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView detail;
        public TextView like;
        public TextView comment;
        public ImageView imagePost;
        public Context context;
        public ImageView avatar;
        public ImageView imagelike;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titlePost);
            detail = (TextView) view.findViewById(R.id.detailPost);
            like = (TextView) view.findViewById(R.id.textLike);
            comment = (TextView) view.findViewById(R.id.textComment);
            imagePost = (ImageView) view.findViewById(R.id.imagePost);
            avatar = (ImageView) view.findViewById(R.id.imageAvatar);
            imagelike = (ImageView) view.findViewById(R.id.imageLike) ;
            context = view.getContext();
            view.setOnClickListener(this);
            imagelike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_LIKE);
                }
            });

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view ,Constants.CLICK_TIMELINE);
        }
    }


    public PostTimelineAdapter(List<PostTimelineModel> postList) {
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
        myViewHolder.title.setText(post.getTitle());
        myViewHolder.like.setText(String.valueOf(post.getNumberLike()));
        myViewHolder.comment.setText(String.valueOf(post.getNumberComment()));
        if(post.getUrlAvatar()!=null)
        {
            Glide.with(myViewHolder.context).load(post.getUrlAvatar()).apply(RequestOptions.circleCropTransform()).into(myViewHolder.avatar);
        }
        if(post.getDetail()!=null) {
            myViewHolder.detail.setText(post.getDetail());
        }

            if(post.getUrlImage()!=null) {
                for (String url : post.getUrlImage()) {
                    //listImage.add(url);
                    urlimg = url;
                }
                if(urlimg!=null) {
                    Glide.with(myViewHolder.context).load(urlimg).into(myViewHolder.imagePost);
                }
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