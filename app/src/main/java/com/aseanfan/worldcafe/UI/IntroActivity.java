package com.aseanfan.worldcafe.UI;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    private int[] listtitle;


    @BindView(R.id.Signup)
    Button signup;

    @BindView(R.id.Login)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.aseanfan.worldcafe.worldcafe",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        }

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
        String token = FirebaseInstanceId.getInstance().getToken();
      /*  FirebaseApp.initializeApp(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                       // String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("", token);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });*/


    }

    public class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {

            if (position <= -1.0F || position >= 1.0F) {        // [-Infinity,-1) OR (1,+Infinity]
                view.setAlpha(0.0F);
                view.setVisibility(View.GONE);
            } else if( position == 0.0F ) {     // [0]
                view.setAlpha(1.0F);
                view.setVisibility(View.VISIBLE);
            } else {

                // Position is between [-1,1]
                view.setAlpha(1.0F - Math.abs(position));
              //  view.setTranslationX(-position * (view.getWidth() / 2));
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    void UiInit()
    {
        images = new int[]{
                R.drawable.intro1,
                R.drawable.intro2,
                R.drawable.intro3,
                R.drawable.intro4
        };
        listtitle = new int[]{
                R.string.intro1,
                R.string.intro2,
                R.string.intro3
        };

        viewPager.setAdapter(new IntroAdapter());
        viewPager.setPageMargin(0);
        viewPager.setOffscreenPageLimit(1);

        viewPager.setPageTransformer(true,new  FadePageTransformer());
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
            TextView title = (TextView) view.findViewById(R.id.txttitle);
            ImageView icon = (ImageView) view.findViewById(R.id.icon_image);

            container.addView(view, 0);

            headerimage.setBackgroundResource(images[position]);
            if(position ==0)
            {
                title.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
            }
            else
            {
                title.setVisibility(View.VISIBLE);
                icon.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(listtitle[position-1]));
            }
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
