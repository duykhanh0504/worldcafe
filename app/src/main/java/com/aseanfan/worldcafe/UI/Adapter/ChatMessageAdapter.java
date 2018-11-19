package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<ChatMessageModel> listmessagechat;
    private String friendAvatar;
    private static ChatMessageAdapter.ClickListener clickListener;

    public void setOnItemClickListener(ChatMessageAdapter.ClickListener clickListener) {
        ChatMessageAdapter.clickListener = clickListener;
    }

    public ChatMessageAdapter(List<ChatMessageModel> data , String avatar) {
        this.listmessagechat = data;
        friendAvatar = avatar;
    }

    public void setData(List<ChatMessageModel> data) {
        listmessagechat = data;
        notifyDataSetChanged();
    }
    public void setDataRow(ChatMessageModel data) {
        listmessagechat.add(0,data);
        notifyItemChanged(0);
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @Override
    public int getItemViewType(int position) {
        if(listmessagechat.get(position).getSend_account().equals(AccountController.getInstance().getAccount().getId()))
        {
            if(listmessagechat.get(position).getType() == 0) {
                return 0;
            }
            else
            {
                return 2;
            }
        }
        else {
            if(listmessagechat.get(position).getType() == 0) {
                return 1;
            }
            else
            {
                return 3;
            }
        }
    }

    static class ViewHolderLeftChat extends RecyclerView.ViewHolder implements View.OnClickListener {

        hani.momanii.supernova_emoji_library.Helper.EmojiconTextView mMessage;
        ImageView mAvatar;
        Context mContext;

        public ViewHolderLeftChat(View view) {
            super(view);
            mMessage = (hani.momanii.supernova_emoji_library.Helper.EmojiconTextView) view.findViewById(R.id.txtchat);
            mAvatar = (ImageView) view.findViewById(R.id.imageAvatar);
            mContext = view.getContext();
        }

        @Override
        public void onClick(View view) {
              //  clickListener.onItemClick(getAdapterPosition(), view );

        }
    }

    static class ViewHolderRightCHat extends RecyclerView.ViewHolder implements View.OnClickListener {

        hani.momanii.supernova_emoji_library.Helper.EmojiconTextView mMessage;
        ImageView mAvatar;

        public ViewHolderRightCHat(View view) {
            super(view);
            mMessage = (hani.momanii.supernova_emoji_library.Helper.EmojiconTextView) view.findViewById(R.id.txtchat);
            mAvatar = (ImageView) view.findViewById(R.id.imageAvatar);
        }

        @Override
        public void onClick(View view) {
           //     clickListener.onItemClick(getAdapterPosition(), view );
        }
    }
    static class ViewHolderRightImage extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;

        public ViewHolderRightImage(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.imagechat);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
                 clickListener.onItemClick(getAdapterPosition(), view );
        }
    }
    static class ViewHolderLeftImage extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;

        public ViewHolderLeftImage(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.imagechat);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
                 clickListener.onItemClick(getAdapterPosition(), view );
        }
    }
    @Override
    public int getItemCount() {
        if(listmessagechat==null)
        {
            return 0;
        }
        return listmessagechat.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        switch (i) {
            case 0:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.right_chat_row, viewGroup, false);
                return new ViewHolderRightCHat(itemView);
            case 1:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.left_chat_row, viewGroup, false);
                return new ViewHolderLeftChat(itemView);
            case 2:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.right_chat_image, viewGroup, false);
                return new ViewHolderRightImage(itemView);
            case 3:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.left_chat_image, viewGroup, false);
                return new ViewHolderLeftImage(itemView);
                default:
                    return new ViewHolderRightCHat(null);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {

            case 0:
                ViewHolderRightCHat right = (ViewHolderRightCHat)viewHolder;
                if(i < listmessagechat.size()-1 && (listmessagechat.get(i).getSend_account() == listmessagechat.get(i+1).getSend_account())) {
                    right.mAvatar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    right.mAvatar.setVisibility(View.VISIBLE);
                    Drawable mDefaultBackground = right.mAvatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
                    Glide.with(right.mAvatar.getContext()).load(AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(right.mAvatar);

                }
             //   right.mAvatar.setVisibility(View.GONE);
               // Drawable mDefaultBackground = right.mAvatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
               // Glide.with(right.mAvatar.getContext()).load(AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(right.mAvatar);
             //   right.mMessage.setText(Utils.decodeStringUrl(listmessagechat.get(i).getMessageText()));
                right.mMessage.setText((listmessagechat.get(i).getMessageText()));
                break;

            case 1:
                ViewHolderLeftChat left = (ViewHolderLeftChat)viewHolder;
                if(i < listmessagechat.size()-1 && (listmessagechat.get(i).getSend_account() == listmessagechat.get(i+1).getSend_account())) {
                    left.mAvatar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    left.mAvatar.setVisibility(View.VISIBLE);
                    Drawable mDefaultBackground = left.mAvatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
                    Glide.with(left.mAvatar.getContext()).load(friendAvatar).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(left.mAvatar);

                }
              //  left.mMessage.setText(Utils.decodeStringUrl(listmessagechat.get(i).getMessageText()));
                left.mMessage.setText((listmessagechat.get(i).getMessageText()));
                break;
            case 2:
                ViewHolderRightImage rightimage = (ViewHolderRightImage)viewHolder;
                if(listmessagechat.get(i).getMessageText().contains("http://crosea1.g-days.net") == false)
                {
                    byte[] imageByteArray= Base64.decode(listmessagechat.get(i).getMessageText(), Base64.DEFAULT);
                    Glide.with(rightimage.image.getContext()).load(imageByteArray).into(rightimage.image);
                }
                else {
                    Glide.with(rightimage.image.getContext()).load(listmessagechat.get(i).getMessageText()).into(rightimage.image);
                }
                break;
            case 3:
                ViewHolderLeftImage leftimage = (ViewHolderLeftImage)viewHolder;
                if(listmessagechat.get(i).getMessageText().contains("http://crosea1.g-days.net") == false)
                {
                    byte[] imageByteArray= Base64.decode(listmessagechat.get(i).getMessageText(), Base64.DEFAULT);
                    Glide.with(leftimage.image.getContext()).load(imageByteArray).into(leftimage.image);
                }
                else {
                    Glide.with(leftimage.image.getContext()).load(listmessagechat.get(i).getMessageText()).into(leftimage.image);
                }
                break;
        }
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