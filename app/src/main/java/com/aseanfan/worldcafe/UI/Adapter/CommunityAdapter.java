package com.aseanfan.worldcafe.UI.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.worldcafe.R;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MyViewHolder> {

    private List<EventModel> eventList;

    private static ClickListener clickListener;

    public void setOnItemClickListener(ClickListener clickListener) {
        CommunityAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v,int Type);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView name;
        public TextView price;
        public ImageView imageEvent;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txttitle);
            name = (TextView) view.findViewById(R.id.txtname);
            price = (TextView) view.findViewById(R.id.txtprice);
            imageEvent = (ImageView) view.findViewById(R.id.imageEvent);
            view.setOnClickListener(this);
            imageEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , 1);
                }
            });

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view , 0);
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
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}