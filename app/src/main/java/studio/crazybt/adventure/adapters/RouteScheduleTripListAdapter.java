package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.models.Route;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 30/09/2016.
 */

public class RouteScheduleTripListAdapter extends RecyclerView.Adapter<RouteScheduleTripListAdapter.ViewHolder> {

    private Context rootContext;
    private List<Route> lstRoute;

    public RouteScheduleTripListAdapter(Context rootContext, List<Route> lstRoute) {
        this.rootContext = rootContext;
        this.lstRoute = lstRoute;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_schedule_trip, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Route route = lstRoute.get(position);
        StringUtil.setText(holder.tvPeriodRoute, ConvertTimeHelper.convertISODateToString(route.getStartAt()) + " - " +
                ConvertTimeHelper.convertISODateToString(route.getEndAt()));
        StringUtil.setText(holder.tvTitleRoute, route.getTitle());
        StringUtil.setText(holder.tvContentRoute, route.getContent());
    }

    @Override
    public int getItemCount() {
        return lstRoute.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPeriodRoute)
        TextView tvPeriodRoute;
        @BindView(R.id.tvTitleRoute)
        TextView tvTitleRoute;
        @BindView(R.id.tvContentRoute)
        TextView tvContentRoute;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
