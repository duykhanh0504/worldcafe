package com.aseanfan.worldcafe.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aseanfan.worldcafe.worldcafe.R;
import com.google.android.gms.maps.internal.IMapFragmentDelegate;

import java.security.Principal;

public class ContractActivity extends AppCompatActivity {

    private ImageView btncancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        btncancel = (ImageView)this.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
