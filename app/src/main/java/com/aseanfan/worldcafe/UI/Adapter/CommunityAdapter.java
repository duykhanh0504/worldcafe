package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

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

    public List<EventModel> getEvent() {
       return eventList;
    }


    public interface ClickListener {
        void onItemClick(int position, View v,int Type,EventModel event);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView name;
        public TextView price;
        public ImageView imageAvatar;
        public TextView numberlike;
        public TextView numbercomment;
        public Context context;
        public TextView type;
        public TextView time;
        public TextView location;
        public LinearLayout background;

        public MyViewHolder(View view) {
            super(view);
            context =view.getContext();
            title = (TextView) view.findViewById(R.id.txttitle);
            name = (TextView) view.findViewById(R.id.txtname);
            price = (TextView) view.findViewById(R.id.txtprice);
            imageAvatar = (ImageView) view.findViewById(R.id.imageAvatar);
            numberlike = (TextView) view.findViewById(R.id.textLike);
            numbercomment = (TextView) view.findViewById(R.id.textComment);
            type = (TextView) view.findViewById(R.id.txttype);
            time = (TextView) view.findViewById(R.id.txtdate);
            location = (TextView) view.findViewById(R.id.txtlocation);
            background = (LinearLayout) view.findViewById(R.id.background_event);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_EVENT ,eventList.get(getAdapterPosition()));
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        EventModel event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.name.setText(event.getUsername());
        holder.price.setText(Utils.currencyFormat(event.getPrice()) + " $");
        if(event.getType() == Constants.EVENT_FRIEND)
        {
            holder.type.setText(holder.type.getContext().getText(R.string.Friend));
        }
        else if (event.getType() == Constants.EVENT_BUSSINESS)
        {
            holder.type.setText(holder.type.getContext().getText(R.string.Business));
        }
        else if (event.getType() == Constants.EVENT_LOCAL)
        {
            holder.type.setText(holder.type.getContext().getText(R.string.Local));
        }
        else
        {
            holder.type.setText(holder.type.getContext().getText(R.string.Language));
        }
        holder.time.setText(Utils.ConvertDate(event.getStarttime()));
        holder.location.setText(event.getCityname());

        holder.numberlike.setText(String.valueOf(event.getNumberLike()));
        holder.numbercomment.setText(String.valueOf(event.getNumberComment()));
        Drawable mDefaultBackground = holder.imageAvatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(holder.imageAvatar.getContext()).load(event.getUrlAvatar()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(holder.imageAvatar);
        if(event.getUrlImage().get(0)!=null) {
            Glide.with(holder.background.getContext()).load(event.getUrlImage().get(0)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    holder.background.setBackgroundDrawable(resource);
                }
            });
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