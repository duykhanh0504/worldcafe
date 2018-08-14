package com.aseanfan.worldcafe.Service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.UI.ChatActivity;
import com.aseanfan.worldcafe.UI.LoginActivity;
import com.aseanfan.worldcafe.UI.MainActivity;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {

    private LocalBroadcastManager mLocalBroadcastManager;
    private int RESTARTSERVICE = 0;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    public void SocketDisconnect() {
         RESTARTSERVICE = 2;
         mSocket.disconnect();
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
          /*  runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "connect error", Toast.LENGTH_LONG).show();
                    mSocket.disconnect();
                    //   Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // getApplicationContext().startActivity(intent);
                }
            });*/
            mSocket.disconnect();
          //  if(RESTARTSERVICE !=2) {
             //   RESTARTSERVICE = 1;
          //  }
            stopService(new Intent(SocketService.this, SocketService.class));
        }
    };

    private Emitter.Listener onReceiveMessgeFromServer = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            if((JSONObject) args[0]!=null) {
                JSONObject data = (JSONObject) args[0];
                ChatMessageModel message = new ChatMessageModel();
                try {
                    message.setMessage_id(data.getLong("message_id"));
                    message.setMessageText(data.getString("message"));
                    message.setSend_account(data.getLong("from_account_id"));
                    message.setReceiver(data.getLong("to_account_ids"));
                    message.setGroupid(Long.valueOf(0));
                    DBHelper.getInstance(getApplicationContext()).InsertMessageChat(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if((int) args[1]==0) {
                    Intent i = new Intent(Constants.REICEVE_ACTION);

                    i.putExtra(Constants.FRIENDID, message.getSend_account());
                    i.putExtra(Constants.MESSAGE, message.getMessageText());
                    i.putExtra(Constants.MESSAGEID, message.getMessage_id());


                    mLocalBroadcastManager.sendBroadcast(i);
                }
                if(isApplicationInForeground() == false)
                {
                    Map<String, String> datamessage = new HashMap<>();
                    String avatar = AccountController.getInstance().getAccount().getAvarta();
                    if(avatar ==null)
                        avatar = "";
                    datamessage.put("avatar" , avatar);
                    datamessage.put("username" ,AccountController.getInstance().getAccount().getUsername());
                    datamessage.put("message", Utils.decodeStringUrl(message.getMessageText()));
                    sendNotification(datamessage);
                }
            }
           // mSocket.disconnect();
        }
    };


    @SuppressLint("CheckResult")
    private void sendNotification(final Map<String, String> data) {


        if(data.get("avatar").isEmpty()) {
            Drawable mDefaultBackground = SocketService.this.getResources().getDrawable(R.drawable.avata_defaul);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(SocketService.this)
                            .setLargeIcon(((BitmapDrawable) mDefaultBackground).getBitmap())
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle(data.get("username"))
                            .setContentText(data.get("message"));

            Intent notificationIntent = new Intent(SocketService.this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(SocketService.this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
        else {
            Glide.with(SocketService.this).load(data.get("avatar")).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    e.printStackTrace();
                    Drawable mDefaultBackground = SocketService.this.getResources().getDrawable(R.drawable.avata_defaul);
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(SocketService.this)
                                    .setLargeIcon(((BitmapDrawable) mDefaultBackground).getBitmap())
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle(data.get("username"))
                                    .setContentText(data.get("message"));

                    Intent notificationIntent = new Intent(SocketService.this, MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(SocketService.this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(SocketService.this)
                                    .setLargeIcon(((BitmapDrawable) resource).getBitmap())
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle("Notifications Example")
                                    .setContentText("This is a test notification");

                    Intent notificationIntent = new Intent(SocketService.this, MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(SocketService.this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                    return false;
                }
            });
        }

}

    @Override
    public void onCreate() {
        super.onCreate();

        mSocket.connect();

        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Socket.EVENT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_DISCONNECT, onConnectError);
        mSocket.on(Socket.EVENT_RECONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, onConnectError);
        mSocket.on("server_to_client", onReceiveMessgeFromServer);
        // Get the localBroadcastManager instance, so that it can communicate with the fragment
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SEND_ACTION);
        mIntentFilter.addAction(Constants.DISCONECT_SOCKET_ACTION);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                JSONObject data = new JSONObject();
                try {
                    data.put("account_id", AccountController.getInstance().getAccount().getId());
                    data.put("username", AccountController.getInstance().getAccount().getUsername());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("message_updateinfo", data);
           /*    if(AccountController.getInstance().getAccount().getId()!=null) {
                    JsonObject dataJson = new JsonObject();
                    dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());

                    RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_UPDATESOCKET, new RestAPI.RestAPIListenner() {
                        @Override
                        public void OnComplete(int httpCode, String error, String s) {
                            if (!RestAPI.checkHttpCode(httpCode)) {
                                //AppFuncs.alert(getApplicationContext(),s,true);

                                return;
                            }


                        }
                    });
                }*/
            }
        }, 1000);

      //  RESTARTSERVICE = 0;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        // We should here clean up everything we used.
        //if (RESTARTSERVICE == 1)
        {
            startService(new Intent(SocketService.this, SocketService.class));
        }
        super.onDestroy();
    }

    public boolean isApplicationInForeground() {
        final ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        // get the info from the currently running task

        List< ActivityManager.RunningTaskInfo > taskInfo = activityManager.getRunningTasks(1);

        if( taskInfo.get(0).topActivity.getClassName().equals("com.aseanfan.worldcafe.UI.ChatActivity"))
        {
            return true;
        }

        return false;
    }

    private final BroadcastReceiver mBroadcastReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.SEND_ACTION)) {
                Long friendid = intent.getExtras().getLong(Constants.FRIENDID);
                String message = intent.getExtras().getString(Constants.MESSAGE);
                int type = intent.getExtras().getInt(Constants.TYPE_MEASSAGE);
                JSONObject data = new JSONObject();
                try {
                    data.put("type", type);
                    data.put("message", message);
                    data.put("receiver_id", friendid);
                    data.put("fullname", AccountController.getInstance().getAccount().getUsername());
                    data.put("avatar", AccountController.getInstance().getAccount().getAvarta());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("message_chat", data);

             //   Intent i = new Intent(Send_Ethereum_Activity.SEND_STATUS);
              //  i.putExtra(Send_Ethereum_Activity.STATUS, status);
              //  mLocalBroadcastManager.sendBroadcast(i);

            }
            if (intent.getAction().equals(Constants.DISCONECT_SOCKET_ACTION))
            {
                SocketDisconnect();
                stopService(new Intent(SocketService.this, SocketService.class));
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
