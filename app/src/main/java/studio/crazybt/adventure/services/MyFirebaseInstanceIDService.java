package studio.crazybt.adventure.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 11/12/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        saveFCMToken(token);
    }

    private void saveFCMToken(String token){
        SharedPref.getInstance(this).putString(ApiConstants.KEY_FCM_TOKEN, token);
    }

}
