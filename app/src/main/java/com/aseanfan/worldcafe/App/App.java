package com.aseanfan.worldcafe.App;

import android.app.Application;

import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Utils.Constants;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class App  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance(this);
    }
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
}
