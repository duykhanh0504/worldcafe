package com.aseanfan.worldcafe.UI.Adapter;

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

import java.util.List;

public class PostTimelineAdapter extends RecyclerView.Adapter<PostTimelineAdapter.MyViewHolder> {


    private List<EventModel> eventList;

    private static PostTimelineAdapter.ClickListener clickListener;

    public void setOnItemClickListener(PostTimelineAdapter.ClickListener clickListener) {
        PostTimelineAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v, int Type);
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
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_EVENT);
                }
            });

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_EVENT);
        }
    }


    public PostTimelineAdapter(List<EventModel> eventList) {
        this.eventList = eventList;
    }

    public void setEventList (List<EventModel> eventList) {
        this.eventList = eventList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostTimelineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.community_row, viewGroup, false);

        return new PostTimelineAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        EventModel event = eventList.get(i);
        myViewHolder.title.setText(event.getTitle());

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