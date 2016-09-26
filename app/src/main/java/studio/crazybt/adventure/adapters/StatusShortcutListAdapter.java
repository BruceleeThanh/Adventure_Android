package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.activities.TripActivity;
import studio.crazybt.adventure.fragments.CommentStatusFragment;
import studio.crazybt.adventure.fragments.StatusDetailFragment;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;

/**
 * Created by Brucelee Thanh on 24/09/2016.
 */

public class StatusShortcutListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context rootContext;
    private FragmentController fragmentController;

    public static final int STATUS = 0;
    public static final int TRIP = 1;

    public StatusShortcutListAdapter(Context context) {
        this.rootContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position == 2 || position == 4 || position == 6 | position == 8){
            return STATUS;
        }else{
            return TRIP;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(rootContext);
        switch (viewType){
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case STATUS:
                final StatusViewHolder statusViewHolder = (StatusViewHolder) holder;
                statusViewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, ProfileActivity.class);
                        rootContext.startActivity(intent);
                    }
                });
                statusViewHolder.tvProfileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootContext, ProfileActivity.class);
                        rootContext.startActivity(intent);
                    }
                });
                statusViewHolder.tvContentStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((AppCompatActivity)rootContext).invalidateOptionsMenu();
                        fragmentController = new FragmentController((AppCompatActivity) rootContext);
                        fragmentController.setCustomAnimations();
                        fragmentController.addFragment_BackStack(R.id.rlContentHomePage, new StatusDetailFragment());
                        fragmentController.commit();
                    }
                });
                statusViewHolder.llImageStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((AppCompatActivity)rootContext).invalidateOptionsMenu();
                        fragmentController = new FragmentController((AppCompatActivity) rootContext);
                        fragmentController.setCustomAnimations();
                        fragmentController.addFragment_BackStack(R.id.rlContentHomePage, new StatusDetailFragment());
                        fragmentController.commit();
                    }
                });
                statusViewHolder.llLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (statusViewHolder.cbLike.isChecked()) {
                            statusViewHolder.cbLike.setChecked(false);
                            statusViewHolder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.secondary_text));
                            int countLike = Integer.parseInt(statusViewHolder.tvCountLike.getText().toString());
                            statusViewHolder.tvCountLike.setText(String.valueOf(countLike - 1));
                        }else{
                            statusViewHolder.cbLike.setChecked(true);
                            statusViewHolder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.primary));
                            int countLike = Integer.parseInt(statusViewHolder.tvCountLike.getText().toString());
                            statusViewHolder.tvCountLike.setText(String.valueOf(countLike + 1));
                        }
                    }
                });
                statusViewHolder.tvCountComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragmentController = new FragmentController((AppCompatActivity) rootContext);
                        fragmentController.setCustomAnimations();
                        fragmentController.addFragment_BackStack(R.id.rlContentHomePage, new CommentStatusFragment());
                        fragmentController.commit();
                    }
                });
                statusViewHolder.tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragmentController = new FragmentController((AppCompatActivity) rootContext);
                        fragmentController.setCustomAnimations();
                        fragmentController.addFragment_BackStack(R.id.rlContentHomePage, new CommentStatusFragment());
                        fragmentController.commit();
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
        return 10;
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public DrawableProcessHelper drawableProcessHelper;

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.tvContentStatus)
        TextView tvContentStatus;
        @BindView(R.id.llImageStatus)
        LinearLayout llImageStatus;
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
}
