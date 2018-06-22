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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.IntroActivity;
import com.aseanfan.worldcafe.worldcafe.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommunityFragment extends Fragment {

  //  @BindView(R.id.tab_community)
    private  TabLayout tabcommunity;

  //  @BindView(R.id.intro_view_pager)
    private ViewFlipper viewpager;

    private CommunityAdapter mAdapter;

    private Spinner dropdown;

    private ImageButton btn_narrow;

    private static final String[]paths = {"item 1", "item 2", "item 3"};

    public static CommunityFragment newInstance() {
        CommunityFragment firstFrag = new CommunityFragment();
        return firstFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        viewpager = (ViewFlipper)view.findViewById(R.id.viewFlippercommunity);
        tabcommunity = (TabLayout)view.findViewById(R.id.tab_community);

        btn_narrow = (ImageButton)view.findViewById(R.id.btn_narrow);

        View tab1 = view.findViewById(R.id.community_tab1);

        final List<EventModel> eventList = new ArrayList<>();

        mAdapter = new CommunityAdapter(eventList);
        final RecyclerView list_community = (RecyclerView) tab1.findViewById(R.id.list_community);;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(container.getContext(),2);
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
                int a = Type;
            }
        });

        tabcommunity.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                  @Override
                                                  public void onTabSelected(TabLayout.Tab tab) {
                                                      EventModel e;
                                                      switch(tab.getPosition()) {
                                                          case 0:
                                                              e = new EventModel();
                                                              e.setTitle("sss");
                                                              eventList.add(e);
                                                              mAdapter.setEventList(eventList);
                                                              mAdapter.notifyDataSetChanged();
                                                          case 1:

                                                              e = new EventModel();
                                                              e.setTitle("dsdd");
                                                              eventList.add(e);
                                                              mAdapter.setEventList(eventList);
                                                          case 2:
                                                              e = new EventModel();
                                                              e.setTitle("dsd");
                                                              eventList.add(e);
                                                              mAdapter.setEventList(eventList);
                                                          case 3:
                                                               e = new EventModel();
                                                              e.setTitle("ddftrtrd");
                                                              eventList.add(e);
                                                              mAdapter.setEventList(eventList);

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
