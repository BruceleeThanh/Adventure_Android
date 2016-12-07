package studio.crazybt.adventure.utils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Brucelee Thanh on 07/12/2016.
 */

public class RealmUtils extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("AdventureDB").build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
