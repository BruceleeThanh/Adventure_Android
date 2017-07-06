package studio.crazybt.adventure.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.one.EmojiOneProvider;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.activities.TripActivity;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.listeners.OnLoadMoreListener;
import studio.crazybt.adventure.models.Status;
import studio.crazybt.adventure.models.Trip;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 30/05/2017.
 */

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context rootContext;
    private List<Object> lstPosts = null;
    private String idUser = null;
    private Dialog dialog;

    private final int STATUS = 0;
    private final int TRIP = 1;
    private final int LOAD_MORE = 3;

    public PostListAdapter(Context context) {
        this.rootContext = context;
    }

    public PostListAdapter(Context rootContext, List<Object> lstPosts) {
        this.rootContext = rootContext;
        this.lstPosts = lstPosts;
        idUser = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_ID, null);
    }

    @Override
    public int getItemViewType(int position) {
        if (lstPosts.get(position) instanceof Status)
            return STATUS;
        else if (lstPosts.get(position) instanceof Trip)
            return TRIP;
        else if (lstPosts.get(position) == null)
            return LOAD_MORE;
        else
            return -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EmojiManager.install(new EmojiOneProvider());
        LayoutInflater li = LayoutInflater.from(rootContext);
        switch (viewType) {
            case STATUS:
                View viewStatus = li.inflate(R.layout.item_status_shortcut, parent, false);
                return new StatusViewHolder(viewStatus);
            case TRIP:
                View viewTrip = li.inflate(R.layout.item_trip_shortcut_2, parent, false);
                return new TripNormalViewHolder(viewTrip);
            case LOAD_MORE:
                View viewLoadMore = li.inflate(R.layout.item_load_more, parent, false);
                return new LoadingViewHolder(viewLoadMore);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == STATUS) {
            final StatusViewHolder statusViewHolder = (StatusViewHolder) holder;
            final Status statusItem = (Status) lstPosts.get(position);

            // User avatar
            PicassoHelper.execPicasso_ProfileImage(rootContext, statusItem.getUser().getAvatar(), statusViewHolder.ivProfileImage);
            statusViewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rootContext.startActivity(ProfileActivity.newInstance(rootContext, statusItem.getUser().getId(), statusItem.getUser().getFullName()));
                }
            });

            // User name
            statusViewHolder.tvProfileName.setText(statusItem.getUser().getFullName());
            statusViewHolder.tvProfileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rootContext.startActivity(ProfileActivity.newInstance(rootContext, statusItem.getUser().getId(), statusItem.getUser().getFullName()));
                }
            });

            // Permission (Status Privacy)
            if (statusItem.getPermission() == 1) {
                statusViewHolder.ivPermission.setImageResource(R.drawable.ic_private_96);
            } else if (statusItem.getPermission() == 2) {
                statusViewHolder.ivPermission.setImageResource(R.drawable.ic_friend_96);
            } else if (statusItem.getPermission() == 3) {
                statusViewHolder.ivPermission.setImageResource(R.drawable.ic_public_96);
            } else {
                statusViewHolder.ivPermission.setVisibility(View.GONE);
            }

            // Options
            if (statusItem.getUser().getId().equals(idUser)) {
                statusViewHolder.ivOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] values = new int[2];
                        v.getLocationOnScreen(values);
                        loadOptionStatus(true, rootContext, statusItem, values[0], values[1]);
                    }
                });
            } else {
                statusViewHolder.ivOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] values = new int[2];
                        v.getLocationOnScreen(values);
                        loadOptionStatus(false, rootContext, statusItem, values[0], values[1]);
                    }
                });
            }

            // Status create time
            StringUtil.setText(statusViewHolder.tvTimeUpload, ConvertTimeHelper.convertISODateToPrettyTimeStamp(statusItem.getCreatedAt()));

            // Remove content status if not exits
            StringUtil.setText_Gone(statusViewHolder.etvContentStatus, statusItem.getContent());

            // Content status
            statusViewHolder.etvContentStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rootContext.startActivity(StatusActivity.newInstance(rootContext, CommonConstants.ACT_STATUS_DETAIL, statusItem));
                }
            });

            // Click to see detail Images
            statusViewHolder.llImageStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAdapterClick != null) onAdapterClick.onStatusDetailClick(position);
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
                    PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                    statusViewHolder.llImageStatusDown.setVisibility(View.GONE);
                    statusViewHolder.ivUpItem2.setVisibility(View.GONE);
                } else {
                    statusViewHolder.ivUpItem2.setVisibility(View.VISIBLE);
                    if (countImage == 2) {
                        PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                        PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                        statusViewHolder.llImageStatusDown.setVisibility(View.GONE);
                    } else {
                        statusViewHolder.llImageStatusDown.setVisibility(View.VISIBLE);
                        statusViewHolder.ivDownItem1.setVisibility(View.VISIBLE);
                        if (countImage == 3) {
                            PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                            PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                            PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(2).getUrl(), statusViewHolder.ivDownItem1);
                            statusViewHolder.ivDownItem2.setVisibility(View.GONE);
                            statusViewHolder.rlDownItem3.setVisibility(View.GONE);
                        } else {
                            statusViewHolder.ivDownItem2.setVisibility(View.VISIBLE);
                            if (countImage == 4) {
                                PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                                PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                                PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(2).getUrl(), statusViewHolder.ivDownItem1);
                                PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(3).getUrl(), statusViewHolder.ivDownItem2);
                                statusViewHolder.rlDownItem3.setVisibility(View.GONE);
                            } else {
                                statusViewHolder.rlDownItem3.setVisibility(View.VISIBLE);
                                statusViewHolder.ivDownItem3.setVisibility(View.VISIBLE);
                                if (countImage == 5) {
                                    statusViewHolder.tvDownItem3.setVisibility(View.GONE);
                                    PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                                    PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                                    PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(2).getUrl(), statusViewHolder.ivDownItem1);
                                    PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(3).getUrl(), statusViewHolder.ivDownItem2);
                                    PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(4).getUrl(), statusViewHolder.ivDownItem3);
                                } else {
                                    if (countImage > 5) {
                                        PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(0).getUrl(), statusViewHolder.ivUpItem1);
                                        PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(1).getUrl(), statusViewHolder.ivUpItem2);
                                        PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(2).getUrl(), statusViewHolder.ivDownItem1);
                                        PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(3).getUrl(), statusViewHolder.ivDownItem2);
                                        PicassoHelper.execPicasso(rootContext, statusItem.getImageContents().get(4).getUrl(), statusViewHolder.ivDownItem3);
                                        statusViewHolder.tvDownItem3.setVisibility(View.VISIBLE);
                                        StringUtil.setText(statusViewHolder.tvDownItem3, "+" + (countImage - 5));
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
                    rootContext.startActivity(StatusActivity.newInstance(rootContext, CommonConstants.ACT_STATUS_LIKE, statusItem));
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
                    Map<String, String> params = new HashMap<>();
                    params.put(ApiConstants.KEY_TOKEN, token);
                    params.put(ApiConstants.KEY_ID_STATUS, statusItem.getId());
                    CustomRequest customRequest = new CustomRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_LIKE_STATUS), params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                                JSONObject status = JsonUtil.getJSONObject(data, ApiConstants.KEY_STATUS);
                                int amountLike = JsonUtil.getInt(status, ApiConstants.KEY_AMOUNT_LIKE, -1);
                                int isLike = JsonUtil.getInt(data, ApiConstants.KEY_IS_LIKE, -1);
                                if (amountLike != -1) {
                                    ((Status) lstPosts.get(position)).setAmountLike(amountLike);
                                }
                                if (isLike != -1) {
                                    ((Status) lstPosts.get(position)).setIsLike(isLike);
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
                    rootContext.startActivity(StatusActivity.newInstance(rootContext, CommonConstants.ACT_STATUS_COMMENT, statusItem));
                }
            });

            // Comment highlight
            if (statusItem.getIsComment() == 1) {
                Drawable drawable = ContextCompat.getDrawable(rootContext, R.drawable.ic_chat_bubble_green_24dp);
                statusViewHolder.tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                statusViewHolder.tvComment.setTextColor(rootContext.getResources().getColor(R.color.primary));
            } else {
                Drawable drawable = ContextCompat.getDrawable(rootContext, R.drawable.ic_chat_bubble_gray_24dp);
                statusViewHolder.tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                statusViewHolder.tvComment.setTextColor(rootContext.getResources().getColor(R.color.secondary_text));
            }

            // Click comment detail
            statusViewHolder.tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rootContext.startActivity(StatusActivity.newInstance(rootContext, CommonConstants.ACT_STATUS_COMMENT, statusItem));
                }
            });
        } else if (getItemViewType(position) == TRIP) {
            Date now = new Date();
            TripNormalViewHolder tripNormalViewHolder = (TripNormalViewHolder) holder;
            final Trip trip = (Trip) lstPosts.get(position);
            PicassoHelper.execPicasso_ProfileImage(rootContext, trip.getOwner().getAvatar(), tripNormalViewHolder.ivProfileImage);
            tripNormalViewHolder.tvProfileName.setText(trip.getOwner().getFullName());
            tripNormalViewHolder.tvTimeUpload.setText(ConvertTimeHelper.convertISODateToPrettyTimeStamp(trip.getCreatedAt()));

            // Permission (Trip Privacy)
            if (trip.getPermission() == 1) {
                tripNormalViewHolder.ivPermission.setImageResource(R.drawable.ic_private_96);
            } else if (trip.getPermission() == 2) {
                tripNormalViewHolder.ivPermission.setImageResource(R.drawable.ic_friend_96);
            } else if (trip.getPermission() == 3) {
                tripNormalViewHolder.ivPermission.setImageResource(R.drawable.ic_public_96);
            } else {
                tripNormalViewHolder.ivPermission.setVisibility(View.GONE);
            }

            // Start at, end at
            Date startTime = ConvertTimeHelper.convertISODateToDate(trip.getStartAt());
            Date endTime = ConvertTimeHelper.convertISODateToDate(trip.getEndAt());

            // Time label
            if (startTime.compareTo(now) > 0) {
                tripNormalViewHolder.tvTripPeriod.setTextColor(rootContext.getResources().getColor(R.color.greed_label));
            } else if (endTime.compareTo(now) > 0) {
                tripNormalViewHolder.tvTripPeriod.setTextColor(rootContext.getResources().getColor(R.color.yellow_label));
            } else {
                tripNormalViewHolder.tvTripPeriod.setTextColor(rootContext.getResources().getColor(R.color.red_label));
            }

            tripNormalViewHolder.tvTripName.setText(trip.getName());

            tripNormalViewHolder.tvTripStartPosition.setText(trip.getStartPosition());

            tripNormalViewHolder.tvTripPeriod.setText(ConvertTimeHelper.convertISODateToString(trip.getStartAt(), ConvertTimeHelper.DATE_FORMAT_1) + " - " +
                    ConvertTimeHelper.convertISODateToString(trip.getEndAt(), ConvertTimeHelper.DATE_FORMAT_1));

            tripNormalViewHolder.tvTripDestination.setText(trip.getDestinationSummary());

            tripNormalViewHolder.tvTripMoney.setText(trip.getExpense());

            String tripPeople = String.valueOf(trip.getAmountPeople()) + " " +
                    rootContext.getResources().getString(R.string.unit_people_tv_trip_shortcut);
            tripNormalViewHolder.tvTripPeople.setText(tripPeople);

            tripNormalViewHolder.tvTripMember.setText(String.valueOf(trip.getAmountMember()));

            tripNormalViewHolder.tvTripInterested.setText(String.valueOf(trip.getAmountInterested()));

            tripNormalViewHolder.tvTripRate.setText(trip.getAmountRating() + "/" + trip.getAmountMember());
            tripNormalViewHolder.drawableHelper.setTextViewRatingDrawable(tripNormalViewHolder.tvTripRate, trip.getRating(),
                    tripNormalViewHolder.fiveStarWidth, tripNormalViewHolder.fiveStarHeight);

            tripNormalViewHolder.rlContentTripShortcut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(rootContext, TripActivity.class);
                    intent.putExtra(ApiConstants.KEY_ID_TRIP, trip.getId());
                    intent.putExtra(ApiConstants.KEY_OWNER, trip.getOwner().getId());
                    intent.putExtra(ApiConstants.KEY_NAME, trip.getName());
                    rootContext.startActivity(intent);
                }
            });
            tripNormalViewHolder.tvProfileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rootContext.startActivity(ProfileActivity.newInstance(rootContext, trip.getOwner().getId(), trip.getOwner().getFullName()));
                }
            });
            tripNormalViewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rootContext.startActivity(ProfileActivity.newInstance(rootContext, trip.getOwner().getId(), trip.getOwner().getFullName()));
                }
            });
        } else if (getItemViewType(position) == LOAD_MORE) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.pbLoadMore.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return lstPosts == null ? 0 : lstPosts.size();
    }

    private void loadOptionStatus(boolean isOwner, Context context, Status status, int x, int y) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_option_status);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = x;
        wmlp.y = y;
        TextView tvCopyStatus = (TextView) dialog.findViewById(R.id.tvCopyStatus);
        tvCopyStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView tvEditStatus = (TextView) dialog.findViewById(R.id.tvEditStatus);
        tvEditStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView tvDeleteStatus = (TextView) dialog.findViewById(R.id.tvDeleteStatus);
        tvDeleteStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (!isOwner) {
            tvEditStatus.setVisibility(View.GONE);
            tvDeleteStatus.setVisibility(View.GONE);
        }
        dialog.show();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.ivOptions)
        ImageView ivOptions;
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

        public StatusViewHolder(View itemView) {
            super(itemView);
            EmojiManager.install(new EmojiOneProvider());
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            drawableHelper = new DrawableHelper(itemView.getContext());
            drawableHelper.setTextViewDrawableFitSize(tvCountLike, R.drawable.ic_thumb_up_96, itemSizeSmall, itemSizeSmall);
            drawableHelper.setTextViewDrawableFitSize(tvCountComment, R.drawable.ic_chat_96, itemSizeSmall, itemSizeSmall);
        }
    }

    public class TripNormalViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public DrawableHelper drawableHelper;

        @BindView(R.id.rlContentTripShortcut)
        RelativeLayout rlContentTripShortcut;
        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.tvTimeUpload)
        TextView tvTimeUpload;
        @BindView(R.id.ivPermission)
        ImageView ivPermission;
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
        @BindView(R.id.tvTripPeople)
        TextView tvTripPeople;
        @BindView(R.id.tvTripMember)
        TextView tvTripMember;
        @BindView(R.id.tvTripInterested)
        TextView tvTripInterested;
        @BindView(R.id.tvTripRate)
        TextView tvTripRate;
        @BindDimen(R.dimen.item_icon_size_tiny)
        float itemSizeTiny;
        @BindDimen(R.dimen.five_star_icon_width)
        float fiveStarWidth;
        @BindDimen(R.dimen.five_star_icon_height)
        float fiveStarHeight;

        public TripNormalViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            drawableHelper = new DrawableHelper(itemView.getContext());
            ButterKnife.bind(this, itemView);

            drawableHelper.setTextViewDrawableFitSize(tvTripName, R.drawable.ic_signpost_96, itemSizeTiny, itemSizeTiny);
            drawableHelper.setTextViewDrawableFitSize(tvTripStartPosition, R.drawable.ic_flag_filled_96, itemSizeTiny, itemSizeTiny);
            drawableHelper.setTextViewDrawableFitSize(tvTripPeriod, R.drawable.ic_clock_96, itemSizeTiny, itemSizeTiny);
            drawableHelper.setTextViewDrawableFitSize(tvTripDestination, R.drawable.ic_marker_normal_red_96, itemSizeTiny, itemSizeTiny);
            drawableHelper.setTextViewDrawableFitSize(tvTripMoney, R.drawable.ic_money_bag_96, itemSizeTiny, itemSizeTiny);
            drawableHelper.setTextViewDrawableFitSize(tvTripPeople, R.drawable.ic_user_96, itemSizeTiny, itemSizeTiny);
            drawableHelper.setTextViewDrawableFitSize(tvTripMember, R.drawable.ic_airplane_take_off_96, itemSizeTiny, itemSizeTiny);
            drawableHelper.setTextViewDrawableFitSize(tvTripInterested, R.drawable.ic_like_filled_96, itemSizeTiny, itemSizeTiny);
            drawableHelper.setTextViewDrawableFitSize(tvTripRate, R.drawable.ic_five_star_96, fiveStarWidth, fiveStarHeight);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public View itemView;

        @BindView(R.id.pbLoadMore)
        ProgressBar pbLoadMore;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private OnAdapterClick onAdapterClick;
    public OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        onLoadMoreListener = listener;
    }

    public void setOnAdapterClickListener(OnAdapterClick listener) {
        onAdapterClick = listener;
    }

    public interface OnAdapterClick {
        void onStatusDetailClick(int pos);
    }
}
