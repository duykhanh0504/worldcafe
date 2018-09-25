package com.aseanfan.worldcafe.UI.Component;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class CustomButton extends Button {

    public CustomButton(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        this.setTypeface(face);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        this.setTypeface(face);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }
}
