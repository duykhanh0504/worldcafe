package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.NotificationCenter;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.UI.Adapter.PostTimelineAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerEventAdapter;
import com.aseanfan.worldcafe.UI.CommentActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.UI.PostTimeLineActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimelineFragment extends android.support.v4.app.Fragment implements PostTimelineAdapter.ClickListener{

    private static final String[]paths = {"Timeline","Question", "Follow"};

    private RecyclerView list_post;

    private PostTimelineAdapter mAdapter;

    private List<PostTimelineModel> timeline;

    private LinearLayout posttimeline;

    private boolean isloading = false;

    private ProgressBar loading;

    private Spinner dropdown;


    private ImageView imageAvatar;

    private int current_pos;

    private int type;
    private List<Integer> area = new ArrayList<>();

    private String keyword = "";

    private SearchView searchView;

    private AdView adView;

    String[] listcity = {"HCM", "Ha Noi", "Da Nang", "Tokyo", "Osaka"};
    int[] listidcity = {1, 2, 3, 4, 5};
    boolean[] checkedItems = {false, false, false, false, false};

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_tool_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // searchView expanded
                } else {
                    keyword = "";
                    LoadListTimeLinePost();
                    // searchView not expanded
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                keyword = s;
                LoadListTimeLinePost();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                NotificationCenter.getInstance().postNotificationName(NotificationCenter.callbacksearch,s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.buttonarea) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose some areas");

            builder.setCancelable(true);
            builder.setMultiChoiceItems(listcity, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if (isChecked) {
                        area.add(listidcity[which]);
                    } else if (area.contains(listidcity[which])) {
                        area.remove(area.indexOf(listidcity[which]));
                    }
                }
            });


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LoadListTimeLinePost();
                }
            });
            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    public void LoadListTimeLinePost()
    {
        String url="";

        if(type ==2) {

            url = String.format(RestAPI.GET_LISTTIMELINEFOLLOWED, AccountController.getInstance().getAccount().getId(), current_pos);

        }
        else
        {
            url = String.format(RestAPI.GET_LISTPOSTTIMELINE, AccountController.getInstance().getAccount().getId(), current_pos,type);
        }
        if(area.size()>0)
        {
            String city = "&city=";
            for( int i = 0 ; i <area.size() ; i++)
            {
                if(i == area.size()-1)
                    city = city + area.get(i) ;
                else
                   city = city + area.get(i) + ",";
            }
            url = url + city;
        }
        if(!keyword.isEmpty())
        {
            url = url + "&keyword=" + keyword;
        }

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
                    if(current_pos == 0) {
                        timeline = gson.fromJson(jsonArray, type);
                        mAdapter.setPostList(timeline);
                    }
                    else
                    {
                        List<PostTimelineModel> temp  = new ArrayList<>();
                        temp = gson.fromJson(jsonArray, type);
                        if(temp.size()>0) {
                            timeline.addAll(temp);
                            mAdapter.setPostList(timeline);
                        }
                        else
                        {
                            current_pos--;
                        }
                    }

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isloading = false;
                        }
                    }, 3000);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isloading = false;
                        }
                    }, 3000);

                }
                finally {
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    public void loadmore()
    {
        current_pos++;
        LoadListTimeLinePost();
    }

    public void LikePost(Long Postid)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("newfeed_id",Postid);

        RestAPI.PostDataMasterWithToken(getActivity().getApplicationContext(),dataJson,RestAPI.POST_LIKEPOST, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public static TimelineFragment newInstance() {
        TimelineFragment firstFrag = new TimelineFragment();
        return firstFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        timeline = new ArrayList<>();

        current_pos = 0;

        mAdapter = new PostTimelineAdapter(null,false);
        list_post = (RecyclerView) view.findViewById(R.id.listtimeline);
        posttimeline = (LinearLayout) view.findViewById(R.id.posttimeline);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list_post.setLayoutManager(mLayoutManager);
        list_post.setItemAnimator(new DefaultItemAnimator());
        list_post.setAdapter(mAdapter);
        loading = (ProgressBar)view.findViewById(R.id.loading_spinner);
        imageAvatar = (ImageView)view.findViewById(R.id.imageAvatar);

        adView = (AdView) view.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        Drawable mDefaultBackground = getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(getContext()).load(AccountController.getInstance().getAccount().getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground)).into(imageAvatar);


        dropdown = (Spinner)view.findViewById(R.id.spinnersort);
    //    ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(),
               // android.R.layout.simple_spinner_item,paths);

      //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerEventAdapter adapter = new SpinnerEventAdapter(getActivity(),paths);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = i;
                LoadListTimeLinePost();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mAdapter.setOnItemClickListener(this);
        posttimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PostTimeLineActivity.class);
                startActivity(intent);
            }
        });

        list_post.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();;
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if(!recyclerView.canScrollVertically(1)) {
                     //   Toast.makeText(getContext(), "LAst", Toast.LENGTH_LONG).show();
                        loadmore();
                        isloading = true;
                    }
                }

                if (((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                    if(isloading==false) {
                      //  Toast.makeText(getContext(), "Top", Toast.LENGTH_LONG).show();
                        current_pos = 0;
                        LoadListTimeLinePost();
                        isloading = true;
                    }

                }


            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position, View v, int type) {
        if(type == Constants.CLICK_IMAGE_LIKE)
        {
            if(timeline.get(position).getIslike() == 0)
            {
                timeline.get(position).setIslike(1);
                timeline.get(position).setNumberLike(timeline.get(position).getNumberLike() + 1);
            }
            else
            {

                timeline.get(position).setIslike(0);
                timeline.get(position).setNumberLike(timeline.get(position).getNumberLike() -1);

            }
            LikePost( timeline.get(position).getTimelineid());
            mAdapter.setPostList(timeline);

        }
        if(type == Constants.CLICK_AVATAR) {

            if(timeline.get(position).getAccountid().compareTo(AccountController.getInstance().getAccount().getId()) != 0)
            {
                ((MainActivity) getActivity()).callFriendPage(timeline.get(position).getAccountid());

            }
            else
            {
                ((MainActivity) getActivity()).gotoMypage();
            }

        }
        if(type == Constants.CLICK_IMAGE_COMMENT) {
            Intent intent = new Intent(getContext(), CommentActivity.class);
            intent.putExtra("Timeline_id", timeline.get(position).getTimelineid());
            intent.putExtra("Account_id", timeline.get(position).getAccountid());
            startActivity(intent);

        }
        if(type == Constants.CLICK_TIMELINE)
        {
            if(timeline.get(position)!=null) {
                ((MainActivity) getActivity()).callDetailTimeline(timeline.get(position));
            }
        }
    }
}
