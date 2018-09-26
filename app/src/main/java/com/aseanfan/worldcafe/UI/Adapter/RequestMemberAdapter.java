package com.aseanfan.worldcafe.UI.Adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Model.FollowModel;
import com.aseanfan.worldcafe.Model.RequestUserModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RequestMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<RequestUserModel> listuser;

    private static RequestMemberAdapter.ClickListener clickListener;

    public void setOnItemClickListener(RequestMemberAdapter.ClickListener clickListener) {
        RequestMemberAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v , int type);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView avatar;
        public TextView name;
        public Button reject;
        public TextView confirm;
        public TextView comment;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtname);
            avatar = (ImageView) view.findViewById(R.id.imageAvatar);
            reject = (Button) view.findViewById(R.id.btnReject);
            confirm = (Button) view.findViewById(R.id.btnConfirm);
            comment = (TextView) view.findViewById(R.id.txtcomment);
            view.setOnClickListener(this);
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view, Constants.MEMBER_REJECT);
                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view,Constants.MEMBER_ACCEPT );
                }
            });

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view,-1 );
        }
    }


    public RequestMemberAdapter(List<RequestUserModel> data) {
        this.listuser = data;
    }


    public void setData (List<RequestUserModel> data) {
        this.listuser = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.request_member_row, viewGroup, false);

        return new RequestMemberAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int i) {
        RequestUserModel user = listuser.get(i);
        RequestMemberAdapter.MyViewHolder viewHolder = ( RequestMemberAdapter.MyViewHolder)myViewHolder;
        if(user.getStatus()== Constants.MEMBER_ACCEPT)
        {
            viewHolder.reject.setVisibility(View.GONE);
            viewHolder.confirm.setText("Accepted");
            viewHolder.confirm.setEnabled(false);
        }
        else
        {
            viewHolder.reject.setVisibility(View.VISIBLE);
            viewHolder.confirm.setText("Accept");
            viewHolder.confirm.setEnabled(true);
        }
        viewHolder.name.setText(user.getUsername());
        if(user.getComment()==null || user.getComment().isEmpty())
        {
            viewHolder.comment.setText("No comment");
        }
        else
        {
            viewHolder.comment.setText(user.getComment());
        }
        Drawable mDefaultBackground = viewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(viewHolder.avatar.getContext()).load(user.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(viewHolder.avatar);
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