package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaAdapter  extends RecyclerView.Adapter<ChooseAreaAdapter.MyViewHolder> {

    private  List<String> list = new ArrayList<>();

    public ChooseAreaAdapter(List<String> list) {
        this.list = list;
    }


    public void updatedata(List<String> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textview;
        public Context context;


        public MyViewHolder(View view) {
            super(view);
            textview = (TextView) view;
            context = view.getContext();
            int padding = Utils.convertDpToPixel(1,context);
            textview.setPadding(padding,padding,padding,padding);
            textview.setBackgroundResource(R.drawable.background_button_facebook);
        }

    }

    @NonNull
    @Override
    public ChooseAreaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView =new TextView(viewGroup.getContext());


        return new ChooseAreaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChooseAreaAdapter.MyViewHolder holder, int position) {

        holder.textview.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null)
        {
            return 0;
        }
        return list.size();
    }
}