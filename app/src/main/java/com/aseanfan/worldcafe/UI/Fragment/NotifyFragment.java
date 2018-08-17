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
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    NotificationModel notify = new NotificationModel();
                    notify.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.NOTIFY_TITLE)));
                    notify.setMessage(cursor.getString(cursor.getColumnIndex(DBHelper.NOTIFY_MESSAGE)));
                    notify.setType(cursor.getInt(cursor.getColumnIndex(DBHelper.NOTIFY_TYPE)));
                    notify.setAvarta(cursor.getString(cursor.getColumnIndex(DBHelper.NOTIFY_AVATAR)));
                    list.add(notify);
                    cursor.moveToNext();
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


    public void LoadListTimeLinePost()
    {
        if(listnotify==null)
        {
            listnotify = new ArrayList<>();
        }
        listnotify.clear();
        String url = String.format(RestAPI.GET_HISTORYPUSH, AccountController.getInstance().getAccount().getId(), 0);


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
                        NotificationModel nofity = new NotificationModel();
                        nofity.setType(jsonArray.get(i).getAsJsonObject().get("typepush").getAsInt());
                        nofity.setCreatetime(jsonArray.get(i).getAsJsonObject().get("create_time").getAsString());
                       // JsonObject jsons = (new JsonParser()).parse(jsonArray.get(i).getAsJsonObject().get("data").getAsString()).getAsJsonObject();
                        nofity.setAvarta(jsonArray.get(0).getAsJsonObject().get("avarta").toString());
                        nofity.setTitle(jsonArray.get(i).getAsJsonObject().get("username").getAsString());
                        nofity.setMessage(jsonArray.get(i).getAsJsonObject().get("username").getAsString());
                        listnotify.add(nofity);
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

      //  new ListHistoryNotifyAsync().execute();
        LoadListTimeLinePost();

        ((MainActivity) getActivity()).checkbadge();

        return view;
    }
}
