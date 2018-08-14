package com.aseanfan.worldcafe.UI.Adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Helper.NotificationCenter;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.NotificationModel;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {


    private List<NotificationModel> notifylist;

    private static NotifyAdapter.ClickListener clickListener;

    public void setOnItemClickListener(NotifyAdapter.ClickListener clickListener) {
      //  NotifyAdapter.clickListener = clickListener;
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


    public NotifyAdapter(List<NotificationModel> notifylist) {
        this.notifylist = notifylist;
    }


    public void setCommentList (List<NotificationModel> data) {
        this.notifylist = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_row, viewGroup, false);

        return new NotifyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.MyViewHolder myViewHolder, int i) {
        NotificationModel notify = notifylist.get(i);
       /* myViewHolder.name.setText(commentModel.getUsername());
        myViewHolder.detail.setText(commentModel.getContent());
        myViewHolder.date.setText(commentModel.getCreatetime());
        Drawable mDefaultBackground = myViewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(myViewHolder.avatar.getContext()).load(commentModel.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(myViewHolder.avatar);*/
    }

    @Override
    public int getItemCount() {
        if (notifylist == null)
        {
            return 0;
        }
        return notifylist.size();
    }
}