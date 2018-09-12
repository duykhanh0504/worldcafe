package com.aseanfan.worldcafe.UI.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.NotificationCenter;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.UI.Adapter.FragmentMyPagerAdapter;
import com.aseanfan.worldcafe.UI.EditProfileActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.aseanfan.worldcafe.Helper.cropper.CropImage;
import com.aseanfan.worldcafe.Helper.cropper.CropImageView;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MypageFragment extends android.support.v4.app.Fragment implements NotificationCenter.NotificationCenterDelegate {

    private ViewPager viewPager;
    private ImageView avatar;
    private CardView background;
    private TextView name;
    private ProgressBar loading;
    private List<PostTimelineModel> posttimeline;
    private ImageView rankImage;
    private ImageView editImage;
    private Long accountid;
    private FragmentMyPagerAdapter adapter;
    private  UserModel user = new UserModel();
    private Button btn_follow;
    private int isfollow;

    private LinearLayout content_info;
    private LinearLayout content_toolbar;

    private TextView followed;
    private TextView follower;
    private Toolbar toolbar;

    private ImageView avatartoolbar;
    private TextView nametoolbar;

    private static final float PERCENTAGE_TO_SHOW  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE  = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private boolean mIsTheVisible          = true;
    private boolean mIsTheinVisible          = true;
    Uri selectedAvatar = null;
    private Activity activity;


    @Override
    public void onResume() {
        super.onResume();
        LoadListMyPost(accountid);
        LoadAccount(accountid);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void UpdateImage(Uri image, final int type)
    {

        if(image==null)
            return;

        String[] bb = Utils.compressFormat(image.getPath(), getActivity());
        String base64 = bb[0];
        String imagename = System.currentTimeMillis() + "." + bb[1];

        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id",AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("base64",base64);
        dataJson.addProperty("image",imagename);
        dataJson.addProperty("type","image/");

        String url = "";
        if(type ==1)
        {
            url =RestAPI.POST_UPDATECOVER;
        }
        else if(type == 0)
        {
            url =RestAPI.POST_UPDATEAVATAR;
        }

        RestAPI.PostDataMasterWithToken(getActivity(), dataJson, url, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    if(type==0) {
                        JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                        AccountController.getInstance().getAccount().setAvarta(jsons.get("url").getAsString());
                        DBHelper.getInstance(getActivity()).updatePerson(AccountController.getInstance().getAccount());
                    }

                    //  String email = _emailText.getText().toString();
                    //   String password = _passwordText.getText().toString();


                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });
    }

    public void LoadAccount(Long account)
    {
        String url =  String.format(RestAPI.GET_ACCOUNT_INFO,AccountController.getInstance().getAccount().getId(),account);

        RestAPI.GetDataMasterWithToken(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if (statuscode == RestAPI.STATUS_SUCCESS) {
                        JsonObject jsonObject = jsons.getAsJsonArray("result").get(0).getAsJsonObject();
                        Gson gson = new Gson();
                        user = gson.fromJson(jsonObject, UserModel.class);
                        name.setText(user.getUsername());
                        final Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
                        Glide.with(getContext()).load( user.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(avatar);
                        final Drawable mDefaultBackground1 = getContext().getResources().getDrawable(R.drawable.avata_defaul);
                        nametoolbar.setText(user.getUsername());
                        Glide.with(getContext()).load( user.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground1)).into(avatartoolbar);

                        if(user.getCover()!=null)
                        {
                            Glide.with(getContext()).load( user.getCover()).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    background.setBackgroundDrawable(resource);
                                }
                            });
                            if(accountid ==AccountController.getInstance().getAccount().getId()) {
                                AccountController.getInstance().getAccount().setCover(user.getCover());
                            }
                        }

                        followed.setText(getString(R.string.Following) + user.getFollowed());
                        follower.setText(getString(R.string.Followers) + user.getFollower());
                        adapter.setdata(user);

                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void UnFollow(Long followid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("follower_id",followid);
        loading.setVisibility(View.VISIBLE);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_UNFOLLOW, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if (statuscode == RestAPI.STATUS_SUCCESS) {
                        isfollow = Constants.FOLLOW;
                        btn_follow.setText("Follow");
                        user.setFollower(user.getFollower() -1);
                        follower.setText(getString(R.string.Followers) + user.getFollower());

                    }


                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
                finally {
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }



    public void Follow(Long followid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("follower_id",followid);
        loading.setVisibility(View.VISIBLE);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_FOLLOW, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if (statuscode == RestAPI.STATUS_SUCCESS) {
                        isfollow = Constants.UNFOLLOW;
                        btn_follow.setText("unFollow");
                        user.setFollower(user.getFollower() +1);
                        follower.setText(getString(R.string.Followers) + user.getFollower());

                    }



                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
                finally {
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    public void LoadListMyPost(Long account)
    {
        String url =  String.format(RestAPI.GET_LISTPOSTMYPAGE,AccountController.getInstance().getAccount().getId(),account,0);
        loading.setVisibility(View.VISIBLE);

        RestAPI.GetDataMasterWithToken(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<PostTimelineModel>>(){}.getType();
                    posttimeline = gson.fromJson(jsonArray, type);
                   // mAdapter.setPostList(posttimeline);
                    if(  viewPager.getCurrentItem() == FragmentMyPagerAdapter.MYPPOST_PAGE)
                    {
                        adapter.updateFragmentPost(posttimeline);
                    }

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
                finally {
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    public void CheckFollow(Long followid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("follower_id",followid);


        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_CHECK_FOLLOW, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if (statuscode == RestAPI.STATUS_SUCCESS) {
                        isfollow = jsons.get("result").getAsInt();
                    }
                    if(isfollow == Constants.FOLLOW)
                    {
                        btn_follow.setText("Follow");
                    }
                    else
                    {
                        btn_follow.setText("Unfollow");
                    }
                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        btn_follow =(Button)view.findViewById(R.id.btn_follow);
        avatar = view.findViewById(R.id.avatar);
        followed = (TextView) view.findViewById(R.id.txt_followed);
        follower =(TextView)view.findViewById(R.id.txt_follower);
        editImage= (ImageView) view.findViewById(R.id.image_edit);

        content_info = (LinearLayout) view.findViewById(R.id.content_info);
        content_toolbar = (LinearLayout) view.findViewById(R.id.content_toolbar);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        content_toolbar.startAnimation(alphaAnimation);

        avatartoolbar = view.findViewById(R.id.imageAvatar);
        nametoolbar = view.findViewById(R.id.txtusername);


        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(null);
      //  ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        name = view.findViewById(R.id.Name);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accountid.equals(AccountController.getInstance().getAccount().getId())) {
                    Intent intent = CropImage.activity(null)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAutoZoomEnabled(false)
                            .setMultiTouchEnabled(false)
                            .getIntent(getActivity());
                    intent.putExtra("type", 0);
                    getActivity().startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });


        if(getArguments()!=null) {
            accountid = getArguments().getLong("account_id");

        }
        else
        {
            accountid = AccountController.getInstance().getAccount().getId();
            name.setText(AccountController.getInstance().getAccount().getUsername());
            Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
            Glide.with(getContext()).load( AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(avatar);
            nametoolbar.setText(AccountController.getInstance().getAccount().getUsername());
            Drawable mDefaultBackground1 = getContext().getResources().getDrawable(R.drawable.avata_defaul);
            Glide.with(getContext()).load( AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground1)).into(avatartoolbar);

        }

        LoadAccount(accountid);

        if(!accountid.equals(AccountController.getInstance().getAccount().getId()))
        {

            btn_follow.setVisibility(View.VISIBLE);
            editImage.setVisibility(View.GONE);
            CheckFollow(accountid);
        }
        else
        {
            btn_follow.setVisibility(View.GONE);
            editImage.setVisibility(View.VISIBLE);
        }

        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isfollow == Constants.FOLLOW)
                {
                    Follow(accountid);
                }
                else
                {
                    UnFollow(accountid);
                }
            }
        });
        //visible image view
    //    avatar.getLayoutParams().height = 150;
      //  avatar.getLayoutParams().width = 150;
        avatar.setScaleType(ImageView.ScaleType.FIT_XY);
        rankImage= (ImageView) view.findViewById(R.id.image_rank);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accountid.equals(AccountController.getInstance().getAccount().getId())) {
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivity(intent);
                }
            }
        });


        background = view.findViewById(R.id.background);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accountid.equals(AccountController.getInstance().getAccount().getId())) {
                    Intent intent = CropImage.activity(null)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAutoZoomEnabled(false)
                            .setMultiTouchEnabled(false)
                            .setInitialCropWindowRectangle(new Rect(0,0,Utils.getwidthScreen(getContext()),Utils.convertDpToPixel(190,getContext())))
                            .getIntent(getActivity());
                    intent.putExtra("type", 1);
                    getActivity().startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });


        loading = view.findViewById(R.id.loading_spinner);

        Glide.with(getContext()).load( "https://png.pngtree.com/thumb_back/fh260/back_pic/00/15/30/4656e81f6dc57c5.jpg").into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                background.setBackgroundDrawable(resource);
            }
        });
        AppBarLayout appBarLayout = view.findViewById(R.id.appBar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                scrollRange = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) scrollRange;
                handleVisible(percentage);
                handleinVisible(percentage);
            }
        });

        rankImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
      //  ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      /*  CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        if(AccountController.getInstance().getAccount().getUsername()!=null) {
            collapsingToolbar.setTitle(AccountController.getInstance().getAccount().getUsername());
        }
        else
        {
            collapsingToolbar.setTitle(AccountController.getInstance().getAccount().getEmail());
        }*/

        viewPager = (ViewPager)view.findViewById(R.id.view_mypage);

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.mypagebackpress);

        adapter = new FragmentMyPagerAdapter(accountid, getActivity(),getChildFragmentManager(),user);

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_mypage);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position ==FragmentMyPagerAdapter.ALBUM_PAGE)
                {
                    adapter.updateFragmentAlbum(posttimeline);
                }
                if(position ==FragmentMyPagerAdapter.MYPPOST_PAGE)
                {
                    adapter.updateFragmentPost(posttimeline);
                }
            }

            @Override
            public void onPageSelected(int position) {
                int i=0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int i=0;
            }
        });

        return view;
    }
    public static void startAlphaAnimation (final View v, long duration, final int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void handleinVisible(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW) {

            if(!mIsTheinVisible) {

                startAlphaAnimation(content_toolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheinVisible = true;
            }

        } else {

            if (mIsTheinVisible) {

                startAlphaAnimation(content_toolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheinVisible = false;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if(result.getUri()!=null) {
                        if(data.getIntExtra("type",0) ==0) {
                            selectedAvatar = result.getUri();
                            UpdateImage(selectedAvatar, 0);
                            Glide.with(this).load( selectedAvatar).apply(RequestOptions.circleCropTransform()).into( avatar);
                        }
                        else if(data.getIntExtra("type",0) ==1) {
                            selectedAvatar = result.getUri();
                            UpdateImage(selectedAvatar, 1);
                            Glide.with(this).load( selectedAvatar).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    background.setBackgroundDrawable(resource);
                                }
                            });
                        }
                    }
                   // Glide.with(this).load( selectedAvatar).apply(RequestOptions.circleCropTransform()).into( _avatarimage);
                }
                break;
        }
    }

    private void handleVisible(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE) {

            if(!mIsTheVisible) {
                startAlphaAnimation(content_info, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);

                mIsTheVisible = true;
            }

        } else {

            if (mIsTheVisible) {
                startAlphaAnimation(content_info, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);

                mIsTheVisible = false;
            }
        }
    }



    @Override
    public void didReceivedNotification(int id, Object... args) {
        if(id == NotificationCenter.mypagebackpress)
        {
            if(accountid.equals(AccountController.getInstance().getAccount().getId()))
            {
                ((MainActivity)activity).BackKey();
            }
            else
            {
                ((MainActivity)activity).showSecondFragment();
            }
        }
    }
}
