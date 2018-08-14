package com.aseanfan.worldcafe.Service;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onCreate() {
        String token = FirebaseInstanceId.getInstance().getToken();

        if(AccountController.getInstance().getAccount().getId()!=null) {
            JsonObject dataJson = new JsonObject();
            dataJson.addProperty("device", "android");
            dataJson.addProperty("push_token", token);
            dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());

            RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_UPDATEUSER, new RestAPI.RestAPIListenner() {

                @Override
                public void OnComplete(int httpCode, String error, String s) {
                    try {
                        if (!RestAPI.checkHttpCode(httpCode)) {
                            //AppFuncs.alert(getApplicationContext(),s,true);

                            return;
                        }


                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }

                }
            });
        }

    }

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

    }
}