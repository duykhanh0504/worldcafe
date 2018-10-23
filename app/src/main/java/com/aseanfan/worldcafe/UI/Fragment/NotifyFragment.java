package com.aseanfan.worldcafe.UI.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.NotificationModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Adapter.ChatMessageAdapter;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.UI.Adapter.NotifyAdapter;
import com.aseanfan.worldcafe.UI.ChatActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotifyFragment extends android.support.v4.app.Fragment {

    private RecyclerView rcynotify;

    private NotifyAdapter mAdapter;

    private List<NotificationModel> listnotify;

    public static NotifyFragment newInstance() {
        NotifyFragment firstFrag = new NotifyFragment();
        return firstFrag;
    }

    private class ListHistoryNotifyAsync extends AsyncTask<Long, Long, List<NotificationModel>> {

        @Override
        protected List<NotificationModel> doInBackground(Long... ints) {
            List<NotificationModel> list = new ArrayList<>();

            Cursor cursor = DBHelper.getInstance(getActivity()).getNotify();
            if (cursor != null) {
                cursor.moveToLast();
                while (cursor.isBeforeFirst() == false) {
                    NotificationModel notify = new NotificationModel();
                    notify.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.NOTIFY_TITLE)));
                    notify.setMessage(cursor.getString(cursor.getColumnIndex(DBHelper.NOTIFY_MESSAGE)));
                    notify.setType(cursor.getInt(cursor.getColumnIndex(DBHelper.NOTIFY_TYPE)));
                    notify.setAvarta(cursor.getString(cursor.getColumnIndex(DBHelper.NOTIFY_AVATAR)));
                    list.add(notify);
                    cursor.moveToPrevious();
                }
            }
            //   SystemClock.sleep(1000);
            return list;
        }

        @Override
        protected void onPostExecute(List<NotificationModel> param) {
            if(listnotify==null)
            {
                listnotify = new ArrayList<>();
            }
            listnotify.clear();
            listnotify =param;
            mAdapter.setNotifyList(listnotify);

        }

    }


    public void LoadListHistoryNotify()
    {
        if(listnotify==null)
        {
            listnotify = new ArrayList<>();
        }
        listnotify.clear();
        String url = String.format(RestAPI.GET_HISTORYPUSH, AccountController.getInstance().getAccount().getId(), 0,0);


        RestAPI.GetDataMasterWithToken(getActivity().getApplicationContext(),url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result");
                   // Gson gson = new Gson();
                 //   java.lang.reflect.Type type = new TypeToken<List<PostTimelineModel>>(){}.getType();
                    for (int i = 0; i < jsonArray.size(); i++)
                    {
                        NotificationModel notify = new NotificationModel();
                        notify.setType(jsonArray.get(i).getAsJsonObject().get("typepush").getAsInt());
                        notify.setCreatetime(jsonArray.get(i).getAsJsonObject().get("create_time").getAsString());
                        // JsonObject jsons = (new JsonParser()).parse(jsonArray.get(i).getAsJsonObject().get("data").getAsString()).getAsJsonObject();
                        if(!jsonArray.get(i).getAsJsonObject().get("avarta").isJsonNull()) {
                            notify.setAvarta(jsonArray.get(i).getAsJsonObject().get("avarta").getAsString());
                        }
                        if(!jsonArray.get(i).getAsJsonObject().get("username").isJsonNull()) {
                            notify.setTitle(jsonArray.get(i).getAsJsonObject().get("username").getAsString());
                        }
                        if(!jsonArray.get(i).getAsJsonObject().get("time_diff").isJsonNull()) {
                            notify.setCreatetime(jsonArray.get(i).getAsJsonObject().get("time_diff").getAsString());
                        }

                        notify.setNotifyid(jsonArray.get(i).getAsJsonObject().get("id").getAsInt());
                        JsonObject json = (new JsonParser()).parse(jsonArray.get(i).getAsJsonObject().get("data").getAsString()).getAsJsonObject();
                     //   notify.setAvarta(json.get("data").getAsJsonObject().get("avarta").getAsString());
                        notify.setMessage(json.get("data").getAsJsonObject().get("message").getAsString());
                        if(notify.getType() == 1|| notify.getType() == 2||
                                notify.getType() == 3|| notify.getType() == 4||notify.getType() == 5||
                                notify.getType() == 6||notify.getType() == 7||notify.getType() == 9||notify.getType() == 10)
                        {
                            notify.setObjectId(json.get("data").getAsJsonObject().get("objectId").getAsLong());
                        }
                        listnotify.add(notify);
                    }

                    mAdapter.setNotifyList(listnotify);

                }
                catch (Exception ex) {

                    ex.printStackTrace();

                }
                finally {
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);

        rcynotify = (RecyclerView)view.findViewById(R.id.listNotify);

        mAdapter = new NotifyAdapter(null);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false);

        //  mLayoutManager.setStackFromEnd(true);
        rcynotify.setLayoutManager(mLayoutManager);
        rcynotify.setItemAnimator(new DefaultItemAnimator());
        rcynotify.setAdapter(mAdapter);

       // ListHistoryNotifyAsync().execute();
       LoadListHistoryNotify();

        ((MainActivity) getActivity()).checkbadge();

        mAdapter.setOnItemClickListener(new NotifyAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(listnotify.get(position).getType() ==1
                        ||listnotify.get(position).getType() ==2 ||
                listnotify.get(position).getType() ==3)
                {
                    ((MainActivity) getActivity()).callDetailTimelineID(listnotify.get(position).getObjectId());
                }
                else if (listnotify.get(position).getType() ==4
                        ||listnotify.get(position).getType() ==5 ||
                        listnotify.get(position).getType() ==6
                        ||listnotify.get(position).getType() ==7
                        ||listnotify.get(position).getType() ==9)
                {
                    ((MainActivity) getActivity()).callDetailEventID(listnotify.get(position).getObjectId());
                }
                else if( listnotify.get(position).getType() ==10)
                {
                    ((MainActivity) getActivity()).callFriendPage(listnotify.get(position).getObjectId());
                }
                else if ( listnotify.get(position).getType() ==8 )
                {

                }

            }
        });

        return view;
    }
}
