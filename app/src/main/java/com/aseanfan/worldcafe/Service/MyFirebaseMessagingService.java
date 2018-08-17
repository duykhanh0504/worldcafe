package com.aseanfan.worldcafe.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.NotificationCenter;
import com.aseanfan.worldcafe.Model.NotificationModel;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String FCM_PARAM = "picture";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private int numMessages = 0;
    private LocalBroadcastManager mLocalBroadcastManager;


    @Override
    public void onCreate() {
        //String token = FirebaseInstanceId.getInstance().getToken();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SEND_PUSH);

    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        Log.d("FROM", remoteMessage.getFrom());
        sendNotification(notification, data);
    }


    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bundle bundle = new Bundle();
        bundle.putString(FCM_PARAM, data.get(FCM_PARAM));

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);

        JsonObject jsons = (new JsonParser()).parse(data.get("key")).getAsJsonObject();

        Drawable mDefaultBackground = MyFirebaseMessagingService.this.getResources().getDrawable(R.drawable.avata_defaul);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                        .setLargeIcon(((BitmapDrawable) mDefaultBackground).getBitmap())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(notification.getTitle())
                        .setContentText(jsons.get("message").getAsString());

        Intent notificationIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

        if(jsons.get("type").getAsInt()!= 0)
        {
            updateToDB(jsons.get("message").getAsString(), jsons.get("type").getAsInt() , notification.getTitle(), "");
        }
    }
    private void updateToDB(String message , int type , String Title , String urlAvatar)
    {
        NotificationModel notify = new NotificationModel();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        notify.setAvarta(urlAvatar);
        notify.setType(type);
        notify.setMessage(message);
        notify.setTitle(Title);
        notify.setStatus(0);
        notify.setCreatetime(String.valueOf(timestamp.getTime()));
        DBHelper.getInstance(this).InsertNotify(notify);
        Intent i = new Intent(Constants.SEND_PUSH);
        mLocalBroadcastManager.sendBroadcast(i);
    }
}