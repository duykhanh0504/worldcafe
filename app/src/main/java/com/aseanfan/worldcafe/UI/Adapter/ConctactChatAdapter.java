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

import com.aseanfan.worldcafe.Model.ChatModel;
import com.aseanfan.worldcafe.Model.FollowModel;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ConctactChatAdapter  extends RecyclerView.Adapter<ConctactChatAdapter.MyViewHolder> {


    private List<ChatModel> contactlist;

    private static ConctactChatAdapter.ClickListener clickListener;

    public void setOnItemClickListener(ConctactChatAdapter.ClickListener clickListener) {
        ConctactChatAdapter.clickListener = clickListener;
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
            detail = (TextView) view.findViewById(R.id.txtcontent);
            name = (TextView) view.findViewById(R.id.txtname);
            date = (TextView) view.findViewById(R.id.txtdate);
            avatar = (ImageView) view.findViewById(R.id.imageAvatar);
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view );
        }
    }


    public ConctactChatAdapter(List<ChatModel> contactlist) {
        this.contactlist = contactlist;
    }


    public void setFollowlist (List<ChatModel> contactlist) {
        this.contactlist = contactlist;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConctactChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_chat_row, viewGroup, false);

        return new ConctactChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConctactChatAdapter.MyViewHolder myViewHolder, int i) {
        ChatModel contact = contactlist.get(i);
        myViewHolder.name.setText(contact.getUsername());
         Drawable mDefaultBackground = myViewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(myViewHolder.avatar.getContext()).load(contact.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(myViewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        if (contactlist == null)
        {
            return 0;
        }
        return contactlist.size();
    }
}