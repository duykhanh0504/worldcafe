package com.aseanfan.worldcafe.UI.Component;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.aseanfan.worldcafe.UI.Adapter.ImagePrevewAdapter;
import com.yanzhenjie.album.impl.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class DIalogImagePreview extends DialogFragment {

    private RecyclerView mRecyclerView;
    private ImagePrevewAdapter adapter;
    private static List<String> listimage;
    private static  int pos =0;

    public static DIalogImagePreview newInstance(List<String> data) {
        listimage = data;
        return new DIalogImagePreview();
    }



    public static DIalogImagePreview newInstance(List<String> data, int Pos) {
        listimage = data;
        pos = Pos;
        return new DIalogImagePreview();
    }


    public static DIalogImagePreview newInstancestring(String data) {
        listimage = new ArrayList<>();
        listimage.clear();
        listimage.add(data);
        return new DIalogImagePreview();
    }

    public void setData(List<String> data)
    {
        listimage= new ArrayList<>();
        listimage = data;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getContext());
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        adapter = new ImagePrevewAdapter(getContext(), listimage, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dismiss();
            }
        });
        // you can use LayoutInflater.from(getContext()).inflate(...) if you have xml layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.scrollToPosition(pos);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        return new AlertDialog.Builder(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                .setView(mRecyclerView)
                .create();
    }
}