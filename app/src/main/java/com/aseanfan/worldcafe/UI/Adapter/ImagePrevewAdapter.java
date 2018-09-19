package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.util.AlbumUtils;

import java.util.List;

public class ImagePrevewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;

    private OnItemClickListener mItemClickListener;

    private List<String> imageFile;

    public ImagePrevewAdapter(Context context,List<String> imagePathList, OnItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.imageFile = imagePathList;
        this.mItemClickListener = itemClickListener;
    }

    public void setdata(List<String> imagePathList) {
        this.imageFile = imagePathList;
        super.notifyDataSetChanged();
    }

  /*  @Override
    public int getItemViewType(int position) {
       // String albumFile = imageFile.get(position);
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ImageViewHolder(mInflater.inflate(R.layout.image_preview_content, parent, false),mItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        Glide.with(((ImageViewHolder) holder).mIvImage.getContext()).load(imageFile.get(position)).into(((ImageViewHolder) holder).mIvImage);
        //((ImageViewHolder) holder).setData(imageFile.get(position));


    }

    @Override
    public int getItemCount() {
        return imageFile == null ? 0 : imageFile.size();
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemClickListener mItemClickListener;
        public ImageView mIvImage;

        ImageViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.mItemClickListener = itemClickListener;
            this.mIvImage = itemView.findViewById(R.id.iv_album_content_image);
            itemView.setOnClickListener(this);
        }

     /*   public void setData(String image) {
           /* Album.getAlbumConfig().
                    getAlbumLoader().
                    load(mIvImage, albumFile);
            Glide.with(contain.getContext()).load(url.get(2)).into(image2);
        }*/

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

}