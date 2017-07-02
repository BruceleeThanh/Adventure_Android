package studio.crazybt.adventure.utils;

import android.content.Context;
import android.widget.Toast;

import studio.crazybt.adventure.libs.ApiConstants;

/**
 * Created by Brucelee Thanh on 09/10/2016.
 */

public class ToastUtil {
    private static Toast mToast;

    public static void showToast(Context context, String message){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showToast(Context context, int messageResID){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(context, context.getResources().getString(messageResID), Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showToast(String message){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(ApiConstants.getContext(), message, Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showToast(int messageResID){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(ApiConstants.getContext(), ApiConstants.getContext().getResources().getString(messageResID), Toast.LENGTH_LONG);
        mToast.show();
    }
}
