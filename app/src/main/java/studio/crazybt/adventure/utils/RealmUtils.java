package studio.crazybt.adventure.utils;

import android.app.Application;
import android.content.Context;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import studio.crazybt.adventure.libs.ApiConstants;

/**
 * Created by Brucelee Thanh on 07/12/2016.
 */

public class RealmUtils extends Application {

    private Socket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("AdventureDB").build();
        Realm.setDefaultConfiguration(realmConfiguration);
        ApiConstants.instance(getApplicationContext());
        if(ApiConstants.getApiRoot() == null || ApiConstants.getApiRoot().equals("")){
            ApiConstants.setApiRoot(ApiConstants.getFirstApiRoot());
        }
        if(ApiConstants.getApiRootImages() == null || ApiConstants.getApiRootImages().equals("")){
            ApiConstants.setApiRootImages(ApiConstants.getFirstApiRootImages());
        }
        try {
            mSocket = IO.socket(ApiConstants.getBaseUrl());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Context getContext() {
        return getApplicationContext();
    }

    public Socket getSocket() {
        return mSocket;
    }
}
