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

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.DiaryTripActivity;

/**
 * Created by Brucelee Thanh on 30/09/2016.
 */

public class DiaryTripShortcutListAdapter extends RecyclerView.Adapter<DiaryTripShortcutListAdapter.ViewHolder> {

    private Context rootContext;

    public DiaryTripShortcutListAdapter(Context rootContext) {
        this.rootContext = rootContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary_trip_shortcut, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cvDiaryTripShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootContext.startActivity(new Intent(rootContext, DiaryTripActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
