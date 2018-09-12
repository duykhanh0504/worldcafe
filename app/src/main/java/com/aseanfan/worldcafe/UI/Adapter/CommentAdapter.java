package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {


    private List<CommentModel> commentlist;

    private static CommentAdapter.ClickListener clickListener;

    public void setOnItemClickListener(CommentAdapter.ClickListener clickListener) {
        CommentAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v, int Type);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView detail;
        public TextView name;
        public TextView date;
        public ImageView avatar;


        public MyViewHolder(View view) {
            super(view);
            detail = (TextView) view.findViewById(R.id.txtcontent);
            name = (TextView) view.findViewById(R.id.txtname);
            date = (TextView) view.findViewById(R.id.txtdate);
            avatar = (ImageView) view.findViewById(R.id.imageAvatar);
            view.setOnClickListener(this);
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_EVENT);
                }
            });

        }

        @Override
        public void onClick(View view) {
          //  clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_EVENT);
        }
    }


    public CommentAdapter(List<CommentModel> commentList) {
        this.commentlist = commentList;
    }


    public void setCommentList (List<CommentModel> commentList) {
        this.commentlist = commentList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_row, viewGroup, false);

        return new CommentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        CommentModel commentModel = commentlist.get(i);
        myViewHolder.name.setText(commentModel.getUsername());
        myViewHolder.detail.setText(commentModel.getContent());
        myViewHolder.date.setText(   Utils.ConvertDiffTime(commentModel.getTimeDiff()));
        Drawable mDefaultBackground = myViewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(myViewHolder.avatar.getContext()).load(commentModel.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(myViewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        if (commentlist == null)
        {
            return 0;
        }
        return commentlist.size();
    }
}