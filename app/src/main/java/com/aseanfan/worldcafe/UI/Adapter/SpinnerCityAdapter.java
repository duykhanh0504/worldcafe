package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aseanfan.worldcafe.Model.AreaModel;
import com.aseanfan.worldcafe.Model.CityModel;

import java.util.ArrayList;
import java.util.List;

public class SpinnerCityAdapter  extends ArrayAdapter<String> {
    public SpinnerCityAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    private List<CityModel> items = new ArrayList<>();
    private  int mResource;
    private LayoutInflater mInflater;

    public void setdata(List<CityModel> data )
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
        return items.size() ;
    }

    public SpinnerCityAdapter(@NonNull Context context,  int resource,
                              @NonNull List objects) {
        super(context, resource, 0, objects);

        items = objects;
        mResource = resource;
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
        final View view = mInflater.inflate(mResource, parent, false);

        TextView text = (TextView) view.findViewById(android.R.id.text1);
        text.setText(items.get(position).getname());

        return view;
    }

}