package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.activities.TripActivity;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;
import studio.crazybt.adventure.models.TripShortcut;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TripShortcutAdapter extends RecyclerView.Adapter<TripShortcutAdapter.ViewHolder> {

    private Context rootContext;
    private List<TripShortcut> listTripShortcutList;

    public TripShortcutAdapter(Context context) {
        this.rootContext = context;
    }

    public TripShortcutAdapter(Context context, List<TripShortcut> listTripShortcutList) {
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
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        private DrawableProcessHelper drawableProcessHelper;

        public ImageView ivProfileImage;
        public TextView tvProfileName;
        public TextView tvTimeUpload;
        public TextView tvTripName;
        public TextView tvTripStartPosition;
        public TextView tvTripPeriod;
        public TextView tvTripDestination;
        public TextView tvTripMoney;
        public TextView tvTripMember;
        public TextView tvTripJoiner;
        public TextView tvTripInterested;
        public TextView tvTripRate;
        public TextView tvTripDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            drawableProcessHelper = new DrawableProcessHelper(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvProfileName = (TextView) itemView.findViewById(R.id.tvProfileName);
            tvTimeUpload = (TextView) itemView.findViewById(R.id.tvTimeUpload);
            tvTripName = (TextView) itemView.findViewById(R.id.tvTripName);
            tvTripStartPosition = (TextView) itemView.findViewById(R.id.tvTripStartPosition);
            tvTripPeriod = (TextView) itemView.findViewById(R.id.tvTripPeriod);
            tvTripDestination = (TextView) itemView.findViewById(R.id.tvTripDestination);
            tvTripMoney = (TextView) itemView.findViewById(R.id.tvTripMoney);
            tvTripMember = (TextView) itemView.findViewById(R.id.tvTripMember);
            tvTripJoiner = (TextView) itemView.findViewById(R.id.tvTripJoiner);
            tvTripInterested = (TextView) itemView.findViewById(R.id.tvTripInterested);
            tvTripRate = (TextView) itemView.findViewById(R.id.tvTripRate);
            tvTripDetail = (TextView) itemView.findViewById(R.id.tvTripDetail);

            double itemSizeSmall = Double.parseDouble(itemView.getResources().getString(R.string.item_icon_size_small));
            double fiveStarWidth = Double.parseDouble(itemView.getResources().getString(R.string.five_star_icon_width));
            double fiveStarHeight = Double.parseDouble(itemView.getResources().getString(R.string.five_star_icon_height));

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
}