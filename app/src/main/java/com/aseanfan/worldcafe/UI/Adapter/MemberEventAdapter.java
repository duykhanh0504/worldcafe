package com.aseanfan.worldcafe.UI.Adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.UI.Fragment.MemberEventFragment;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MemberEventAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<UserModel> listuser;

    private static MemberEventAdapter.ClickListener clickListener;

    public void setOnItemClickListener(MemberEventAdapter.ClickListener clickListener) {
        MemberEventAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView avatar;
        public TextView name;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtname);
            avatar = (ImageView) view.findViewById(R.id.imageAvatar);

              view.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
             clickListener.onItemClick(getAdapterPosition(), view );
        }
    }


    public MemberEventAdapter(List<UserModel> data) {
        this.listuser = data;
    }


    public void setData (List<UserModel> data) {
        this.listuser = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_row, viewGroup, false);

        return new MemberEventAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int i) {
        UserModel user = listuser.get(i);
        MemberEventAdapter.MyViewHolder viewHolder = ( MemberEventAdapter.MyViewHolder)myViewHolder;

        viewHolder.name.setText(user.getUsername());
        Drawable mDefaultBackground = viewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(viewHolder.avatar.getContext()).load(user.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(viewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        if (listuser == null)
        {
            return 0;
        }
        return listuser.size();
    }
}