package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;

public class ItemViewEmpty  extends RecyclerView.ViewHolder {

    public TextView title;

    public ItemViewEmpty(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.txtempty);

    }

}
