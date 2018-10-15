package com.aseanfan.worldcafe.UI;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChangePassActivity extends AppCompatActivity {

    private EditText oldpass;
    private EditText newpass;
    private EditText repass;
    private Button btn_change;
    private ViewDialog dialog;
    private ImageView btn_back;

    void changePass( String oldpass , String newpass)
    {
        final ProgressDialog progressDialog = new ProgressDialog(ChangePassActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Waiting...");
        progressDialog.show();
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("old_password",oldpass);
        dataJson.addProperty("new_password",newpass);


        RestAPI.PostDataMasterWithToken(getApplicationContext(), dataJson, RestAPI.POST_CHANGEPASS, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        dialog.showDialogCancel( ChangePassActivity.this,getResources().getString(R.string.can_not_connect_server) );
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if(statuscode == RestAPI.STATUS_SUCCESS)
                    {
                        dialog.showDialogOK(ChangePassActivity.this, "Change password successful", new ViewDialog.DialogListenner() {
                            @Override
                            public void OnClickConfirm() {
                                finish();
                            }
                        });
                    }
                    else if(statuscode == 2)
                    {
                        dialog.showDialogCancel( ChangePassActivity.this,jsons.get("message").getAsString() );
                        //Toast.makeText(LoginActivity.this, "Invalid Activation Code", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception ex) {

                    dialog.showDialogCancel( ChangePassActivity.this,ex.getMessage() );
                    Toast.makeText(ChangePassActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
                finally {
                    progressDialog.dismiss();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        oldpass = this.findViewById(R.id.input_oldpass);
        newpass = this.findViewById(R.id.input_newpass);
        repass = this.findViewById(R.id.input_repass);
        btn_change = this.findViewById(R.id.btn_changepass);
        btn_back = this.findViewById(R.id.btncancel);
        dialog = new ViewDialog();


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldpass.getText().toString().isEmpty())
                {
                    dialog.showDialogCancel( ChangePassActivity.this,getResources().getString(R.string.password_empty));
                    //    Toast.makeText(LoginActivity.this, "Passcode can not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newpass.getText().toString().isEmpty())
                {
                    dialog.showDialogCancel( ChangePassActivity.this,getResources().getString(R.string.password_empty) );
                    //    Toast.makeText(LoginActivity.this, "Passcode can not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newpass.getText().toString().equals(repass.getText().toString()))
                {
                    changePass(oldpass.getText().toString(),newpass.getText().toString());
                }
                else
                {
                    dialog.showDialogCancel( ChangePassActivity.this,"Password does not match the confirm password" );
                    //Toast.makeText(LoginActivity.this, "Password does not match the confirm password", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}
