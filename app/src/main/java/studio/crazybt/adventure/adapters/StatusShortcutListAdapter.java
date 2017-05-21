package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.one.EmojiOneProvider;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.Status;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 28/09/2016.
 */

public class StatusShortcutListAdapter extends RecyclerView.Adapter<StatusShortcutListAdapter.ViewHolder> {

    private Context rootContext;
    private List<Status> lstStatuses = null;

    public static final int STATUS_DETAIL = 1;
    public static final int STATUS_LIKES = 2;
    public static final int STATUS_COMMENTS = 3;

    public StatusShortcutListAdapter(Context context, List<Status> lstStatuses) {
        this.rootContext = context;
        this.lstStatuses = lstStatuses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EmojiManager.install(new EmojiOneProvider());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status_shortcut, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Status status = lstStatuses.get(position);

        // User avatar
        PicassoHelper.execPicasso_ProfileImage(rootContext, status.getUser().getAvatar(), holder.ivProfileImage);
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                intent.putExtra(CommonConstants.KEY_ID_USER, status.getUser().getId());
                intent.putExtra(CommonConstants.KEY_USERNAME, status.getUser().getFirstName() + " " + status.getUser().getLastName());
                rootContext.startActivity(intent);
            }
        });

        // User name
        holder.tvProfileName.setText(status.getUser().getFirstName() + " " + status.getUser().getLastName());
        holder.tvProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                intent.putExtra(CommonConstants.KEY_ID_USER, status.getUser().getId());
                intent.putExtra(CommonConstants.KEY_USERNAME, status.getUser().getFirstName() + " " + status.getUser().getLastName());
                rootContext.startActivity(intent);
            }
        });

        // Permission (Status Privacy)
        if (status.getPermission() == 1) {
            holder.ivPermission.setImageResource(R.drawable.ic_private_96);
        } else if (status.getPermission() == 2) {
            holder.ivPermission.setImageResource(R.drawable.ic_friend_96);
        } else if (status.getPermission() == 3) {
            holder.ivPermission.setImageResource(R.drawable.ic_public_96);
        }

        // Status create time
        holder.tvTimeUpload.setText(ConvertTimeHelper.convertISODateToPrettyTimeStamp(status.getCreatedAt()));

        // Remove content status if not exits
        if (status.getContent().equals("") || status.getContent() == null) {
            holder.etvContentStatus.setVisibility(View.GONE);
        } else {
            holder.etvContentStatus.setVisibility(View.VISIBLE);
            holder.etvContentStatus.setText(status.getContent());
        }

        // Content status
        holder.etvContentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_DETAIL);
                intent.putExtra("data", status);
                rootContext.startActivity(intent);
            }
        });

        // Click to see detail Images
        holder.llImageStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAdapterClick != null) onAdapterClick.onStatusDetailClick(position);
            }
        });

        // Process display Images
        int countImage = status.getImageContents().size();
        if (countImage == 0) {
            holder.llImageStatus.setVisibility(View.GONE);
        } else {
            holder.llImageStatus.setVisibility(View.VISIBLE);
            holder.llImageStatusUp.setVisibility(View.VISIBLE);
            holder.ivUpItem1.setVisibility(View.VISIBLE);
            if (countImage == 1) {
                PicassoHelper.execPicasso(rootContext, status.getImageContents().get(0).getUrl(), holder.ivUpItem1);
                holder.llImageStatusDown.setVisibility(View.GONE);
                holder.ivUpItem2.setVisibility(View.GONE);
            } else {
                holder.ivUpItem2.setVisibility(View.VISIBLE);
                if (countImage == 2) {
                    PicassoHelper.execPicasso(rootContext, status.getImageContents().get(0).getUrl(), holder.ivUpItem1);
                    PicassoHelper.execPicasso(rootContext, status.getImageContents().get(1).getUrl(), holder.ivUpItem2);
                    holder.llImageStatusDown.setVisibility(View.GONE);
                } else {
                    holder.llImageStatusDown.setVisibility(View.VISIBLE);
                    holder.ivDownItem1.setVisibility(View.VISIBLE);
                    if (countImage == 3) {
                        PicassoHelper.execPicasso(rootContext, status.getImageContents().get(0).getUrl(), holder.ivUpItem1);
                        PicassoHelper.execPicasso(rootContext, status.getImageContents().get(1).getUrl(), holder.ivUpItem2);
                        PicassoHelper.execPicasso(rootContext, status.getImageContents().get(2).getUrl(), holder.ivDownItem1);
                        holder.ivDownItem2.setVisibility(View.GONE);
                        holder.rlDownItem3.setVisibility(View.GONE);
                    } else {
                        holder.ivDownItem2.setVisibility(View.VISIBLE);
                        if (countImage == 4) {
                            PicassoHelper.execPicasso(rootContext, status.getImageContents().get(0).getUrl(), holder.ivUpItem1);
                            PicassoHelper.execPicasso(rootContext, status.getImageContents().get(1).getUrl(), holder.ivUpItem2);
                            PicassoHelper.execPicasso(rootContext, status.getImageContents().get(2).getUrl(), holder.ivDownItem1);
                            PicassoHelper.execPicasso(rootContext, status.getImageContents().get(3).getUrl(), holder.ivDownItem2);
                            holder.rlDownItem3.setVisibility(View.GONE);
                        } else {
                            holder.rlDownItem3.setVisibility(View.VISIBLE);
                            holder.ivDownItem3.setVisibility(View.VISIBLE);
                            if (countImage == 5) {
                                holder.tvDownItem3.setVisibility(View.GONE);
                                PicassoHelper.execPicasso(rootContext, status.getImageContents().get(0).getUrl(), holder.ivUpItem1);
                                PicassoHelper.execPicasso(rootContext, status.getImageContents().get(1).getUrl(), holder.ivUpItem2);
                                PicassoHelper.execPicasso(rootContext, status.getImageContents().get(2).getUrl(), holder.ivDownItem1);
                                PicassoHelper.execPicasso(rootContext, status.getImageContents().get(3).getUrl(), holder.ivDownItem2);
                                PicassoHelper.execPicasso(rootContext, status.getImageContents().get(4).getUrl(), holder.ivDownItem3);
                            } else {
                                if (countImage > 5) {
                                    PicassoHelper.execPicasso(rootContext, status.getImageContents().get(0).getUrl(), holder.ivUpItem1);
                                    PicassoHelper.execPicasso(rootContext, status.getImageContents().get(1).getUrl(), holder.ivUpItem2);
                                    PicassoHelper.execPicasso(rootContext, status.getImageContents().get(2).getUrl(), holder.ivDownItem1);
                                    PicassoHelper.execPicasso(rootContext, status.getImageContents().get(3).getUrl(), holder.ivDownItem2);
                                    PicassoHelper.execPicasso(rootContext, status.getImageContents().get(4).getUrl(), holder.ivDownItem3);
                                    holder.tvDownItem3.setVisibility(View.VISIBLE);
                                    holder.tvDownItem3.setText("+" + (countImage - 5));
                                }
                            }
                        }
                    }
                }
            }
        }

        // Amount of Like
        holder.tvCountLike.setText(String.valueOf(status.getAmountLike()));
        holder.tvCountLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_LIKES);
                intent.putExtra("data", status);
                rootContext.startActivity(intent);
            }
        });

        // Liked highlight
        if (status.getIsLike() == 1) {
            holder.cbLike.setChecked(true);
            holder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.primary));
        } else if (status.getIsLike() == 0) {
            holder.cbLike.setChecked(false);
            holder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.secondary_text));
        }

        // Process Like status
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cbLike.isChecked()) {
                    holder.cbLike.setChecked(false);
                    holder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.secondary_text));
                } else {
                    holder.cbLike.setChecked(true);
                    holder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.primary));
                }
                final String token = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_TOKEN, "");
                Map<String, String> params = new HashMap<>();
                params.put(ApiConstants.KEY_TOKEN, token);
                params.put(ApiConstants.KEY_ID_STATUS, status.getId());
                CustomRequest customRequest = new CustomRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_LIKE_STATUS), params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                            JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                            JSONObject status = JsonUtil.getJSONObject(data, ApiConstants.KEY_STATUS);
                            int amountLike = JsonUtil.getInt(status, ApiConstants.KEY_AMOUNT_LIKE, -1);
                            int isLike = JsonUtil.getInt(data, ApiConstants.KEY_IS_LIKE, -1);
                            if (amountLike != -1) {
                                lstStatuses.get(position).setAmountLike(amountLike);
                            }
                            if (isLike != -1) {
                                lstStatuses.get(position).setIsLike(isLike);
                            }
                            notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                MySingleton.getInstance(rootContext).addToRequestQueue(customRequest, false);
            }
        });

        // Amount of Comment
        holder.tvCountComment.setText(String.valueOf(status.getAmountComment()));
        holder.tvCountComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_COMMENTS);
                intent.putExtra("data", status);
                rootContext.startActivity(intent);
            }
        });

        // Comment highlight
        if (status.getIsComment() == 1) {
            Drawable drawable = ContextCompat.getDrawable(rootContext, R.drawable.ic_chat_bubble_green_24dp);
            holder.tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            holder.tvComment.setTextColor(rootContext.getResources().getColor(R.color.primary));
        } else {
            Drawable drawable = ContextCompat.getDrawable(rootContext, R.drawable.ic_chat_bubble_gray_24dp);
            holder.tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            holder.tvComment.setTextColor(rootContext.getResources().getColor(R.color.secondary_text));
        }

        // Click comment detail
        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_COMMENTS);
                intent.putExtra("data", status);
                rootContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return lstStatuses == null ? 0 : lstStatuses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public DrawableHelper drawableHelper;

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.tvTimeUpload)
        TextView tvTimeUpload;
        @BindView(R.id.ivPermission)
        ImageView ivPermission;
        @BindView(R.id.etvContentStatus)
        EmojiTextView etvContentStatus;
        @BindView(R.id.tvCountLike)
        TextView tvCountLike;
        @BindView(R.id.tvCountComment)
        TextView tvCountComment;
        @BindView(R.id.llLike)
        LinearLayout llLike;
        @BindView(R.id.cbLike)
        CheckBox cbLike;
        @BindView(R.id.tvComment)
        TextView tvComment;
        @BindDimen(R.dimen.item_icon_size_small)
        float itemSizeSmall;

        // Custom Image Show
        @BindView(R.id.llImageStatus)
        LinearLayout llImageStatus;
        @BindView(R.id.llImageStatusUp)
        LinearLayout llImageStatusUp;
        @BindView(R.id.llImageStatusDown)
        LinearLayout llImageStatusDown;
        @BindView(R.id.ivUpItem1)
        ImageView ivUpItem1;
        @BindView(R.id.ivUpItem2)
        ImageView ivUpItem2;
        @BindView(R.id.ivDownItem1)
        ImageView ivDownItem1;
        @BindView(R.id.ivDownItem2)
        ImageView ivDownItem2;
        @BindView(R.id.ivDownItem3)
        ImageView ivDownItem3;
        @BindView(R.id.rlDownItem3)
        RelativeLayout rlDownItem3;
        @BindView(R.id.tvDownItem3)
        TextView tvDownItem3;

        public ViewHolder(View itemView) {
            super(itemView);
            EmojiManager.install(new EmojiOneProvider());
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            drawableHelper = new DrawableHelper(itemView.getContext());
            drawableHelper.setTextViewDrawableFitSize(tvCountLike, R.drawable.ic_thumb_up_96, itemSizeSmall, itemSizeSmall);
            drawableHelper.setTextViewDrawableFitSize(tvCountComment, R.drawable.ic_chat_96, itemSizeSmall, itemSizeSmall);
        }
    }

    private OnAdapterClick onAdapterClick;

    public void setOnAdapterClickListener(OnAdapterClick listener) {
        onAdapterClick = listener;
    }

    public interface OnAdapterClick {
        void onStatusDetailClick(int pos);
    }
}