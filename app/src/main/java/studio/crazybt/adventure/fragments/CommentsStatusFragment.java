package studio.crazybt.adventure.fragments;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.one.EmojiOneProvider;

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
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.CommentStatusListAdapter;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.CommentStatus;
import studio.crazybt.adventure.models.Status;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 25/09/2016.
 */

public class CommentsStatusFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

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
    @BindView(R.id.srlComments)
    SwipeRefreshLayout srlComments;
    @BindDimen(R.dimen.item_icon_size_medium)
    float itemSizeMedium;
    private EmojiEditText eetEditComment;
    private EmojiPopup emojiPopup;
    private LinearLayoutManager llmCommentStatus;
    private CommentStatusListAdapter cslaCommentStatus;
    private DrawableHelper drawableHelper;
    private List<CommentStatus> commentStatusList;
    private Status status;
    private Realm realm;
    private int posItem;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            EmojiManager.install(new EmojiOneProvider());
            rootView = inflater.inflate(R.layout.fragment_comments_status, container, false);
            ButterKnife.bind(this, rootView);
            if (getArguments() != null) {
                status = getArguments().getParcelable("data");
            }
            realm = Realm.getDefaultInstance();
            ivEmoticon.setOnClickListener(this);
            ivSendComment.setOnClickListener(this);
            srlComments.setOnRefreshListener(this);
            this.initDrawable();
            this.setupPopUpEmoji();
            this.initCommentsStatusList();
            srlComments.post(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            });
            rlCountLike.setOnClickListener(this);
        }
        return rootView;
    }

    private void initDrawable() {
        drawableHelper = new DrawableHelper(getContext());
        drawableHelper.setTextViewDrawableFitSize(tvCountLike, R.drawable.ic_thumb_up_96, itemSizeMedium, itemSizeMedium);
    }

    private void initCommentsStatusList() {
        commentStatusList = new ArrayList<>();
        llmCommentStatus = new LinearLayoutManager(rootView.getContext());
        rvCommentStatus.setLayoutManager(llmCommentStatus);
        cslaCommentStatus = new CommentStatusListAdapter(rootView.getContext(), commentStatusList);
        cslaCommentStatus.setOnAdapterClickListener(new CommentStatusListAdapter.AdapterClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {
                posItem = pos;
                loadOptionCommentStatus(isOwnerCommentStatus(), v.getLeft() - (v.getWidth() * 2), v.getTop() + (v.getHeight()));
            }
        });
        rvCommentStatus.setAdapter(cslaCommentStatus);
    }

    private void loadData() {
        srlComments.setRefreshing(true);
        commentStatusList.clear();
        tvCountLike.setText(status.getAmountLike() + " " + getResources().getString(R.string.count_like_tv_status));
        final String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_STATUS, status.getId());
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_BROWSE_COMMENT), params, new Response.Listener<JSONObject>() {
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
                srlComments.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srlComments.setRefreshing(false);
            }
        });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(customRequest, false);
    }

    private void loadOptionCommentStatus(boolean isOwner, int x, int y) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_option_comment_status);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = x;
        wmlp.y = y;
        TextView tvCopyComment = (TextView) dialog.findViewById(R.id.tvCopyComment);
        tvCopyComment.setOnClickListener(this);
        TextView tvEditComment = (TextView) dialog.findViewById(R.id.tvEditComment);
        tvEditComment.setOnClickListener(this);
        TextView tvDeleteComment = (TextView) dialog.findViewById(R.id.tvDeleteComment);
        tvDeleteComment.setOnClickListener(this);
        if (!isOwner) {
            tvEditComment.setVisibility(View.GONE);
            tvDeleteComment.setVisibility(View.GONE);
        }
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlCountLike:
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", status);
                LikesStatusFragment likesStatusFragment = new LikesStatusFragment();
                likesStatusFragment.setArguments(bundle);
                FragmentController.replaceFragment_BackStack_Animation(getActivity(), R.id.rlStatus, likesStatusFragment);
                break;
            case R.id.ivEmoticon:
                emojiPopup.toggle();
                break;
            case R.id.ivSendComment:
                if (!StringUtil.isEmpty(eetComment))
                    createComment();
                else
                    eetComment.setError(getResources().getString(R.string.field_can_not_empty));
                break;
            case R.id.tvCopyComment:
                dialog.dismiss();
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("AdventureCopyText", commentStatusList.get(posItem).getContent());
                clipboardManager.setPrimaryClip(clipData);
                ToastUtil.showToast(getContext(), R.string.copy_to_clipboard);
                break;
            case R.id.tvEditComment:
                dialog.dismiss();
                loadEditCommentStatus();
                break;
            case R.id.tvDeleteComment:
                dialog.dismiss();
                deleteComment();
                break;
            case R.id.btnConfirmEdit:
                dialog.dismiss();
                if (!StringUtil.isEmpty(eetEditComment))
                    editComment();
                else
                    eetEditComment.setError(getResources().getString(R.string.field_can_not_empty));
                break;
            case R.id.btnCancelEdit:
                dialog.dismiss();
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

    private void loadEditCommentStatus() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView = li.inflate(R.layout.dialog_edit_comment_status, null);
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        ImageView ivProfileImage = (ImageView) dialog.findViewById(R.id.ivProfileImage);
        PicassoHelper.execPicasso_ProfileImage(getContext(), commentStatusList.get(posItem).getUser().getAvatar(), ivProfileImage);
        TextView tvProfileName = (TextView) dialog.findViewById(R.id.tvProfileName);
        String profileName = commentStatusList.get(posItem).getUser().getFirstName() + " " + commentStatusList.get(posItem).getUser().getLastName();
        tvProfileName.setText(profileName);

        TextView tvTimeUpload = (TextView) dialog.findViewById(R.id.tvTimeUpload);
        tvTimeUpload.setText(new ConvertTimeHelper().convertISODateToPrettyTimeStamp(commentStatusList.get(posItem).getCreatedAt()));

        EmojiTextView etvContentComment = (EmojiTextView) dialog.findViewById(R.id.etvContentComment);
        etvContentComment.setText(commentStatusList.get(posItem).getContent());

        eetEditComment = (EmojiEditText) dialog.findViewById(R.id.eetEditComment);
        eetEditComment.setText(commentStatusList.get(posItem).getContent());

        Button btnConfirmEdit = (Button) dialog.findViewById(R.id.btnConfirmEdit);
        btnConfirmEdit.setOnClickListener(this);

        Button btnCancelEdit = (Button) dialog.findViewById(R.id.btnCancelEdit);
        btnCancelEdit.setOnClickListener(this);

        dialog.show();
    }

    private void createComment() {
        ivEmoticon.setEnabled(false);
        eetComment.setEnabled(false);
        ivSendComment.setEnabled(false);
        final String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_STATUS, status.getId());
        params.put(ApiConstants.KEY_CONTENT, eetComment.getText().toString());
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_COMMENT_STATUS), params, new Response.Listener<JSONObject>() {
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
    }

    private void editComment() {
        final String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_STATUS, status.getId());
        params.put(ApiConstants.KEY_ID_COMMENT, commentStatusList.get(posItem).getId());
        params.put(ApiConstants.KEY_CONTENT, eetEditComment.getText().toString());
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_COMMENT_EDIT_CONTENT), params, new Response.Listener<JSONObject>() {
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
                    commentStatusList.set(posItem, new CommentStatus(userRealmResults, id, idStatus, createdAt, content));
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

    private void deleteComment() {
        final String token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_STATUS, status.getId());
        params.put(ApiConstants.KEY_ID_COMMENT, commentStatusList.get(posItem).getId());
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_DELETE_COMMENT), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                RLog.i(response.toString());
                if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                    JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                    JSONObject status = JsonUtil.getJSONObject(data, ApiConstants.KEY_STATUS);
                    int amountComment = JsonUtil.getInt(status, ApiConstants.KEY_AMOUNT_COMMENT, -1);
                    int isComment = JsonUtil.getInt(data, ApiConstants.KEY_IS_COMMENT, -1);
                    CommentsStatusFragment.this.status.setAmountComment(amountComment);
                    CommentsStatusFragment.this.status.setIsComment(isComment);
                    commentStatusList.remove(posItem);
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

    private boolean isOwnerCommentStatus() {
        String currentUserId = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_ID, "");
        if (currentUserId.equals(commentStatusList.get(posItem).getUser().getId())) {
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        this.loadData();
    }
}
