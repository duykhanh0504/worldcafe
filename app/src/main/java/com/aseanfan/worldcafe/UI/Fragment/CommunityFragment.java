package com.aseanfan.worldcafe.UI.Fragment;

import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.NotificationCenter;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.IntroActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommunityFragment extends Fragment implements NotificationCenter.NotificationCenterDelegate {

  //  @BindView(R.id.tab_community)
    private  TabLayout tabcommunity;

  //  @BindView(R.id.intro_view_pager)
    private ViewFlipper viewpager;
    RecyclerView list_community;

    private CommunityAdapter mAdapter;

    private Spinner dropdown;

    private ImageButton btn_narrow;

    List<EventModel> listEventFriend;
    List<EventModel> listEventBusiness;
    List<EventModel> listEventLocal;
    List<EventModel> listEventLanguage;

    final int TAB_FRIEND = 0;
    final int TAB_BUSINESS=1;
    final int TAB_LOCAL=2;
    final int TAB_LANGUAGE=3;

    private SearchView searchView;


    private static final String[]paths = {"item 1", "item 2", "item 3"};

    public static CommunityFragment newInstance() {
        CommunityFragment firstFrag = new CommunityFragment();
        return firstFrag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //do when hidden
        } else {
            //do when show
        }
    }

    public void SearchEvent(String search_text)
    {
        int i =0;
    }

    public void LoadListEvent(final int Type)
    {
        String url =  String.format(RestAPI.GET_LISTEVENT,AccountController.getInstance().getAccount().getId(),Type,0);

        RestAPI.GetDataMaster(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result1");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<EventModel>>(){}.getType();
                    switch(Type)
                    {
                        case TAB_FRIEND:
                            listEventFriend = gson.fromJson(jsonArray, type);
                            if (tabcommunity.getSelectedTabPosition() == TAB_FRIEND) {
                                mAdapter.setEventList(listEventFriend);
                            }
                            break;
                        case TAB_BUSINESS:
                            listEventBusiness = gson.fromJson(jsonArray, type);
                            if (tabcommunity.getSelectedTabPosition() == TAB_BUSINESS) {
                                mAdapter.setEventList(listEventBusiness);
                            }
                            break;
                        case TAB_LOCAL:
                            listEventLocal = gson.fromJson(jsonArray, type);
                            if (tabcommunity.getSelectedTabPosition() == TAB_LOCAL) {
                                mAdapter.setEventList(listEventLocal);
                            }
                            break;
                        case TAB_LANGUAGE:
                            listEventLanguage = gson.fromJson(jsonArray, type);
                            if (tabcommunity.getSelectedTabPosition() == TAB_LANGUAGE) {
                                mAdapter.setEventList(listEventLanguage);
                            }
                            break;
                    }


                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_tool_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        listEventFriend = new ArrayList<>();
        listEventBusiness = new ArrayList<>();
        listEventLocal = new ArrayList<>();
        listEventLanguage = new ArrayList<>();

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.callbacksearch);


        LoadListEvent(TAB_FRIEND);

        viewpager = (ViewFlipper)view.findViewById(R.id.viewFlippercommunity);
        tabcommunity = (TabLayout)view.findViewById(R.id.tab_community);

        btn_narrow = (ImageButton)view.findViewById(R.id.btn_narrow);

        View tab1 = view.findViewById(R.id.community_tab1);

        mAdapter = new CommunityAdapter(null);
        list_community = (RecyclerView) tab1.findViewById(R.id.list_community);;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list_community.setLayoutManager(mLayoutManager);
        list_community.setItemAnimator(new DefaultItemAnimator());
        list_community.setAdapter(mAdapter);

        viewpager.setDisplayedChild(0);

        dropdown = (Spinner)view.findViewById(R.id.spinnersort);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(container.getContext(),
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_narrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
                builder.setTitle("Choose some animals");

// add a checkbox list
                String[] animals = {"horse", "cow", "camel", "sheep", "goat","horse", "cow", "camel", "sheep", "goat","horse", "cow", "camel", "sheep", "goat"};
                boolean[] checkedItems = {true, false, false, true, false,true, false, false, true, false,true, false, false, true, false};
                builder.setMultiChoiceItems(animals, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // user checked or unchecked a box
                    }
                });

// add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
                    }
                });
                builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mAdapter.setOnItemClickListener(new CommunityAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v,int Type) {
                if(Type == Constants.CLICK_EVENT) {
                    ((MainActivity)getActivity()).callDetailEvent(position);
                }
            }
        });

        tabcommunity.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                  @Override
                                                  public void onTabSelected(TabLayout.Tab tab) {
                                                      switch(tab.getPosition()) {
                                                          case TAB_FRIEND:
                                                              mAdapter.setEventList(listEventFriend);
                                                              LoadListEvent(TAB_FRIEND);
                                                              break;
                                                          case TAB_BUSINESS:
                                                              mAdapter.setEventList(listEventBusiness);
                                                              LoadListEvent(TAB_BUSINESS);
                                                              break;
                                                          case TAB_LOCAL:
                                                              mAdapter.setEventList(listEventLocal);
                                                              LoadListEvent(TAB_LOCAL);
                                                              break;
                                                          case TAB_LANGUAGE:
                                                              mAdapter.setEventList(listEventLanguage);
                                                              LoadListEvent(TAB_LANGUAGE);
                                                              break;

                                                      }
                                                  }

                                                  @Override
                                                  public void onTabUnselected(TabLayout.Tab tab) {

                                                  }

                                                  @Override
                                                  public void onTabReselected(TabLayout.Tab tab) {

                                                  }
                                              });

                //  ButterKnife.bind(this, view);

      /*  viewpager.setAdapter(new CommunityFragment.PageCommunityAdapter());
        viewpager.setPageMargin(0);
        viewpager.setOffscreenPageLimit(1);*/

     /*   tabcommunity.setupWithViewPager(viewpager);

        for (int i = 0; i < 4; i++) {
            TabLayout.Tab tab = tabcommunity.getTabAt(i);
            View tabView = ((ViewGroup) tabcommunity.getChildAt(0)).getChildAt(i);
            tabView.requestLayout();
            tab.setText(i + " tab ");
        }*/

        return view;
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if(id == NotificationCenter.callbacksearch)
        {
            String i = (String)args[0];
        }

    }

   /* private class PageCommunityAdapter extends PagerAdapter {

        private CommunityAdapter mAdapter;

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(container.getContext(), R.layout.community_view_layout, null);
            RecyclerView list_community = (RecyclerView) view.findViewById(R.id.list_community);
            container.addView(view, 0);

             List<EventModel> eventList = new ArrayList<>();

            mAdapter = new CommunityAdapter(eventList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
            list_community.setLayoutManager(mLayoutManager);
            list_community.setItemAnimator(new DefaultItemAnimator());
            list_community.setAdapter(mAdapter);


            if (position == 0) {

            } else if (position == 1){

            } else if (position == 2){

            } else {

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
    }*/

}
