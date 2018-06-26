package com.aseanfan.worldcafe.UI;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.worldcafe.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.intro_view_pager)
    ViewPager viewPager;

    @BindView(R.id.icon_introtext)
    ImageView textintro;
    @BindView(R.id.bottom_pages)
    ViewGroup bottomPages;

    private int lastPage = 0;
    private boolean justCreated = false;
    private boolean startPressed = false;
    private int[] images;


    @BindView(R.id.Signup)
    Button signup;

    @BindView(R.id.Login)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(Store.getBooleanData(IntroActivity.this , Store.LOGGED) == true)
        {
            Intent intent = new Intent(IntroActivity.this , MainActivity.class);
            startActivity(intent);
            finish();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        UiInit();

        justCreated = true;
    }

    void UiInit()
    {
        images = new int[]{
                R.drawable.introl1,
                R.drawable.introl2,
                R.drawable.introl3,
                R.drawable.introl4
        };

        viewPager.setAdapter(new IntroAdapter());
        viewPager.setPageMargin(0);
        viewPager.setOffscreenPageLimit(1);
    }

    @OnClick(R.id.Login)
    public void Login() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        intent.putExtra("type",0);
        startActivity(intent);
    }

    @OnClick(R.id.Signup)
    public void Signup() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        intent.putExtra("type",2);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (justCreated) {

            viewPager.setCurrentItem(0);
            lastPage = 0;
            justCreated = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class IntroAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(container.getContext(), R.layout.intro_view_layout, null);
            ImageView headerimage = (ImageView) view.findViewById(R.id.header_image);
            container.addView(view, 0);

            headerimage.setBackgroundResource(images[position]);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            int count = bottomPages.getChildCount();
            for (int a = 0; a < count; a++) {
                View child = bottomPages.getChildAt(a);
                if (a == position) {
                    child.setBackgroundResource(R.drawable.circle_select);
                } else {
                    child.setBackgroundResource(R.drawable.circle);
                }
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer != null) {
                super.unregisterDataSetObserver(observer);
            }
        }
    }
}
