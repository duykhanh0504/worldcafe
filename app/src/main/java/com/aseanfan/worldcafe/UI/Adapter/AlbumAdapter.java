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
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class AlbumAdapter  extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {


    private List<String> imageListUrl;
    private static ClickListener clickListener;

    public  void  setOnItemClickListener(AlbumAdapter.ClickListener clickListener) {
        AlbumAdapter.clickListener = clickListener;
    }

    public void setData(List<String> data)
    {
        imageListUrl = data;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public Context context;


        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            context = view.getContext();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view );
        }
    }


    public AlbumAdapter(List<String> imageListUrl) {
        this.imageListUrl = imageListUrl;
    }


    public void setEventList (List<String> imageListUrl) {
        this.imageListUrl = imageListUrl;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView =new ImageView(viewGroup.getContext());


        return new AlbumAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.MyViewHolder holder, int position) {
        String imageurl = imageListUrl.get(position);
        Glide.with(holder.context).load( imageurl).into( holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (imageListUrl == null)
        {
            return 0;
        }
        return imageListUrl.size();
    }
}