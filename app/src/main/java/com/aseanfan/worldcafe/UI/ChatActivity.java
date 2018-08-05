package com.aseanfan.worldcafe.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.UI.Adapter.ChatMessageAdapter;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private ImageButton btnSend;
    private Long chatid;
    private EmojiconEditText edtChat;
    ImageView emojiImageView;
    View rootView;

    private RecyclerView rcychat;

    private ChatMessageAdapter mAdapter;

    private List<ChatMessageModel> listmessage;


    private LocalBroadcastManager mLocalBroadcastManager;

    EmojIconActions emojIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        App app = (App) getApplication();
       // mSocket = app.getSocket();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.REICEVE_ACTION);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
      //  mSocket.on("server_to_client", onReceiveMessgeFromServer);

        rootView = findViewById(R.id.root_view);
        emojiImageView = (ImageView) findViewById(R.id.emoji_btn);
        btnSend = (ImageButton) this.findViewById(R.id.btn_send);
        edtChat = (EmojiconEditText)this.findViewById(R.id.input_message);
        rcychat = (RecyclerView)this.findViewById(R.id.listChat);

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

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            chatid= extras.getLong("chat_id");
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(0);
            }
        });


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
                String friendid = intent.getExtras().getString(Constants.FRIENDID);
                JSONObject data = new JSONObject();
                try {
                    data.put("type", 2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
             //   mSocket.emit("message", data);

                //   Intent i = new Intent(Send_Ethereum_Activity.SEND_STATUS);
                //  i.putExtra(Send_Ethereum_Activity.STATUS, status);
                //  mLocalBroadcastManager.sendBroadcast(i);

            }
        }
    };

    private void sendMessage(int typeMessage) {

        switch (typeMessage) {
            case 0: {
                String messageText = edtChat.getText().toString();
                if (messageText.trim().length() == 0)
                    return;


                Intent i = new Intent(Constants.SEND_ACTION);
                i.putExtra(Constants.FRIENDID,chatid);
                i.putExtra(Constants.TYPE_MEASSAGE,3);
                i.putExtra(Constants.MESSAGE,messageText);
                mLocalBroadcastManager.sendBroadcast(i);

           //     mSocket.emit("message", data);

            }
            break;

        }

        edtChat.setText("");
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

    }


}
