package com.aseanfan.worldcafe.UI;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.AreaModel;
import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.Service.SyncDataService;
import com.aseanfan.worldcafe.UI.Adapter.ChatMessageAdapter;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.UI.Component.DIalogImagePreview;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.aseanfan.worldcafe.Helper.cropper.CropImage;
import com.aseanfan.worldcafe.Helper.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private ImageButton btnSend;
    private Long chatid;
    private String chatavatar;
    private EmojiconEditText edtChat;
    ImageView emojiImageView;
    ImageView imagechat;
    View rootView;
    private ProgressBar loading;

    private RecyclerView rcychat;

    private ChatMessageAdapter mAdapter;

    private List<ChatMessageModel> listmessage;


    private LocalBroadcastManager mLocalBroadcastManager;

    EmojIconActions emojIcon;

    private class ListHistoryChatAsync extends AsyncTask<Long, Long, List<ChatMessageModel>> {

        @Override
        protected List<ChatMessageModel> doInBackground(Long... ints) {
            List<ChatMessageModel> list = new ArrayList<>();

            Cursor cursor = DBHelper.getInstance(getApplicationContext()).getAllMessageChat(ints[0]);
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (cursor.isAfterLast() == false) {
                        ChatMessageModel message = new ChatMessageModel();
                        message.setMessage_id(cursor.getLong(cursor.getColumnIndex(DBHelper.MESSAGE_ID)));
                        message.setMessageText(cursor.getString(cursor.getColumnIndex(DBHelper.MESSAGE)));
                        message.setSend_account(cursor.getLong(cursor.getColumnIndex(DBHelper.SEND_ACCOUNT)));
                        message.setReceiver(cursor.getLong(cursor.getColumnIndex(DBHelper.RECEIVER_ACCOUNT)));
                        message.setType(cursor.getInt(cursor.getColumnIndex(DBHelper.TYPE)));
                        list.add(message);
                        cursor.moveToNext();
                    }
                }
         //   SystemClock.sleep(1000);
            return list;
        }

        @Override
        protected void onPostExecute(List<ChatMessageModel> param) {
            if(listmessage==null)
            {
                listmessage = new ArrayList<>();
            }
            listmessage.clear();
            listmessage =param;
            rcychat.smoothScrollToPosition(0);
            Collections.reverse(listmessage);
            mAdapter.setData(listmessage);

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            chatid= extras.getLong("chat_id");
            chatavatar = extras.getString("avatarurl");
            new ListHistoryChatAsync().execute(chatid);
        }


        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.REICEVE_ACTION);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
      //  mSocket.on("server_to_client", onReceiveMessgeFromServer);

        rootView = findViewById(R.id.root_view);
        emojiImageView = (ImageView) findViewById(R.id.emoji_btn);
        imagechat = (ImageView) findViewById(R.id.image_btn);
        btnSend = (ImageButton) this.findViewById(R.id.btn_send);
        edtChat = (EmojiconEditText)this.findViewById(R.id.input_message);
        rcychat = (RecyclerView)this.findViewById(R.id.listChat);

        mAdapter = new ChatMessageAdapter(null,chatavatar);
        mAdapter.setOnItemClickListener(new ChatMessageAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                DialogFragment fragment =  DIalogImagePreview.newInstancestring(listmessage.get(position).getMessageText());
                fragment.show(getSupportFragmentManager(), "image preview");
            }
        });


         LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL,true);

      //  mLayoutManager.setStackFromEnd(true);
        rcychat.setLayoutManager(mLayoutManager);
        rcychat.setItemAnimator(new DefaultItemAnimator());
        rcychat.setAdapter(mAdapter);

      //  ListComment(timelineId);

        loading = (ProgressBar) this.findViewById(R.id.loading);


        emojIcon = new EmojIconActions(this,rootView, edtChat ,emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
               // Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
               // Log.e(TAG, "Keyboard closed");
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtChat.getText().toString().trim().isEmpty())
                {

                    String messageText = Utils.encodeStringUrl(edtChat.getText().toString());
                    sendMessage(0, messageText);
                }
            }
        });
        imagechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 0 );
            }
            Intent intent = CropImage.activity(null)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(this);
            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);


        } catch (Exception e) {
            //   dialog.showDialogCancel( LoginActivity.this,"Camera Permission error" );
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
  /*  private Emitter.Listener onReceiveMessgeFromServer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int type = 0;
                    try {
                        type = data.getInt("type");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };*/

    private final BroadcastReceiver mBroadcastReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.REICEVE_ACTION)) {
                Long friendid = intent.getExtras().getLong(Constants.FRIENDID);
                if(friendid.equals(chatid)) {
                    Long messageid = intent.getExtras().getLong(Constants.MESSAGEID);
                    String message = intent.getExtras().getString(Constants.MESSAGE);
                    ChatMessageModel m = new ChatMessageModel();
                    m.setReceiver(AccountController.getInstance().getAccount().getId());
                    m.setMessage_id(messageid);
                    m.setMessageText(message);
                    m.setSend_account(friendid);
                    m.setType(intent.getExtras().getInt(Constants.MESSAGETYPE));
                    listmessage.add(0,m);
                    mAdapter.setData(listmessage);
                    rcychat.smoothScrollToPosition(0);
                }

            }
        }
    };

    private void sendMessage(int typeMessage , String message) {

        switch (typeMessage) {
            case 0: {

                Intent i = new Intent(Constants.SEND_ACTION);
                i.putExtra(Constants.FRIENDID,chatid);
                i.putExtra(Constants.TYPE_MEASSAGE,0);
                i.putExtra(Constants.MESSAGE,message);
                mLocalBroadcastManager.sendBroadcast(i);

                ChatMessageModel m = new ChatMessageModel();
                m.setReceiver(chatid);
                m.setMessageText(message);
                m.setSend_account(AccountController.getInstance().getAccount().getId());
                m.setType(0);
              //  listmessage.add(m);
                mAdapter.setDataRow(m);
                rcychat.smoothScrollToPosition(0);


           //     mSocket.emit("message", data);

            }
            break;
            case 1:
            {
                if (message.trim().length() == 0)
                    return;


                Intent i = new Intent(Constants.SEND_ACTION);
                i.putExtra(Constants.FRIENDID,chatid);
                i.putExtra(Constants.TYPE_MEASSAGE,1);
                i.putExtra(Constants.MESSAGE,message);
                mLocalBroadcastManager.sendBroadcast(i);

                ChatMessageModel m = new ChatMessageModel();
                m.setReceiver(chatid);
                m.setMessageText(message);
                m.setSend_account(AccountController.getInstance().getAccount().getId());
                m.setType(1);
                //  listmessage.add(m);
                mAdapter.setDataRow(m);
                rcychat.smoothScrollToPosition(0);
            }
                break;

        }

        edtChat.setText("");
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    String[] bb = Utils.compressFormat(result.getUri().getPath(), this);
                    String base64 = bb[0];
                    sendMessage(1,base64);
                }
                break;
        }
    }



}
