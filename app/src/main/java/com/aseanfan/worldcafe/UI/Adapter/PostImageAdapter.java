package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PostImageAdapter  extends RecyclerView.Adapter<PostImageAdapter.MyViewHolder> {


    private List<String> imageListUrl;
    private static ClickListener clickListener;

    public void setOnItemClickListener(PostImageAdapter.ClickListener clickListener) {
        PostImageAdapter.clickListener = clickListener;
    }

    public void setData(List<String> data) {
        data.remove("");
        imageListUrl = data;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }


    public PostImageAdapter(List<String> imageListUrl) {
        this.imageListUrl = imageListUrl;
    }


    public void setEventList(List<String> imageListUrl) {
        this.imageListUrl = imageListUrl;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_row, viewGroup, false);

        return new PostImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String imageurl = imageListUrl.get(i);
        Glide.with(myViewHolder.imageView.getContext()).load(imageurl).apply(new RequestOptions()
                .override(Utils.getwidthScreen(myViewHolder.imageView.getContext()), Utils.getwidthScreen(myViewHolder.imageView.getContext())) // set exact size
                .centerCrop()).into(myViewHolder.imageView);
    }


    @Override
    public int getItemCount() {
        if (imageListUrl == null) {
            return 0;
        }
        return imageListUrl.size();
    }
}