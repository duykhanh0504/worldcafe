package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aseanfan.worldcafe.worldcafe.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerEventAdapter extends ArrayAdapter<String> {
    public SpinnerEventAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    private String[] items ;
    private LayoutInflater mInflater;

    public void setdata(String[] data )
    {
        items =data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(items ==null)
        {
            return 0;
        }
        return items.length ;
    }

    public SpinnerEventAdapter(@NonNull Context context,
                              @NonNull String[] objects) {
        super(context, 0, objects);

        items = objects;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(R.layout.dropdown_item, parent, false);

        TextView text = (TextView) view.findViewById(R.id.txttext);
        text.setText(items[position]);

        return view;
    }

}