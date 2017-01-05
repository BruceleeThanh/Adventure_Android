package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.FloatProperty;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.activities.TripActivity;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;
import studio.crazybt.adventure.models.TripShortcut;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TripShortcutListAdapter extends RecyclerView.Adapter<TripShortcutListAdapter.ViewHolder> {

    private Context rootContext;
    private List<TripShortcut> listTripShortcutList;

    public TripShortcutListAdapter(Context context) {
        this.rootContext = context;
    }

    public TripShortcutListAdapter(Context context, List<TripShortcut> listTripShortcutList) {
        this.rootContext = context;
        this.listTripShortcutList = listTripShortcutList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_shortcut, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rlTripShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, TripActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.tvTripDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, TripActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.tvProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
        @BindDimen(R.dimen.item_icon_size_tiny)
        float itemSizeTiny;
        @BindDimen(R.dimen.five_star_icon_width)
        float fiveStarWidth;
        @BindDimen(R.dimen.five_star_icon_height)
        float fiveStarHeight;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            drawableProcessHelper = new DrawableProcessHelper(itemView);
            ButterKnife.bind(this, itemView);

            drawableProcessHelper.setTextViewDrawableFitSize(tvTripName, R.drawable.ic_signpost_96, itemSizeTiny, itemSizeTiny);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripStartPosition, R.drawable.ic_flag_filled_96, itemSizeTiny, itemSizeTiny);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripPeriod, R.drawable.ic_clock_96, itemSizeTiny, itemSizeTiny);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripDestination, R.drawable.ic_marker_96, itemSizeTiny, itemSizeTiny);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripMoney, R.drawable.ic_money_bag_96, itemSizeTiny, itemSizeTiny);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripMember, R.drawable.ic_user_96, itemSizeTiny, itemSizeTiny);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripJoiner, R.drawable.ic_airplane_take_off_96, itemSizeTiny, itemSizeTiny);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripInterested, R.drawable.ic_like_filled_96, itemSizeTiny, itemSizeTiny);
            drawableProcessHelper.setTextViewDrawableFitSize(tvTripRate, R.drawable.ic_five_star_96, fiveStarWidth, fiveStarHeight);
        }
    }
}