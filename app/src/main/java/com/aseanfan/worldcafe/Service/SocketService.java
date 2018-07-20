package com.aseanfan.worldcafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.aseanfan.worldcafe.UI.ChatActivity;
import com.aseanfan.worldcafe.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {

    private LocalBroadcastManager mLocalBroadcastManager;

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
        }
    };

    private Emitter.Listener onReceiveMessgeFromServer = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
           /* runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });*/
            JSONObject data = (JSONObject) args[0];
            Intent i = new Intent(Constants.REICEVE_ACTION);
          //  i.putExtra(Constants.FRIENDID,data.getString(""));
            mLocalBroadcastManager.sendBroadcast(i);
            mSocket.disconnect();
        }
    };

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
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        // We should here clean up everything we used.
        super.onDestroy();
    }

    private final BroadcastReceiver mBroadcastReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.SEND_ACTION)) {
                String friendid = intent.getExtras().getString(Constants.FRIENDID);
                JSONObject data = new JSONObject();
                try {
                    data.put("type", 2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("message", data);

             //   Intent i = new Intent(Send_Ethereum_Activity.SEND_STATUS);
              //  i.putExtra(Send_Ethereum_Activity.STATUS, status);
              //  mLocalBroadcastManager.sendBroadcast(i);

            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
