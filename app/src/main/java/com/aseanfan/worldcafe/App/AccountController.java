package com.aseanfan.worldcafe.App;

import com.aseanfan.worldcafe.Model.UserModel;

import java.net.UnknownServiceException;

public class AccountController {

    private static volatile AccountController Instance = null;

    private UserModel account = new UserModel();

    public static AccountController getInstance() {
        AccountController localInstance = Instance;
        if (localInstance == null) {
            synchronized (AccountController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new AccountController();
                }
            }
        }
        return localInstance;
    }

    public void SetAccount(UserModel user)
    {
        account = user;
    }

    public UserModel getAccount()
    {
        return account;
    }

    public AccountController()
    {

    }

}
