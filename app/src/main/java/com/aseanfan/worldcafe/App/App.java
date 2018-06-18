package com.aseanfan.worldcafe.App;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration =
                new RealmConfiguration.Builder(this)
                        .name("realm-db.db")
                        .deleteRealmIfMigrationNeeded()
                        .schemaVersion(1)
                        .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
