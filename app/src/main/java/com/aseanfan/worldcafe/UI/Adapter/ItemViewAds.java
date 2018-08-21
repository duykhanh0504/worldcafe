package com.aseanfan.worldcafe.UI.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aseanfan.worldcafe.worldcafe.R;
import com.google.android.gms.ads.AdView;

public class ItemViewAds extends RecyclerView.ViewHolder {

    private AdView adView;

    public ItemViewAds(View view) {
        super(view);
        adView = (AdView) view.findViewById(R.id.ad_view);

    }

}
