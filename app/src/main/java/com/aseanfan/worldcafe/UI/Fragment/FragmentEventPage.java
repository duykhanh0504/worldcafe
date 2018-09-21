package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.UI.Adapter.CommunityAdapter;
import com.aseanfan.worldcafe.UI.Adapter.FragmentEventPageAdapter;
import com.aseanfan.worldcafe.UI.CommentEventActivity;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentEventPage extends android.support.v4.app.Fragment {

    private RecyclerView list_community;

    private CommunityAdapter mAdapter;
    private List<EventModel> mlistevent ;
    private boolean isloading = false;

    public FragmentEventPage() {
        mlistevent = new ArrayList<>();
    }


    public void LoadListEvent(Context context, final int Type , List<Integer> area , String keyword , int typeSort , final int pos )
    {

        String url =  String.format(RestAPI.GET_LISTEVENT,AccountController.getInstance().getAccount().getId(),Type,pos);

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


        url = url + "&sort_type=" + typeSort;



        RestAPI.GetDataMasterWithToken(context,url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);
                        return;
                    }
                    if(RestAPI.checkExpiredtoken(s))
                    {
                        ViewDialog dialog = new ViewDialog();
                        dialog.showDialogOK(getActivity(), "invalid token", new ViewDialog.DialogListenner() {
                            @Override
                            public void OnClickConfirm() {
                                App.mApp.Logout();
                            }
                        });

                        return;
                    }
                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<EventModel>>(){}.getType();
                    if(pos == 0) {
                        mlistevent = gson.fromJson(jsonArray, type);
                        mAdapter.setData(mlistevent);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                isloading = false;
                            }
                        }, 3000);
                    }
                    else
                    {
                        List<EventModel> temp  = new ArrayList<>();
                        temp = gson.fromJson(jsonArray, type);
                        if(temp.size()>0) {
                            mlistevent.addAll(temp);
                            mAdapter.setData(mlistevent);
                        }
                        else
                        {
                           //pos--;
                            clickListener.updatePos();
                        }
                    }


                   // java.lang.reflect.Type type = new TypeToken<List<EventModel>>(){}.getType();
                   // mlistevent = gson.fromJson(jsonArray, type);


                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void setData(List<EventModel> data)
    {
        mlistevent =data;
        mAdapter.setData(mlistevent);
    }

    private static FragmentEventPage.ClickListener clickListener;

    public void setOnItemClickListener(FragmentEventPage.ClickListener clickListener) {
        FragmentEventPage.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(List<EventModel> list);
        void loadmore(List<EventModel> list);
        void updatePos();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_view_layout, container, false);
        Bundle bundle = this.getArguments();
        mlistevent = new ArrayList<>();
        mAdapter = new CommunityAdapter(mlistevent);
        list_community = (RecyclerView) view.findViewById(R.id.list_community);;
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list_community.setLayoutManager(mLayoutManager);
        list_community.setItemAnimator(new DefaultItemAnimator());
        list_community.setAdapter(mAdapter);

        list_community.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount =  mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();;
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if(!recyclerView.canScrollVertically(1)) {
                        //   Toast.makeText(getContext(), "LAst", Toast.LENGTH_LONG).show();
                      //  loadmore();
                        isloading = true;
                      //  LoadListEvent( mcontext,  type ,  area ,  keyword ,  typeSort );
                        clickListener.loadmore(mlistevent);
                    }
                }

                if (((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                    clickListener.onItemClick(mlistevent);
                    if(isloading==false) {
                        //  Toast.makeText(getContext(), "Top", Toast.LENGTH_LONG).show();
                        clickListener.onItemClick(mlistevent);
                        isloading = true;
                    }

                }


            }
        });

        mAdapter.setOnItemClickListener(new CommunityAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v,int Type,List<EventModel> event, int pos) {
                if(Type == Constants.CLICK_EVENT) {
                    ((MainActivity)getActivity()).callDetailEvent(event.get(pos));
                }
                if(Type == Constants.CLICK_IMAGE_LIKE) {
                   // ((MainActivity)getActivity()).callDetailEvent(event);
                   // LikeEvent(event , pos);
                }
                if(Type == Constants.CLICK_IMAGE_COMMENT) {
                   // ((MainActivity)getActivity()).callDetailEvent(event);
                    Intent intent = new Intent(getActivity(), CommentEventActivity.class);
                    intent.putExtra("Event_id",event.get(pos).getEventid());
                    startActivity(intent);
                  //  ((MainActivity)getActivity()).callDetailEvent(event);
                }
            }
        });

        return view;
    }


}
