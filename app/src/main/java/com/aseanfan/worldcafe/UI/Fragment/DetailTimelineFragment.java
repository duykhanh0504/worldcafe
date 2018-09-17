package com.aseanfan.worldcafe.UI.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.UI.Adapter.PostImageAdapter;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.UI.EditPostTimeline;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DetailTimelineFragment  extends android.support.v4.app.Fragment implements View.OnClickListener {


    private ImageView avatar;
    private TextView username;
    private PostImageAdapter mAdapter;
    private FrameLayout imagePost;
    private TextView like;
    private TextView comment;
    private TextView detail;
    private ImageView imagelike;
    private ImageButton toolbar_button;

    private RecyclerView rcycoment;

    private CommentAdapter commentAdapter;

    private List<CommentModel> listcomment;
    PostTimelineModel timeline;

    public void ListComment(Long timelineid)
    {
        String url;
        url = String.format(RestAPI.GET_COMMENT, timelineid);

        RestAPI.GetDataMasterWithToken(getActivity(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<CommentModel>>(){}.getType();
                    listcomment = gson.fromJson(jsonArray, type);
                    commentAdapter.setCommentList(listcomment);
                    rcycoment.smoothScrollToPosition(listcomment.size());

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void initview(View view )
    {
        avatar = (ImageView)view.findViewById(R.id.imageAvatar);
        username = (TextView)view.findViewById(R.id.namePost);
        imagePost = (FrameLayout) view.findViewById(R.id.list_image);
        like = (TextView) view.findViewById(R.id.textLike);
        comment = (TextView) view.findViewById(R.id.textComment);
        detail = (TextView) view.findViewById(R.id.detailPost);
        imagelike = (ImageView) view.findViewById(R.id.imageLike) ;
        toolbar_button = (ImageButton) view.findViewById(R.id.toolbar_button) ;

        toolbar_button.setOnClickListener(this);

        mAdapter = new PostImageAdapter(null);

        rcycoment = (RecyclerView)view.findViewById(R.id.listComment);

        // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(),3);
      /*  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        imagePost.setLayoutManager(mLayoutManager);
        imagePost.setItemAnimator(new DefaultItemAnimator());
        imagePost.setAdapter(mAdapter);*/

    }

    public void initdata()
    {
        if (getArguments() != null) {
             timeline = new PostTimelineModel();
            timeline.setUrlImage((List<String>)getArguments().getStringArrayList("listimage"));
            timeline.setUsername(getArguments().getString("username"));
            timeline.setNumberLike(getArguments().getInt("numberlike"));
            timeline.setNumberComment(getArguments().getInt("numbercomment"));
            timeline.setTimelineid(getArguments().getLong("timelineid"));
            timeline.setDetail(getArguments().getString("detail"));
            timeline.setIslike(getArguments().getInt("islike"));
            timeline.setAccount_id(getArguments().getLong("accountid"));

            Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
            Glide.with(getContext()).load(timeline.getUrlAvatar()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(avatar);
            username.setText(timeline.getUsername());
            detail.setText(timeline.getDetail());

           // mAdapter.setData(timeline.getUrlImage());
            if(timeline.getUrlImage()!=null && timeline.getUrlImage().size() > 0) {
                imagePost.setVisibility(View.VISIBLE);
                UpdateLayoutImage(imagePost,timeline.getUrlImage());
            }
            else
            {
                imagePost.setVisibility(View.GONE);
            }

            like.setText(String.valueOf(timeline.getNumberLike()));
            comment.setText(String.valueOf(timeline.getNumberComment()));

            if(timeline.getIslike() == 0)
            {
                imagelike.setBackgroundResource(R.drawable.unlike);
            }
            else
            {
                imagelike.setBackgroundResource(R.drawable.like);
            }


            commentAdapter = new CommentAdapter(null);


            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rcycoment.setLayoutManager(mLayoutManager);
            rcycoment.setItemAnimator(new DefaultItemAnimator());
            rcycoment.setAdapter(commentAdapter);

            ListComment(timeline.getTimelineid());


        }
    }

    void UpdateLayoutImage(FrameLayout contain ,List<String> url)
    {
        contain.removeAllViews();
        if(url.size()==1) {
            ImageView image = new ImageView(contain.getContext());
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(0)).into(image);
            contain.addView(image);

        }
        else if (url.size() ==2)
        {
            LinearLayout contentimage = new LinearLayout(contain.getContext());
            contentimage.setOrientation(LinearLayout.HORIZONTAL);

            ImageView image = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((Utils.getwidthScreen(contain.getContext())/2) - Utils.convertDpToPixel(1,contain.getContext()), Utils.convertDpToPixel(240,contain.getContext()));
            image.setLayoutParams(layoutParams);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(0)).into(image);

            FrameLayout line = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline = new LinearLayout.LayoutParams( Utils.convertDpToPixel(2,contain.getContext()), Utils.convertDpToPixel(240,contain.getContext()));
            line.setLayoutParams(layoutParamsline);
            line.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            ImageView image1 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams((Utils.getwidthScreen(contain.getContext())/2 - Utils.convertDpToPixel(1,contain.getContext())), Utils.convertDpToPixel(240,contain.getContext()));
            image1.setLayoutParams(layoutParams1);
            image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(1)).into(image1);

            contentimage.addView(image);
            contentimage.addView(line);
            contentimage.addView(image1);
            contain.addView(contentimage);


        }
        else if (url.size() ==3)
        {
            LinearLayout contentimage = new LinearLayout(contain.getContext());
            contentimage.setOrientation(LinearLayout.HORIZONTAL);

            ImageView image = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(240,contain.getContext()));
            image.setLayoutParams(layoutParams);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(0)).into(image);

            LinearLayout contentimage1 = new LinearLayout(contain.getContext());
            contentimage1.setOrientation(LinearLayout.VERTICAL);
            ImageView image1 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(119,contain.getContext()));
            layoutParams.setMargins( 0,0,0,Utils.convertDpToPixel(5,contain.getContext()));
            image1.setLayoutParams(layoutParams1);
            image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(1)).into(image1);

            ImageView image2 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(120,contain.getContext()));
            image2.setLayoutParams(layoutParams2);
            image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(2)).into(image2);

            FrameLayout line = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline = new LinearLayout.LayoutParams( Utils.convertDpToPixel(2,contain.getContext()), Utils.convertDpToPixel(240,contain.getContext()));
            line.setLayoutParams(layoutParamsline);
            line.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            FrameLayout line1 = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline1 = new LinearLayout.LayoutParams( Utils.convertDpToPixel(Utils.convertDpToPixel(120,contain.getContext()),contain.getContext()), Utils.convertDpToPixel(2,contain.getContext()));
            line1.setLayoutParams(layoutParamsline1);
            line1.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            contentimage1.addView(image1);
            contentimage1.addView(line1);
            contentimage1.addView(image2);

            contentimage.addView(image);
            contentimage.addView(line);
            contentimage.addView(contentimage1);
            contain.addView(contentimage);


        }
        else if (url.size() >=4)
        {
            LinearLayout contentimage = new LinearLayout(contain.getContext());
            contentimage.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout contentimage1 = new LinearLayout(contain.getContext());
            contentimage1.setOrientation(LinearLayout.VERTICAL);

            ImageView image = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(119,contain.getContext()));
            image.setLayoutParams(layoutParams);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(0)).into(image);

            ImageView image1 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(120,contain.getContext()));
            image1.setLayoutParams(layoutParams1);
            image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(1)).into(image1);

            LinearLayout contentimage2 = new LinearLayout(contain.getContext());
            contentimage2.setOrientation(LinearLayout.VERTICAL);

            ImageView image2 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(119,contain.getContext()));
            image2.setLayoutParams(layoutParams2);
            image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(2)).into(image2);

            ImageView image3 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(120,contain.getContext()));
            image3.setLayoutParams(layoutParams2);
            image3.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(3)).into(image3);

            FrameLayout line = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline = new LinearLayout.LayoutParams( Utils.convertDpToPixel(2,contain.getContext()), Utils.convertDpToPixel(240,contain.getContext()));
            line.setLayoutParams(layoutParamsline);
            line.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            FrameLayout line1 = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline1 = new LinearLayout.LayoutParams( Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(2,contain.getContext()));
            line1.setLayoutParams(layoutParamsline1);
            line1.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            FrameLayout line2 = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline2= new LinearLayout.LayoutParams( Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(2,contain.getContext()));
            line2.setLayoutParams(layoutParamsline2);
            line2.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            contentimage1.addView(image);
            contentimage1.addView(line1);
            contentimage1.addView(image1);

            contentimage2.addView(image2);
            contentimage2.addView(line2);
            contentimage2.addView(image3);

            contentimage.addView(contentimage1);
            contentimage.addView(line);
            contentimage.addView(contentimage2);
            contain.addView(contentimage);


        }
    }

    public void LoadReport()
    {
        String url;
        url = String.format(RestAPI.GET_LIST_REPORT, 0);

        RestAPI.GetDataMaster(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<String>>(){}.getType();
                    List<String> listEvent = gson.fromJson(jsonArray, type);
                    dialogReport(listEvent);


                }
                catch (Exception ex) {
                }
            }
        });
    }


    public void Report(String reporttext)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("type", 2);
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("object_id", timeline.getAccountid());
        dataJson.addProperty("content", reporttext);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_REPORT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    ViewDialog dialog = new ViewDialog();
                    dialog.showDialogCancel(getActivity(),"Report Successful");
                    //   posttimeline.remove(pos);
                    //  Adapter.setPostList(posttimeline);

                }
                catch (Exception ex) {
                }
            }
        });
    }

    public void DeletePost()
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("newfeed_id", timeline.getTimelineid());
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_DELETE_TIMELINE, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    ((MainActivity) getActivity()).showThirdFragment();
                 //   posttimeline.remove(pos);
                  //  Adapter.setPostList(posttimeline);

                }
                catch (Exception ex) {
                }
            }
        });
    }

    public void EditPost(PostTimelineModel post)
    {
        Intent intent = new Intent(getActivity(), EditPostTimeline.class);
        intent.putExtra("timelineid",post.getTimelineid());
        intent.putExtra("detail",post.getDetail());
        ArrayList<String> listOfStrings = new ArrayList<>(post.getUrlImage().size());
        listOfStrings.addAll(post.getUrlImage());
        intent.putStringArrayListExtra("listimage",listOfStrings);
        intent.putExtra("type",post.getType());
        intent.putExtra("genre",post.getGenre());

        startActivity(intent);
    }

    public void openOptionMenu(View v){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.menu_item_timline, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item_delete:
                        DeletePost();
                        break;
                    case R.id.item_edit:
                        EditPost( timeline);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public void dialogReport(final List<String> list)
    {
        final String[] spinner_item = new String[1];

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report);
        dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
        final EditText edittext = (EditText) dialog.findViewById(R.id.editText1);
        Button button = (Button) dialog.findViewById(R.id.report);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                spinner_item[0] = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                Report(spinner_item[0]);

             //   Toast.makeText(getApplicationContext(), spinner_item + " - " + edittext.getText().toString().trim(), Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_timeline_fragment, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_toolbar_post);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        initview(view);

        initdata();


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_button: {
                if(timeline.getAccountid().equals(AccountController.getInstance().getAccount().getId())) {
                    openOptionMenu(toolbar_button);
                }
                else
                {
                    LoadReport();
                }
                break;
            }

        }
    }
}
