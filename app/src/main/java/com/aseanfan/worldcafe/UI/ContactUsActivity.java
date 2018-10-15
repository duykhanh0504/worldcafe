package com.aseanfan.worldcafe.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.JsonObject;

public class ContactUsActivity extends AppCompatActivity {

    private Button btnSend;
    private EditText txtcontactus;
    private ImageView btn_back;

    public void ContactUs(String content) {



        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("content",content);


        RestAPI.PostDataMasterWithToken(getApplicationContext(), dataJson, RestAPI.POST_CONTACTUS, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    ViewDialog dialog = new ViewDialog();
                    dialog.showDialogOK(ContactUsActivity.this, "Send Successful", new ViewDialog.DialogListenner() {
                        @Override
                        public void OnClickConfirm() {
                            finish();
                        }
                    });

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        btnSend = (Button)this.findViewById(R.id.btn_send);
        txtcontactus = (EditText) this.findViewById(R.id.txtcontactus);
        btn_back = this.findViewById(R.id.btncancel);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtcontactus.getText().toString().trim().isEmpty()) {
                    ContactUs(txtcontactus.getText().toString());
                }
            }
        });
    }
}
