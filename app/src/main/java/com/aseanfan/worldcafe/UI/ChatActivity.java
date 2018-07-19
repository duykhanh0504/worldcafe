package com.aseanfan.worldcafe.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Model.CommentModel;
import com.aseanfan.worldcafe.UI.Adapter.ChatMessageAdapter;
import com.aseanfan.worldcafe.UI.Adapter.CommentAdapter;
import com.aseanfan.worldcafe.worldcafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private ImageButton btnSend;
    private Long chatid;
    private EditText edtChat;

    private RecyclerView rcychat;

    private ChatMessageAdapter mAdapter;

    private List<ChatMessageModel> listmessage;

    Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        App app = (App) getApplication();
        mSocket = app.getSocket();

        mSocket.on("server_to_client", onReceiveMessgeFromServer);

        btnSend = (ImageButton) this.findViewById(R.id.btn_send);
        edtChat = (EditText)this.findViewById(R.id.input_message);
        rcychat = (RecyclerView)this.findViewById(R.id.listChat);

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

    private Emitter.Listener onReceiveMessgeFromServer = new Emitter.Listener() {
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
    };

    private void sendMessage(int typeMessage) {

        try {

            switch (typeMessage) {
                case 0: {
                    String messageText = edtChat.getText().toString();
                    if (messageText.trim().length() == 0)
                        return;
                    JSONObject objMessage = new JSONObject();
                    objMessage.put("type", typeMessage);
                    objMessage.put("message", messageText);
                    JSONObject data = new JSONObject();
                    data.put("type", 2);
                    data.put("message", objMessage);
                    data.put("receiver_id", chatid);

                    mSocket.emit("message", data);

                }
                break;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        edtChat.setText("");
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

    }


}
