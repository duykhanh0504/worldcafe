package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> {


    private List<String> imageListUrl;
    private static ChatMessageAdapter.ClickListener clickListener;

    public void setOnItemClickListener(ChatMessageAdapter.ClickListener clickListener) {
        ChatMessageAdapter.clickListener = clickListener;
    }

    public void setData(List<String> data) {
        imageListUrl = data;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @NonNull
    @Override
    public ChatMessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_row, viewGroup, false);

        return new ChatMessageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdapter.MyViewHolder myViewHolder, int i) {
    /*    CommentModel commentModel = commentlist.get(i);
        myViewHolder.name.setText(commentModel.getUsername());
        myViewHolder.detail.setText(commentModel.getContent());
        myViewHolder.date.setText(commentModel.getCreatetime());
        Glide.with(myViewHolder.context).load(commentModel.getAvarta()).apply(RequestOptions.circleCropTransform()).into(myViewHolder.avatar);*/
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public Context context;


        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view;
            context = view.getContext();
            int padding = Utils.convertDpToPixel(1, context);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }
}