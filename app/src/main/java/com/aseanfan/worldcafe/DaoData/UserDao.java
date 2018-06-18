package com.aseanfan.worldcafe.DaoData;

import android.support.annotation.NonNull;

import com.aseanfan.worldcafe.Model.UserModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class UserDao {
    private Realm mRealm;

    public UserDao(@NonNull Realm realm) {
        mRealm = realm;
    }

    public void save(final UserModel user) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(user);
            }
        });
    }

    public void save(final List<UserModel> userList) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(userList);
            }
        });
    }

    public RealmResults<UserModel> loadAll() {
        return mRealm.where(UserModel.class).findAllSorted("id");
    }

    public RealmResults<UserModel> loadAllAsync() {
        return mRealm.where(UserModel.class).findAllSortedAsync("id");
    }

    public RealmObject loadBy(long id) {
        return mRealm.where(UserModel.class).equalTo("id", id).findFirst();
    }

    public void remove(@NonNull final RealmObject object) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                object.removeFromRealm();
            }
        });
    }

    public void removeAll() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.clear(UserModel.class);
            }
        });
    }

    public long count() {
        return mRealm.where(UserModel.class).count();
    }
}
