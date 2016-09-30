package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 30/09/2016.
 */

public class TimelineDiaryListAdapter extends RecyclerView.Adapter<TimelineDiaryListAdapter.ViewHolder>{

    private Context rootContext;

    public TimelineDiaryListAdapter(Context rootContext) {
        this.rootContext = rootContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_diary_trip, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvItemTimelineMonth)
        TextView tvItemTimelineMonth;
        @BindView(R.id.tvItemTimelineDay)
        TextView tvDay;
        @BindView(R.id.tvItemTimelineDate)
        TextView tvDate;
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
