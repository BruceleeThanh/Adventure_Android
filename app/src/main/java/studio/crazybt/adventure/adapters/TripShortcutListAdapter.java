package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.activities.TripActivity;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.listeners.OnLoadMoreListener;
import studio.crazybt.adventure.models.Trip;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TripShortcutListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context rootContext;
    private List<Trip> lstTrip;

    private final int TRIP_NORMAL = 0;
    private final int TRIP_IMAGE = 1;
    private final int LOAD_MORE = 3;

    public TripShortcutListAdapter(Context context) {
        this.rootContext = context;
    }

    public TripShortcutListAdapter(Context context, List<Trip> lstTrip) {
        this.rootContext = context;
        this.lstTrip = lstTrip;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(rootContext);
        if (viewType == TRIP_NORMAL) {
            View viewTripNormal = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_shortcut_2, parent, false);
            return new TripNormalViewHolder(viewTripNormal);
        } else if (viewType == LOAD_MORE) {
            View viewLoadMore = li.inflate(R.layout.item_load_more, parent, false);
            return new LoadingViewHolder(viewLoadMore);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Trip temp = lstTrip.get(position);
        if (temp == null) {
            return LOAD_MORE;
        } else return TRIP_NORMAL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TRIP_NORMAL) {
            TripNormalViewHolder tripNormalViewHolder = (TripNormalViewHolder) holder;
            Trip trip = lstTrip.get(position);
            tripNormalViewHolder.tvProfileName.setText(trip.getUser().getFirstName() + " " + trip.getUser().getLastName());
            tripNormalViewHolder.tvTimeUpload.setText(ConvertTimeHelper.convertISODateToPrettyTimeStamp(trip.getCreatedAt()));

            // Permission (Trip Privacy)
            if (trip.getPermission() == 1) {
                tripNormalViewHolder.ivPermission.setImageResource(R.drawable.ic_private_96);
            } else if (trip.getPermission() == 2) {
                tripNormalViewHolder.ivPermission.setImageResource(R.drawable.ic_friend_96);
            } else if (trip.getPermission() == 3) {
                tripNormalViewHolder.ivPermission.setImageResource(R.drawable.ic_public_96);
            }
            tripNormalViewHolder.tvTripName.setText(trip.getName());

            tripNormalViewHolder.tvTripStartPosition.setText(trip.getStartPosition());

            tripNormalViewHolder.tvTripPeriod.setText(ConvertTimeHelper.convertISODateToString(trip.getStartAt()) + " - " +
                    ConvertTimeHelper.convertISODateToString(trip.getEndAt()));

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
                    rootContext.startActivity(intent);
                }
            });
            tripNormalViewHolder.tvTripDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(rootContext, TripActivity.class);
                    rootContext.startActivity(intent);
                }
            });
            tripNormalViewHolder.tvProfileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(rootContext, ProfileActivity.class);
                    rootContext.startActivity(intent);
                }
            });
            tripNormalViewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(rootContext, ProfileActivity.class);
                    rootContext.startActivity(intent);
                }
            });
        } else if (viewType == LOAD_MORE) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.pbLoadMore.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return lstTrip == null ? 0 : lstTrip.size();
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
        @BindView(R.id.tvTripDetail)
        TextView tvTripDetail;
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
            drawableHelper.setTextViewDrawableFitSize(tvTripDestination, R.drawable.ic_marker_96, itemSizeTiny, itemSizeTiny);
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

    public OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        onLoadMoreListener = listener;
    }
}