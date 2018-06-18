package com.aseanfan.worldcafe.Provider;

import com.aseanfan.worldcafe.DaoData.UserDao;
import com.aseanfan.worldcafe.Model.UserModel;

import org.json.JSONObject;

import io.realm.Realm;

public class RealmManager {
    private static Realm mRealm;

    public static Realm open() {
        mRealm = Realm.getDefaultInstance();
        return mRealm;
    }

    public static void close() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    public static UserDao createUserDao() {
        checkForOpenRealm();
        return new UserDao(mRealm);
    }

    public static void clear() {
        checkForOpenRealm();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.clear(UserModel.class);
            }
        });
    }

    private static void checkForOpenRealm() {
        if (mRealm == null || mRealm.isClosed()) {
            throw new IllegalStateException("RealmManager: Realm is closed, call open() method first");
        }
    }
}
