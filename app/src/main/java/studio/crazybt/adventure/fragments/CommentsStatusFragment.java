package studio.crazybt.adventure.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.emoji.Emoji;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiClickedListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.HomePageActivity;
import studio.crazybt.adventure.adapters.CommentStatusListAdapter;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.CommentStatus;
import studio.crazybt.adventure.models.StatusShortcut;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 25/09/2016.
 */

public class CommentsStatusFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    @BindView(R.id.rlCountLike)
    RelativeLayout rlCountLike;
    @BindView(R.id.rvCommentStatus)
    RecyclerView rvCommentStatus;
    @BindView(R.id.tvCountLike)
    TextView tvCountLike;
    @BindView(R.id.ivEmoticon)
    ImageView ivEmoticon;
    @BindView(R.id.eetComment)
    EmojiEditText eetComment;
    @BindView(R.id.ivSendComment)
    ImageView ivSendComment;
    @BindDimen(R.dimen.item_icon_size_medium)
    float itemSizeMedium;
    private EmojiPopup emojiPopup;
    private LinearLayoutManager llmCommentStatus;
    private CommentStatusListAdapter cslaCommentStatus;
    private DrawableProcessHelper drawableProcessHelper;
    private FragmentController fragmentController;
    private List<CommentStatus> commentStatusList;
    private StatusShortcut statusShortcut;
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_comments_status, container, false);
            ButterKnife.bind(this, rootView);
            if (getArguments() != null) {
                statusShortcut = getArguments().getParcelable("data");
            }
            realm = Realm.getDefaultInstance();
            ivEmoticon.setOnClickListener(this);
            ivSendComment.setOnClickListener(this);
            this.initDrawable();
            this.setupPopUpEmoji();
            this.initCommentsStatusList();
            this.loadData();
            rlCountLike.setOnClickListener(this);
        }
        return rootView;
    }

    private void initDrawable() {
        drawableProcessHelper = new DrawableProcessHelper(rootView);
        drawableProcessHelper.setTextViewDrawableFitSize(tvCountLike, R.drawable.ic_thumb_up_96, itemSizeMedium, itemSizeMedium);
    }

    private void initCommentsStatusList() {
        commentStatusList = new ArrayList<>();
        llmCommentStatus = new LinearLayoutManager(rootView.getContext());
        rvCommentStatus.setLayoutManager(llmCommentStatus);
        cslaCommentStatus = new CommentStatusListAdapter(rootView.getContext(), commentStatusList);
        rvCommentStatus.setAdapter(cslaCommentStatus);
    }

    private void loadData() {
        tvCountLike.setText(statusShortcut.getAmountLike() + " " + getResources().getString(R.string.count_like_tv_status));

        final String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        Uri.Builder url = ApiConstants.getApi(ApiConstants.API_BROWSE_COMMENT);
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_STATUS, statusShortcut.getId());
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, url.build().toString(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                    String id;
                    String idStatus;
                    String content;
                    String createdAt;

                    String idUser;
                    String firstName;
                    String lastName;
                    String avatar;
                    JSONArray data = JsonUtil.getJSONArray(response, ApiConstants.DEF_DATA);
                    int length = data.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject item = JsonUtil.getJSONObject(data, i);
                        id = JsonUtil.getString(item, ApiConstants.KEY_ID, "");
                        idStatus = JsonUtil.getString(item, ApiConstants.KEY_ID_STATUS, "");
                        content = JsonUtil.getString(item, ApiConstants.KEY_CONTENT, "");
                        createdAt = JsonUtil.getString(item, ApiConstants.KEY_CREATED_AT, "");
                        JSONObject owner = JsonUtil.getJSONObject(item, ApiConstants.KEY_OWNER);
                        idUser = JsonUtil.getString(owner, ApiConstants.KEY_ID, "");
                        firstName = JsonUtil.getString(owner, ApiConstants.KEY_FIRST_NAME, "");
                        lastName = JsonUtil.getString(owner, ApiConstants.KEY_LAST_NAME, "");
                        avatar = JsonUtil.getString(owner, ApiConstants.KEY_AVATAR, "");
                        commentStatusList.add(new CommentStatus(new User(idUser, firstName, lastName, avatar), id, idStatus, createdAt, content));
                    }
                    cslaCommentStatus.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(customRequest, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlCountLike:
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", statusShortcut);
                LikesStatusFragment likesStatusFragment = new LikesStatusFragment();
                likesStatusFragment.setArguments(bundle);
                fragmentController = new FragmentController(getActivity());
                fragmentController.addFragment_BackStack_Animation(R.id.rlStatus, likesStatusFragment);
                fragmentController.commit();
                break;
            case R.id.ivEmoticon:
                emojiPopup.toggle();
                break;
            case R.id.ivSendComment:
                ivEmoticon.setEnabled(false);
                eetComment.setEnabled(false);
                ivSendComment.setEnabled(false);
                final String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
                final String currentUserID = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_ID, "");
                Uri.Builder url = ApiConstants.getApi(ApiConstants.API_COMMENT_STATUS);
                Map<String, String> params = new HashMap<>();
                params.put(ApiConstants.KEY_TOKEN, token);
                params.put(ApiConstants.KEY_ID_STATUS, statusShortcut.getId());
                params.put(ApiConstants.KEY_CONTENT, eetComment.getText().toString());
                CustomRequest customRequest = new CustomRequest(Request.Method.POST, url.build().toString(), params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                            JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                            String id;
                            String owner;
                            String idStatus;
                            String content;
                            String createdAt;
                            id = JsonUtil.getString(data, ApiConstants.KEY_ID, "");
                            owner = JsonUtil.getString(data, ApiConstants.KEY_OWNER, "");
                            idStatus = JsonUtil.getString(data, ApiConstants.KEY_ID_STATUS, "");
                            content = JsonUtil.getString(data, ApiConstants.KEY_CONTENT, "");
                            createdAt = JsonUtil.getString(data, ApiConstants.KEY_CREATED_AT, "");
                            User userRealmResults = realm.where(User.class).equalTo("id", owner).findFirst();
                            commentStatusList.add(0, new CommentStatus(userRealmResults, id, idStatus, createdAt, content));
                            cslaCommentStatus.notifyDataSetChanged();
                            ivEmoticon.setEnabled(true);
                            eetComment.setEnabled(true);
                            ivSendComment.setEnabled(true);
                            eetComment.setText("");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                MySingleton.getInstance(this.getContext()).addToRequestQueue(customRequest, false);
                break;
        }
    }

    private void setupPopUpEmoji() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override
                    public void onEmojiPopupShown() {
                        ivEmoticon.setImageResource(R.drawable.ic_keyboard_96);
                    }
                }).setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override
                    public void onEmojiPopupDismiss() {
                        ivEmoticon.setImageResource(R.drawable.ic_lol_96);
                    }
                }).setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
                    @Override
                    public void onKeyboardClose() {
                        emojiPopup.dismiss();
                    }
                }).build(eetComment);
    }
}
