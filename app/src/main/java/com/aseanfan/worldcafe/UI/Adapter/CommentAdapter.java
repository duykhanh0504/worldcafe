package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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

    private static  int type = Constants.TIMELINE;

    public void setOnItemClickListener(CommentAdapter.ClickListener clickListener) {
        CommentAdapter.clickListener = clickListener;
    }

    public void setdata(List<CommentModel> data)
    {
        commentlist = data;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onItemClick(int position, View v, int Type);
        void onItemSubClick(int position, View v, int subpos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,SubCommentAdapter.ClickListener {
        public TextView detail;
        public TextView name;
        public TextView date;
        public ImageView avatar;
        public ImageView iconlike;
        public ImageView iconcomment;
        public TextView numberlike;
        public TextView numbercomment;
        public RecyclerView subcomment;
        public SubCommentAdapter subcommentadapter;


        public MyViewHolder(View view) {
            super(view);
            detail = (TextView) view.findViewById(R.id.txtcontent);
            name = (TextView) view.findViewById(R.id.txtname);
            date = (TextView) view.findViewById(R.id.txtdate);
            avatar = (ImageView) view.findViewById(R.id.imageAvatar);
            iconlike  = (ImageView) view.findViewById(R.id.imageLike);
            iconcomment = (ImageView) view.findViewById(R.id.imageComment);
            numberlike  = (TextView) view.findViewById(R.id.textLike);
            numbercomment = (TextView) view.findViewById(R.id.textComment);
            subcomment = (RecyclerView) view.findViewById(R.id.subcomment);

            view.setOnClickListener(this);
            subcommentadapter  = new SubCommentAdapter(null,type);
            subcommentadapter.setOnItemClickListener(this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
            subcomment.setLayoutManager(mLayoutManager);
            subcomment.setItemAnimator(new DefaultItemAnimator());
            subcomment.setAdapter(subcommentadapter);

            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_EVENT);
                }
            });

            iconlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_LIKE);
                }
            });

            iconcomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_COMMENT);
                }
            });

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_EVENT);
        }

        @Override
        public void onItemClick(int position, View v ,int commentid) {
                clickListener.onItemSubClick(commentid, v , position);

        }
    }


    public CommentAdapter(List<CommentModel> commentList) {
        this.commentlist = commentList;
        type = Constants.TIMELINE;
    }

    public CommentAdapter(List<CommentModel> commentList, int type) {
        this.commentlist = commentList;
        this.type = type;
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
        myViewHolder.date.setText(   Utils.ConvertDiffTime(commentModel.getTimeDiff(),myViewHolder.date.getContext()));
        myViewHolder.numberlike.setText(String.valueOf(commentModel.getNumberLike()));
        myViewHolder.numbercomment.setText(String.valueOf(commentModel.getNumberSubComment()));
        Drawable mDefaultBackground = myViewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        myViewHolder.subcommentadapter.setdata(commentModel.getSubcomment(),i);
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