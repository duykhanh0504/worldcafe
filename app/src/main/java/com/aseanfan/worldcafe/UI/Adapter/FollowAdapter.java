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
import com.aseanfan.worldcafe.Model.FollowModel;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class FollowAdapter  extends RecyclerView.Adapter<FollowAdapter.MyViewHolder> {


    private List<FollowModel> followlist;

    private static FollowAdapter.ClickListener clickListener;

    public void setOnItemClickListener(FollowAdapter.ClickListener clickListener) {
        FollowAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(Long position, View v);
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


        }

        @Override
        public void onClick(View view) {
              clickListener.onItemClick(followlist.get(getAdapterPosition()).getAccount_id(), view );
        }
    }


    public FollowAdapter(List<FollowModel> followlist) {
        this.followlist = followlist;
    }


    public void setFollowlist (List<FollowModel> followList) {
        this.followlist = followList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.followrow, viewGroup, false);

        return new FollowAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowAdapter.MyViewHolder myViewHolder, int i) {
        FollowModel follow = followlist.get(i);
        myViewHolder.name.setText(follow.getUsername());
        Drawable mDefaultBackground = myViewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(myViewHolder.avatar.getContext()).load(follow.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(myViewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        if (followlist == null)
        {
            return 0;
        }
        return followlist.size();
    }
}