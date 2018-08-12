package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MyViewHolder> {


    private List<EventModel> eventList;

    private static ClickListener clickListener;

    public void setOnItemClickListener(ClickListener clickListener) {
        CommunityAdapter.clickListener = clickListener;
    }

    public void setData(List<EventModel> data) {
        eventList = data;
        notifyDataSetChanged();
    }


    public interface ClickListener {
        void onItemClick(int position, View v,int Type);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView name;
        public TextView price;
        public ImageView imageAvatar;
        public TextView numberlike;
        public TextView numbercomment;
        public Context context;

        public MyViewHolder(View view) {
            super(view);
            context =view.getContext();
            title = (TextView) view.findViewById(R.id.txttitle);
            name = (TextView) view.findViewById(R.id.txtname);
            price = (TextView) view.findViewById(R.id.txtprice);
            imageAvatar = (ImageView) view.findViewById(R.id.imageAvatar);
            numberlike = (TextView) view.findViewById(R.id.textLike);
            numbercomment = (TextView) view.findViewById(R.id.textComment);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_EVENT);
        }
    }


    public CommunityAdapter(List<EventModel> eventList) {
        this.eventList = eventList;
    }


    public void setEventList (List<EventModel> eventList) {
        this.eventList = eventList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.community_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventModel event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.name.setText(event.getUsername());
        holder.price.setText(event.getPrice().toString());
        holder.numberlike.setText(String.valueOf(event.getNumberLike()));
        holder.numbercomment.setText(String.valueOf(event.getNumberComment()));
        if(event.getUrlAvatar()!=null)
        {
            Glide.with(holder.context).load(event.getUrlAvatar()).apply(RequestOptions.circleCropTransform()).into(holder.imageAvatar);
        }
    }

    @Override
    public int getItemCount() {
        if (eventList == null)
        {
            return 0;
        }
        return eventList.size();
    }
}