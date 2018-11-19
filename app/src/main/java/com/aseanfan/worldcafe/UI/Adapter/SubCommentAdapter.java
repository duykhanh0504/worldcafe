package com.aseanfan.worldcafe.UI.Adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.SubCommentModel;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SubCommentAdapter  extends RecyclerView.Adapter<SubCommentAdapter.MyViewHolder> {


    private List<SubCommentModel> commentlist;
    private int position;
    public int type;

    private static SubCommentAdapter.ClickListener clickListener;

    public void setOnItemClickListener(SubCommentAdapter.ClickListener clickListener) {
        SubCommentAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v,int poscomment);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView detail;
        public TextView name;
        public TextView date;
        public ImageView avatar;
        public ImageView iconlike;
        public TextView numberlike;
        public TextView numbercomment;


        public MyViewHolder(View view) {
            super(view);
            detail = (TextView) view.findViewById(R.id.txtcontent);
            name = (TextView) view.findViewById(R.id.txtname);
            date = (TextView) view.findViewById(R.id.txtdate);
            avatar = (ImageView) view.findViewById(R.id.imageAvatar);
            iconlike  = (ImageView) view.findViewById(R.id.imageLike);
            numberlike  = (TextView) view.findViewById(R.id.textLike);
            numbercomment = (TextView) view.findViewById(R.id.textComment);
            view.setOnClickListener(this);
            iconlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view,position);
                }
            });

        }

        @Override
        public void onClick(View view) {
            //  clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_EVENT);
        }
    }


    public SubCommentAdapter(List<SubCommentModel> commentList) {
        this.commentlist = commentList;
    }

    public SubCommentAdapter(List<SubCommentModel> commentList , int type) {
        this.commentlist = commentList;
        this.type =type;
    }

    public void setdata(List<SubCommentModel> commentList, int id)
    {
        this.commentlist = commentList;
        position =id;
        notifyDataSetChanged();
    }


    public void setCommentList (List<SubCommentModel> commentList) {
        this.commentlist = commentList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubCommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sub_comment_row, viewGroup, false);

        return new SubCommentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCommentAdapter.MyViewHolder myViewHolder, int i) {
        SubCommentModel commentModel = commentlist.get(i);
        myViewHolder.name.setText(commentModel.getUsername());
        myViewHolder.detail.setText(commentModel.getContent());
        myViewHolder.date.setText(   Utils.ConvertDiffTime(commentModel.getTimeDiff(),myViewHolder.date.getContext()));
        myViewHolder.numberlike.setText(String.valueOf(commentModel.getNumberLike()));
        myViewHolder.numbercomment.setText(String.valueOf(commentModel.getNumberSubComment()));
        if(commentModel.getIslike() ==0)
        {
            myViewHolder.iconlike.setBackgroundResource(R.drawable.unlike);
        }
        else
        {
            myViewHolder.iconlike.setBackgroundResource(R.drawable.like);
        }
        if(type == Constants.TIMELINE)
        {
            myViewHolder.iconlike.setVisibility(View.VISIBLE);
            myViewHolder.numberlike.setVisibility(View.VISIBLE);
        }
        else
        {
            myViewHolder.iconlike.setVisibility(View.GONE);
            myViewHolder.numberlike.setVisibility(View.GONE);
        }
        Drawable mDefaultBackground = myViewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(myViewHolder.avatar.getContext()).load(commentModel.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(myViewHolder.avatar);
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