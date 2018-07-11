package com.aseanfan.worldcafe.App;

import android.app.Application;
import android.os.Handler;

import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.UI.MediaLoader;
import com.aseanfan.worldcafe.Utils.Constants;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.net.URISyntaxException;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;

public class App  extends Application {

    public static volatile Handler applicationHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance(getApplicationContext());
        applicationHandler = new Handler(getApplicationContext().getMainLooper());
        mSocket.connect();
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        );
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
