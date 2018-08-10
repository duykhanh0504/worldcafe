package com.aseanfan.worldcafe.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SyncDataService {


    // List<Message>
    public static List<ChatMessageModel> listmes;


    public static void listmessage(final Long account_id, final Context mContext) {
        Long offset = DBHelper.getInstance(mContext).getlastMessageChat(account_id);
        String url = String.format(RestAPI.GET_MESSAGECHAT, account_id, offset);
        listmes = new ArrayList<>();
        RestAPI.GetDataMaster(mContext, url, new RestAPI.RestAPIListenner() {
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
                        JsonArray jsonArray = jsons.getAsJsonArray("result");
                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<List<ChatMessageModel>>() {
                        }.getType();
                        listmes = gson.fromJson(jsonArray, type);
                        for (ChatMessageModel temp : listmes) {
                            DBHelper.getInstance(mContext).InsertMessageChat(temp);
                        }
                    }
                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });
    };



}
