package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.DiaryTripActivity;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.TripDiary;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 30/09/2016.
 */

public class DiaryTripShortcutListAdapter extends RecyclerView.Adapter<DiaryTripShortcutListAdapter.ViewHolder> {

    private Context rootContext;
    private List<TripDiary> lstTripDiaries;
    private String token;

    public DiaryTripShortcutListAdapter(Context rootContext, List<TripDiary> lstTripDiaries) {
        this.rootContext = rootContext;
        this.lstTripDiaries = lstTripDiaries;
        token = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_TOKEN, "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary_trip_shortcut, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TripDiary tripDiary = lstTripDiaries.get(position);
        holder.cvDiaryTripShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(rootContext, DiaryTripActivity.class);
                intent.putExtra(ApiConstants.KEY_ID_TRIP_DIARY, tripDiary.getId());
                rootContext.startActivity(intent);
            }
        });
        if (tripDiary.getImages() != null && !tripDiary.getImages().isEmpty()) {
            PicassoHelper.execPicasso(rootContext, tripDiary.getImages().get(0).getUrl(), holder.ivBackgroundDiaryShortcut);
        }
        StringUtil.setText(holder.tvNameDiaryShortcut, tripDiary.getTitle());
        StringUtil.setText(holder.tvAuthorDiaryShortcut, tripDiary.getOwner().getFirstName() + " " +
                tripDiary.getOwner().getLastName());
        StringUtil.setText(holder.tvDateDiaryShortcut, ConvertTimeHelper.convertISODateToPrettyTimeStamp(tripDiary.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return lstTripDiaries == null ? 0 : lstTripDiaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cvDiaryTripShortcut)
        CardView cvDiaryTripShortcut;
        @BindView(R.id.ivBackgroundDiaryShortcut)
        ImageView ivBackgroundDiaryShortcut;
        @BindView(R.id.tvNameDiaryShortcut)
        TextView tvNameDiaryShortcut;
        @BindView(R.id.tvAuthorDiaryShortcut)
        TextView tvAuthorDiaryShortcut;
        @BindView(R.id.tvDateDiaryShortcut)
        TextView tvDateDiaryShortcut;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
