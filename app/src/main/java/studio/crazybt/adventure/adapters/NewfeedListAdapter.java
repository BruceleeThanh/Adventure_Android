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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.fragments.LikesStatusFragment;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.activities.TripActivity;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.StatusShortcut;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 24/09/2016.
 */

public class NewfeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context rootContext;
    private FragmentController fragmentController;
    private List<StatusShortcut> statusShortcuts;
    private PicassoHelper picassoHelper = new PicassoHelper();

    private static final int STATUS = 0;
    private static final int TRIP = 1;

    private static final int STATUS_DETAIL = 1;
    private static final int STATUS_LIKES = 2;
    private static final int STATUS_COMMENTS = 3;

    public NewfeedListAdapter(Context context) {
        this.rootContext = context;
    }

    public NewfeedListAdapter(Context rootContext, List<StatusShortcut> statusShortcuts) {
        this.rootContext = rootContext;
        this.statusShortcuts = statusShortcuts;
    }

    @Override
    public int getItemViewType(int position) {
//        if(position == 0 || position == 2 || position == 4 || position == 6 | position == 8){
//            return STATUS;
//        }else{
//            return TRIP;
//        }
        return STATUS;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(rootContext);
        switch (viewType) {
            case STATUS:
                View viewStatus = li.inflate(R.layout.item_status_shortcut, parent, false);
                return new StatusViewHolder(viewStatus);
            case TRIP:
                View viewTrip = li.inflate(R.layout.item_trip_shortcut, parent, false);
                return new TripViewHolder(viewTrip);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case STATUS:
                final StatusViewHolder statusViewHolder = (StatusViewHolder) holder;
                final StatusShortcut statusItem = statusShortcuts.get(position);

                // User avatar
                statusViewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, ProfileActivity.class);
                        intent.putExtra(CommonConstants.KEY_ID_USER, statusItem.getUser().getId());
                        intent.putExtra(CommonConstants.KEY_USERNAME, statusItem.getUser().getFirstName() + " " + statusItem.getUser().getLastName());
                        rootContext.startActivity(intent);
                    }
                });

                // User name
                statusViewHolder.tvProfileName.setText(statusItem.getUser().getFirstName() + " " + statusItem.getUser().getLastName());
                statusViewHolder.tvProfileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, ProfileActivity.class);
                        intent.putExtra(CommonConstants.KEY_ID_USER, statusItem.getUser().getId());
                        intent.putExtra(CommonConstants.KEY_USERNAME, statusItem.getUser().getFirstName() + " " + statusItem.getUser().getLastName());
                        rootContext.startActivity(intent);
                    }
                });

                // Permission (Status Privacy)
                if (statusItem.getPermission() == 1) {
                    statusViewHolder.ivPermission.setImageResource(R.drawable.ic_private_96);
                } else if (statusItem.getPermission() == 2) {
                    statusViewHolder.ivPermission.setImageResource(R.drawable.ic_friend_96);
                } else if (statusItem.getPermission() == 3) {
                    statusViewHolder.ivPermission.setImageResource(R.drawable.ic_public_96);
                }

                // Status create time
                statusViewHolder.tvTimeUpload.setText(new ConvertTimeHelper().convertISODateToPrettyTimeStamp(statusItem.getCreatedAt()));

                // Remove content status if not exits
                if (statusItem.getContent().equals("") || statusItem.getContent() == null) {
                    statusViewHolder.tvContentStatus.setVisibility(View.GONE);
                } else {
                    statusViewHolder.tvContentStatus.setVisibility(View.VISIBLE);
                    statusViewHolder.tvContentStatus.setText(statusItem.getContent());
                }

                // Content status
                statusViewHolder.tvContentStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, StatusActivity.class);
                        intent.putExtra("TYPE_SHOW", STATUS_DETAIL);
                        intent.putExtra("data", statusItem);
                        rootContext.startActivity(intent);
                    }
                });

                // Click to see detail Images
                statusViewHolder.llImageStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onAdapterClick!= null) onAdapterClick.onStatusDetailClick(position, statusItem);
                    }
                });

                // Process display Images
                int countImage = statusItem.getImageContents().size();
                if (countImage == 0) {
                    statusViewHolder.llImageStatus.setVisibility(View.GONE);
                } else {
                    statusViewHolder.llImageStatus.setVisibility(View.VISIBLE);
                    statusViewHolder.llImageStatusUp.setVisibility(View.VISIBLE);
                    statusViewHolder.ivUpItem1.setVisibility(View.VISIBLE);
                    if (countImage == 1) {
                        picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                        statusViewHolder.llImageStatusDown.setVisibility(View.GONE);
                        statusViewHolder.ivUpItem2.setVisibility(View.GONE);
                    } else {
                        statusViewHolder.ivUpItem2.setVisibility(View.VISIBLE);
                        if (countImage == 2) {
                            picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                            picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                            statusViewHolder.llImageStatusDown.setVisibility(View.GONE);
                        } else {
                            statusViewHolder.llImageStatusDown.setVisibility(View.VISIBLE);
                            statusViewHolder.ivDownItem1.setVisibility(View.VISIBLE);
                            if (countImage == 3) {
                                picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                                picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                                picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(2).getUrl(), statusViewHolder.ivDownItem1);
                                statusViewHolder.ivDownItem2.setVisibility(View.GONE);
                                statusViewHolder.rlDownItem3.setVisibility(View.GONE);
                            } else {
                                statusViewHolder.ivDownItem2.setVisibility(View.VISIBLE);
                                if (countImage == 4) {
                                    picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                                    picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                                    picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(2).getUrl(), statusViewHolder.ivDownItem1);
                                    picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(3).getUrl(), statusViewHolder.ivDownItem2);
                                    statusViewHolder.rlDownItem3.setVisibility(View.GONE);
                                } else {
                                    statusViewHolder.rlDownItem3.setVisibility(View.VISIBLE);
                                    statusViewHolder.ivDownItem3.setVisibility(View.VISIBLE);
                                    if (countImage == 5) {
                                        statusViewHolder.tvDownItem3.setVisibility(View.GONE);
                                        picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                                        picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                                        picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(2).getUrl(), statusViewHolder.ivDownItem1);
                                        picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(3).getUrl(), statusViewHolder.ivDownItem2);
                                        picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(4).getUrl(), statusViewHolder.ivDownItem3);
                                    } else {
                                        if (countImage > 5) {
                                            picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                                            picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                                            picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(2).getUrl(), statusViewHolder.ivDownItem1);
                                            picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(3).getUrl(), statusViewHolder.ivDownItem2);
                                            picassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(4).getUrl(), statusViewHolder.ivDownItem3);
                                            statusViewHolder.tvDownItem3.setVisibility(View.VISIBLE);
                                            statusViewHolder.tvDownItem3.setText("+" + (countImage - 5));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Amount of Like
                statusViewHolder.tvCountLike.setText(String.valueOf(statusItem.getAmountLike()));
                statusViewHolder.tvCountLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, StatusActivity.class);
                        intent.putExtra("TYPE_SHOW", STATUS_LIKES);
                        intent.putExtra("data", statusItem);
                        rootContext.startActivity(intent);
                    }
                });

                // Liked highlight
                if (statusItem.getIsLike() == 1) {
                    statusViewHolder.cbLike.setChecked(true);
                    statusViewHolder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.primary));
                } else if (statusItem.getIsLike() == 0) {
                    statusViewHolder.cbLike.setChecked(false);
                    statusViewHolder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.secondary_text));
                }

                // Process Like status
                statusViewHolder.llLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (statusViewHolder.cbLike.isChecked()) {
                            statusViewHolder.cbLike.setChecked(false);
                            statusViewHolder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.secondary_text));
                        } else {
                            statusViewHolder.cbLike.setChecked(true);
                            statusViewHolder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.primary));
                        }
                        final String token = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_TOKEN, "");
                        Uri.Builder url = ApiConstants.getApi(ApiConstants.API_LIKE_STATUS);
                        Map<String, String> params = new HashMap<>();
                        params.put(ApiConstants.KEY_TOKEN, token);
                        params.put(ApiConstants.KEY_ID_STATUS, statusItem.getId());
                        CustomRequest customRequest = new CustomRequest(Request.Method.POST, url.build().toString(), params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                                    JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                                    JSONObject status = JsonUtil.getJSONObject(data, ApiConstants.KEY_STATUS);
                                    int amountLike = JsonUtil.getInt(status, ApiConstants.KEY_AMOUNT_LIKE, -1);
                                    int isLike = JsonUtil.getInt(data, ApiConstants.KEY_IS_LIKE, -1);
                                    if (amountLike != -1) {
                                        statusShortcuts.get(position).setAmountLike(amountLike);
                                    }
                                    if (isLike != -1) {
                                        statusShortcuts.get(position).setIsLike(isLike);
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
                statusViewHolder.tvCountComment.setText(String.valueOf(statusItem.getAmountComment()));
                statusViewHolder.tvCountComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, StatusActivity.class);
                        intent.putExtra("TYPE_SHOW", STATUS_COMMENTS);
                        intent.putExtra("data", statusItem);
                        rootContext.startActivity(intent);
                    }
                });

                // Comment highlight
                if(statusItem.getIsComment() == 1){
                    Drawable drawable = ContextCompat.getDrawable(rootContext, R.drawable.ic_chat_bubble_green_24dp);
                    statusViewHolder.tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    statusViewHolder.tvComment.setTextColor(rootContext.getResources().getColor(R.color.primary));
                }else{
                    Drawable drawable = ContextCompat.getDrawable(rootContext, R.drawable.ic_chat_bubble_gray_24dp);
                    statusViewHolder.tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    statusViewHolder.tvComment.setTextColor(rootContext.getResources().getColor(R.color.secondary_text));
                }

                // Click comment detail
                statusViewHolder.tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, StatusActivity.class);
                        intent.putExtra("TYPE_SHOW", STATUS_COMMENTS);
                        intent.putExtra("data", statusItem);
                        rootContext.startActivity(intent);
                    }
                });
                break;
            case TRIP:
                TripViewHolder tripViewHolder = (TripViewHolder) holder;
                tripViewHolder.rlTripShortcut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, TripActivity.class);
                        rootContext.startActivity(intent);
                    }
                });
                tripViewHolder.tvTripDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, TripActivity.class);
                        rootContext.startActivity(intent);
                    }
                });
                tripViewHolder.tvProfileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, ProfileActivity.class);
                        rootContext.startActivity(intent);
                    }
                });
                tripViewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, ProfileActivity.class);
                        rootContext.startActivity(intent);
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return statusShortcuts.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public DrawableProcessHelper drawableProcessHelper;

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.tvTimeUpload)
        TextView tvTimeUpload;
        @BindView(R.id.ivPermission)
        ImageView ivPermission;
        @BindView(R.id.tvContentStatus)
        TextView tvContentStatus;
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

        public StatusViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            drawableProcessHelper = new DrawableProcessHelper(itemView);
            drawableProcessHelper.setTextViewDrawableFitSize(tvCountLike, R.drawable.ic_thumb_up_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvCountComment, R.drawable.ic_chat_96, itemSizeSmall, itemSizeSmall);
        }
    }

    public class TripViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        private DrawableProcessHelper drawableProcessHelper;

        @BindView(R.id.rlTripShortcut)
        RelativeLayout rlTripShortcut;
        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.tvTimeUpload)
        TextView tvTimeUpload;
        @BindView(R.id.tvTripName)
        TextView tvTripName;
        @BindView(R.id.tvTripStartPosition)
        TextView tvTripStartPosition;
        @BindView(R.id.tvTripPeriod)
        TextView tvTripPeriod;
        @BindView(R.id.tvTripDestination)
        TextView tvTripDestination;
        @BindView(R.id.tvTripMoney)
        TextView tvTripMoney;
        @BindView(R.id.tvTripMember)
        TextView tvTripMember;
        @BindView(R.id.tvTripJoiner)
        TextView tvTripJoiner;
        @BindView(R.id.tvTripInterested)
        TextView tvTripInterested;
        @BindView(R.id.tvTripRate)
        TextView tvTripRate;
        @BindView(R.id.tvTripDetail)
        TextView tvTripDetail;
        @BindDimen(R.dimen.item_icon_size_small)
        float itemSizeSmall;
        @BindDimen(R.dimen.five_star_icon_width)
        float fiveStarWidth;
        @BindDimen(R.dimen.five_star_icon_height)
        float fiveStarHeight;

        public TripViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            drawableProcessHelper = new DrawableProcessHelper(itemView);
            ButterKnife.bind(this, itemView);

            drawableProcessHelper.setTextViewDrawableFitSize(tvTripName, R.drawable.ic_signpost_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripStartPosition, R.drawable.ic_flag_filled_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripPeriod, R.drawable.ic_clock_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripDestination, R.drawable.ic_marker_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripMoney, R.drawable.ic_money_bag_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripMember, R.drawable.ic_user_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripJoiner, R.drawable.ic_airplane_take_off_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripInterested, R.drawable.ic_like_filled_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripRate, R.drawable.ic_five_star_96, fiveStarWidth, fiveStarHeight);
        }
    }

    private OnAdapterClick onAdapterClick;
    public void setOnAdapterClickListener(OnAdapterClick listener){
        onAdapterClick = listener;
    }

    public interface OnAdapterClick{
        void onStatusDetailClick(int pos, StatusShortcut statusShortcut);
    }
}
