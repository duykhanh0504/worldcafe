package com.aseanfan.worldcafe.UI.Adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Helper.NotificationCenter;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.NotificationModel;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<NotificationModel> notifylist;

    private static NotifyAdapter.ClickListener clickListener;

    private int empty = 0;


    public void setOnItemClickListener(NotifyAdapter.ClickListener clickListener) {
        NotifyAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView detail;
        public TextView name;
        public TextView date;
        public ImageView avatar;


        public MyViewHolder(View view) {
            super(view);
            detail = (TextView) view.findViewById(R.id.txtmessage);
            name = (TextView) view.findViewById(R.id.txttitle);
            date = (TextView) view.findViewById(R.id.txttime);
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
              clickListener.onItemClick(getAdapterPosition(), view );
        }
    }


    public NotifyAdapter(List<NotificationModel> notifylist) {
        this.notifylist = notifylist;
    }


    public void setNotifyList (List<NotificationModel> data) {
        this.notifylist = data;
        if(data.size() ==0)
        {
            empty = 1;
        }
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        if(empty ==1)
        {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.emptyview, viewGroup, false);
           // itemView = new ItemViewEmpty(itemView);
            return new ItemViewEmpty(itemView);
        }
        else{
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.notify_row, viewGroup, false);

            return new NotifyAdapter.MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int i) {
        if(empty ==1) {
            ItemViewEmpty viewEmpty = ( ItemViewEmpty)myViewHolder;
            viewEmpty.title.setText("Nothing to show");
        }
        else
        {
            NotifyAdapter.MyViewHolder viewHolder = ( NotifyAdapter.MyViewHolder)myViewHolder;
            NotificationModel notify = notifylist.get(i);
            viewHolder.name.setText(notify.getTitle());
            viewHolder.detail.setText(notify.getMessage());
            viewHolder.date.setText( Utils.ConvertDiffTime(notify.getCreatetime()));
            Drawable mDefaultBackground = viewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
            Glide.with(viewHolder.avatar.getContext()).load(notify.getAvarta()).apply(RequestOptions.circleCropTransform().error(mDefaultBackground)).into(viewHolder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        if (notifylist == null)
        {
            return empty;
        }
        else if(notifylist.size()== 0)
        {
            return empty;
        }
        return notifylist.size();
    }
}