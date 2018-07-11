package com.aseanfan.worldcafe.UI;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Adapter.ImageTimelineAdapter;
import com.aseanfan.worldcafe.UI.Adapter.PostTimelineAdapter;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

import java.util.ArrayList;
import java.util.List;

public class PostTimeLineActivity extends AppCompatActivity {

    private ArrayList<AlbumFile> mAlbumFiles;
    private ImageButton imgbutton;
    private ImageTimelineAdapter mAdapter;
    private RecyclerView listimage;
    private Button btnshare;
    private EditText edtShare;
    private String listImageBase64 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_time_line);

        listimage= findViewById(R.id.list_image);
        btnshare= findViewById(R.id.btnshare);
        edtShare= findViewById(R.id.input_detail);

        listimage.setLayoutManager(new GridLayoutManager(this, 3));
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        listimage.addItemDecoration(divider);

        mAdapter = new ImageTimelineAdapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage(position);
            }
        });
        listimage.setAdapter(mAdapter);

        imgbutton = (ImageButton)this.findViewById(R.id.btn_select);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostTimeline(listImageBase64);
            }
        });

    }

    private void selectImage() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                .columnCount(2)
                .selectCount(6)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(this)
                                .title("album")
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        mAdapter.notifyDataSetChanged(mAlbumFiles);

                        for (int i = 0; i < mAlbumFiles.size(); i++) {
                            String[] bb = Utils.compressFormat(mAlbumFiles.get(i).getPath(), PostTimeLineActivity.this);
                            String ba1 = bb[0];
                            listImageBase64 += ba1 + ",";
                        }
                      //  mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                       // Toast.makeText(ImageActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    public void PostTimeline(String listImage)
    {
        String urlApi = "";
        JsonObject dataJson = new JsonObject();
        if(listImage.isEmpty())
        {
            urlApi= RestAPI.POST_TIMELINE;

            dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
            dataJson.addProperty("description",edtShare.getText().toString());
        }
        else
        {
            urlApi= RestAPI.POST_TIMELINEIMAGE;
            dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
            dataJson.addProperty("description",edtShare.getText().toString());
            dataJson.addProperty("base64",listImageBase64);
            dataJson.addProperty("image",System.currentTimeMillis() + "");
            dataJson.addProperty("type","image/jpeg");

        }


        RestAPI.PostDataMaster(this,dataJson,urlApi, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);
                        Toast.makeText(getBaseContext(),"Failed to connect to server, please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if (statuscode == RestAPI.STATUS_SUCCESS) {
                        Toast.makeText(getBaseContext(),"Share successful", Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    private void previewImage(int position) {
        if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
            Toast.makeText(this, "no selected", Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles)
                    .currentPosition(position)
                    .widget(
                            Widget.newDarkBuilder(this)
                                    .title("album")
                                    .build()
                    )
                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles = result;
                         //   mAdapter.notifyDataSetChanged(mAlbumFiles);
                           // mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                        }
                    })
                    .start();
        }
    }
}
