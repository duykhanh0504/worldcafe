package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;

import java.util.List;

public class SettingAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private int  count;

    private static SettingAdapter.ClickListener clickListener;

    public void setOnItemClickListener(SettingAdapter.ClickListener clickListener) {
        SettingAdapter.clickListener = clickListener;
    }

    public SettingAdapter(int count) {
       this.count = count;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == Constants.SETTING_TITLE_ROW)
        {

            return 0;

        }
        else if (position == Constants.SETTING_CONTACT_ROW || position == Constants.SETTING_CHANGEPASS_ROW ||
                position == Constants.SETTING_LOGOUT_ROW || position == Constants.SETTING_CONTRACT_ROW )
        {

            return 1;
        }
        else if(position == Constants.SETTING_VERSION_ROW)
        {
            return 2;
        }
        return -1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public MyViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view );
        }
    }

    static class ViewHolderVersion extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        public ViewHolderVersion(View view) {
            super(view);
            title =  view.findViewById(R.id.textversion);
        }

        @Override
        public void onClick(View view) {
              clickListener.onItemClick(getAdapterPosition(), view );

        }
    }


    static class ViewHolderTitle extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        public ViewHolderTitle(View view) {
            super(view);
            title =  view.findViewById(R.id.txttitle);

        }

        @Override
        public void onClick(View view) {
              clickListener.onItemClick(getAdapterPosition(), view );

        }
    }


    static class ViewHolderRowSetting extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView image;

        public ViewHolderRowSetting(View view) {
            super(view);
            title =  view.findViewById(R.id.txttitle);
            image =  view.findViewById(R.id.image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

              clickListener.onItemClick(getAdapterPosition(), view );

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;

        switch (i) {
            case 0:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.title_row, viewGroup, false);
                return new SettingAdapter.ViewHolderTitle(itemView);
            case 1:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.setting_row, viewGroup, false);
                return new SettingAdapter.ViewHolderRowSetting(itemView);
            case 2:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.version_row, viewGroup, false);
                return new SettingAdapter.ViewHolderVersion(itemView);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder myViewHolder, int i) {
        switch (myViewHolder.getItemViewType()) {

            case 0:
                ViewHolderTitle title = (ViewHolderTitle)myViewHolder;
                title.title.setText("Application");
                break;

            case 1:
                ViewHolderRowSetting setting = (ViewHolderRowSetting)myViewHolder;
                if(i == Constants.SETTING_CONTACT_ROW)
                {
                    setting.title.setText("Contact us");
                }
                if(i == Constants.SETTING_CHANGEPASS_ROW)
                {
                    setting.title.setText("Change password");
                }
                if(i == Constants.SETTING_CONTRACT_ROW)
                {
                    setting.title.setText("Contract");
                }
                if(i == Constants.SETTING_LOGOUT_ROW)
                {
                    setting.title.setText("logout");
                    setting.image.setVisibility(View.GONE);
                }
                break;
            case 2:
                ViewHolderVersion version = (ViewHolderVersion)myViewHolder;
                version.title.setText("1.0.0");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return count +1;
    }
}