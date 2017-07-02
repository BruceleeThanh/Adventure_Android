package studio.crazybt.adventure.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.CommentsStatusFragment;
import studio.crazybt.adventure.fragments.LikesStatusFragment;
import studio.crazybt.adventure.fragments.StatusDetailFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.Notification;
import studio.crazybt.adventure.models.Status;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

public class StatusActivity extends SwipeBackActivity {

    private SwipeBackLayout swipeBackLayout;
    private static int typeShow = 0;
    StatusDetailFragment statusDetailFragment;
    Status status;
    Realm realm;

    public static Intent newInstance(Context context, int typeShow, Status status) {
        Intent intent = new Intent(context, StatusActivity.class);
        intent.putExtra(CommonConstants.KEY_TYPE_SHOW, typeShow);
        intent.putExtra(CommonConstants.KEY_DATA, status);
        return intent;
    }

    public static Intent newInstance(Context context, int typeShow, Notification notification) {
        Intent intent = new Intent(context, StatusActivity.class);
        intent.putExtra(CommonConstants.KEY_TYPE_SHOW, typeShow);
        intent.putExtra(ApiConstants.KEY_NOTIFY, notification);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        swipeBackLayout = getSwipeBackLayout();
        Intent intent = getIntent();
        typeShow = intent.getIntExtra(CommonConstants.KEY_TYPE_SHOW, typeShow);
        if (intent.hasExtra(CommonConstants.KEY_DATA)) {
            status = intent.getParcelableExtra(CommonConstants.KEY_DATA);
            this.remoteShow(typeShow);
        } else if (intent.hasExtra(ApiConstants.KEY_NOTIFY)) {
            Notification notification = intent.getParcelableExtra(ApiConstants.KEY_NOTIFY);
//            realm = Realm.getDefaultInstance();
//            realm.beginTransaction();
//            Notification notiExisted = realm.where(Notification.class).equalTo("id", notification.getId()).findFirst();
//            notiExisted.deleteFromRealm();
//            realm.commitTransaction();
            this.loadStatusShortcutFromNotification(notification);
        }
    }


    private void remoteShow(int typeShow) {
        if (typeShow == CommonConstants.ACT_STATUS_DETAIL)
            this.showStatusDetail();
        else if (typeShow == CommonConstants.ACT_STATUS_LIKE)
            this.showStatusLikes();
        else if (typeShow == CommonConstants.ACT_STATUS_COMMENT)
            this.showStatusComments();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            if (typeShow == 1) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", statusDetailFragment.getStatus());
                setResult(Activity.RESULT_OK, returnIntent);
            }
            finish();
        } else {
            super.onBackPressed();
        }
    }

    // Big fucking bug: can't setResult to previous activity
    @Override
    public void scrollToFinishActivity() {
        super.scrollToFinishActivity();
        onBackPressed();
    }

    private void loadStatusShortcutFromNotification(Notification notification) {
        final String token = SharedPref.getInstance(this).getString(ApiConstants.KEY_TOKEN, "");
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_STATUS, notification.getObject());
        AdventureRequest adventureRequest = new AdventureRequest(this, Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_FIND_ONE_STATUS), params, false);
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                JSONObject owner = JsonUtil.getJSONObject(data, ApiConstants.KEY_OWNER);
                JSONArray images = JsonUtil.getJSONArray(data, ApiConstants.KEY_IMAGES);
                List<ImageContent> imageContents = new ArrayList<>();
                if (images != null && images.length() > 0) {
                    for (int j = 0; j < images.length(); j++) {
                        JSONObject image = JsonUtil.getJSONObject(images, j);
                        imageContents.add(new ImageContent(
                                JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                    }
                }

                status = new Status(
                        new User(
                                JsonUtil.getString(owner, ApiConstants.KEY_ID, ""),
                                JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, ""),
                                JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, ""),
                                JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "")),
                        JsonUtil.getString(data, ApiConstants.KEY_ID, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_CREATED_AT, ""),
                        JsonUtil.getString(data, ApiConstants.KEY_CONTENT, ""),
                        JsonUtil.getInt(data, ApiConstants.KEY_PERMISSION, 3),
                        JsonUtil.getInt(data, ApiConstants.KEY_TYPE, 1),
                        JsonUtil.getInt(data, ApiConstants.KEY_AMOUNT_LIKE, 0),
                        JsonUtil.getInt(data, ApiConstants.KEY_AMOUNT_COMMENT, 0),
                        JsonUtil.getInt(data, ApiConstants.KEY_IS_LIKE, 0),
                        JsonUtil.getInt(data, ApiConstants.KEY_IS_COMMENT, 0),
                        imageContents);
                remoteShow(typeShow);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(errorMsg);
            }
        });
    }

    private void showStatusDetail() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", status);
        statusDetailFragment = new StatusDetailFragment();
        statusDetailFragment.setArguments(bundle);
        FragmentController.replaceFragment_BackStack_Animation(this, R.id.rlStatus, statusDetailFragment);
    }

    private void showStatusLikes() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", status);
        LikesStatusFragment likesStatusFragment = new LikesStatusFragment();
        likesStatusFragment.setArguments(bundle);
        FragmentController.replaceFragment_BackStack_Animation(this, R.id.rlStatus, likesStatusFragment);
    }

    private void showStatusComments() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", status);
        CommentsStatusFragment commentsStatusFragment = new CommentsStatusFragment();
        commentsStatusFragment.setArguments(bundle);
        FragmentController.replaceFragment_BackStack_Animation(this, R.id.rlStatus, commentsStatusFragment);
    }
}
