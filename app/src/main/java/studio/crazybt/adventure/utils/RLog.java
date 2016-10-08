package studio.crazybt.adventure.utils;

import android.util.Log;

import studio.crazybt.adventure.BuildConfig;

/**
 * Created by Brucelee Thanh on 09/10/2016.
 */

public class RLog {

    private static final String TAG = "Adventure";

    public static void v(Object message){
        if(BuildConfig.DEBUG){
            Log.v(TAG, String.valueOf(message));
        }
    }

    public static void d(Object message){
        if(BuildConfig.DEBUG){
            Log.d(TAG, String.valueOf(message));
        }
    }

    public static void i(Object message){
        if(BuildConfig.DEBUG){
            Log.i(TAG, String.valueOf(message));
        }
    }

    public static void w(Object message){
        if(BuildConfig.DEBUG){
            Log.w(TAG, String.valueOf(message));
        }
    }

    public static void e(Object message){
        if(BuildConfig.DEBUG){
            Log.e(TAG, String.valueOf(message));
        }
    }

}