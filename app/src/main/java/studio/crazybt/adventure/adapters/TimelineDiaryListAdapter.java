package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.models.DetailDiary;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 30/09/2016.
 */

public class TimelineDiaryListAdapter extends RecyclerView.Adapter<TimelineDiaryListAdapter.ViewHolder> {

    private Context rootContext;
    private List<DetailDiary> lstDetailDiaries;


    public TimelineDiaryListAdapter(Context rootContext, List<DetailDiary> lstDetailDiaries) {
        this.rootContext = rootContext;
        this.lstDetailDiaries = lstDetailDiaries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_diary_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailDiary detailDiary = lstDetailDiaries.get(position);
        Calendar calendar =  ConvertTimeHelper.convertISODateToCalendar(detailDiary.getDate());
        StringUtil.setText(holder.tvItemTimelineMonth, "T" + (calendar.get(Calendar.MONTH) + 1) + "\n" + calendar.get(Calendar.YEAR));
        StringUtil.setText(holder.tvItemTimelineDay, calendar.get(Calendar.DAY_OF_MONTH) + "");
        StringUtil.setText(holder.tvItemTimelineDate, ConvertTimeHelper.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
        holder.tvItemTimelineTitle.setText(detailDiary.getTitle());
        holder.tvItemTimelineContent.setText(detailDiary.getContent());
    }

    @Override
    public int getItemCount() {
        return lstDetailDiaries == null ? 0 : lstDetailDiaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvItemTimelineMonth)
        TextView tvItemTimelineMonth;
        @BindView(R.id.tvItemTimelineDay)
        TextView tvItemTimelineDay;
        @BindView(R.id.tvItemTimelineDate)
        TextView tvItemTimelineDate;
        @BindView(R.id.tvItemTimelineTitle)
        TextView tvItemTimelineTitle;
        @BindView(R.id.tvItemTimelineContent)
        TextView tvItemTimelineContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
