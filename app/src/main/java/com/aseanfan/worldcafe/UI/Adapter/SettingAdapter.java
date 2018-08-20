package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aseanfan.worldcafe.worldcafe.R;

public class SettingAdapter  extends RecyclerView.Adapter<SettingAdapter.MyViewHolder> {



    private static FollowAdapter.ClickListener clickListener;

    public void setOnItemClickListener(FollowAdapter.ClickListener clickListener) {
        SettingAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public MyViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view );
        }
    }


    static class ViewHolderTitle extends RecyclerView.ViewHolder implements View.OnClickListener {



        public ViewHolderTitle(View view) {
            super(view);
        }

        @Override
        public void onClick(View view) {
            //  clickListener.onItemClick(getAdapterPosition(), view );

        }
    }

    static class ViewHolderColSetting extends RecyclerView.ViewHolder implements View.OnClickListener {



        public ViewHolderColSetting(View view) {
            super(view);
        }

        @Override
        public void onClick(View view) {
            //  clickListener.onItemClick(getAdapterPosition(), view );

        }
    }

    @NonNull
    @Override
    public SettingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_row, viewGroup, false);

        return new SettingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}