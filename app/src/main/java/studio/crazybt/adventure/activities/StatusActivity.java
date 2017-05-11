package studio.crazybt.adventure.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
    private FragmentController fragmentController;
    private static int typeShow = 0;
    StatusDetailFragment statusDetailFragment;
    Status status;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        swipeBackLayout = getSwipeBackLayout();
        Intent intent = getIntent();
        typeShow = intent.getIntExtra("TYPE_SHOW", typeShow);
        if (intent.hasExtra("data")) {
            status = intent.getParcelableExtra("data");
            this.remoteShow(typeShow);
        } else if (intent.hasExtra("data_notify")) {
            Notification notification = intent.getParcelableExtra("data_notify");
//            realm = Realm.getDefaultInstance();
//            realm.beginTransaction();
//            Notification notiExisted = realm.where(Notification.class).equalTo("id", notification.getId()).findFirst();
//            notiExisted.deleteFromRealm();
//            realm.commitTransaction();
            this.loadStatusShortcutFromNotification(notification);
        }
    }

    private void remoteShow(int typeShow){
        switch (typeShow) {
            case 1:
                this.showStatusDetail();
                break;
            case 2:
                this.showStatusLikes();
                break;
            case 3:
                this.showStatusComments();
                break;
            default:
                break;
        }
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
                // status
                String idStatus;
                String content;
                int amountLike;
                int amountComment;
                int permission;
                int type;
                String createdAt;
                int isLike;
                int isComment;
                List<ImageContent> imageContents = new ArrayList<>();

                // user
                String idUser;
                String firstName;
                String lastName;
                String avatar;


                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                idStatus = JsonUtil.getString(data, ApiConstants.KEY_ID, "");
                content = JsonUtil.getString(data, ApiConstants.KEY_CONTENT, "");
                amountLike = JsonUtil.getInt(data, ApiConstants.KEY_AMOUNT_LIKE, 0);
                amountComment = JsonUtil.getInt(data, ApiConstants.KEY_AMOUNT_COMMENT, 0);
                permission = JsonUtil.getInt(data, ApiConstants.KEY_PERMISSION, 3);
                type = JsonUtil.getInt(data, ApiConstants.KEY_TYPE, 1);
                createdAt = JsonUtil.getString(data, ApiConstants.KEY_CREATED_AT, "");
                isLike = JsonUtil.getInt(data, ApiConstants.KEY_IS_LIKE, 0);
                isComment = JsonUtil.getInt(data, ApiConstants.KEY_IS_COMMENT, 0);
                JSONObject owner = JsonUtil.getJSONObject(data, ApiConstants.KEY_OWNER);
                idUser = JsonUtil.getString(owner, ApiConstants.KEY_ID, "");
                firstName = JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, "");
                lastName = JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, "");
                avatar = JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "");
                JSONArray images = JsonUtil.getJSONArray(data, ApiConstants.KEY_IMAGES);
                if (images != null && images.length() > 0) {
                    for (int j = 0; j < images.length(); j++) {
                        JSONObject image = JsonUtil.getJSONObject(images, j);
                        imageContents.add(new ImageContent(
                                JsonUtil.getString(image, ApiConstants.KEY_URL, ""),
                                JsonUtil.getString(image, ApiConstants.KEY_DESCRIPTION, "")));
                    }
                }

                status = new Status(new User(idUser, firstName, lastName, avatar), idStatus, createdAt, content, permission,
                        type, amountLike, amountComment, isLike, isComment, imageContents);
                remoteShow(typeShow);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(StatusActivity.this, errorMsg);
            }
        });
    }

    private void showStatusDetail() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", status);
        statusDetailFragment = new StatusDetailFragment();
        statusDetailFragment.setArguments(bundle);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, statusDetailFragment);
        fragmentController.commit();
    }

    private void showStatusLikes() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", status);
        LikesStatusFragment likesStatusFragment = new LikesStatusFragment();
        likesStatusFragment.setArguments(bundle);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, likesStatusFragment);
        fragmentController.commit();
    }

    private void showStatusComments() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", status);
        CommentsStatusFragment commentsStatusFragment = new CommentsStatusFragment();
        commentsStatusFragment.setArguments(bundle);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, commentsStatusFragment);
        fragmentController.commit();
    }
}
