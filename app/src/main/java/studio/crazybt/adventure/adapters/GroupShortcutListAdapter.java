package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.GroupFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.models.Group;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 23/05/2017.
 */

public class GroupShortcutListAdapter extends RecyclerView.Adapter<GroupShortcutListAdapter.ViewHolder> {

    private Context rootContext = null;
    private List<Group> lstGroups = null;
    private FragmentActivity fragmentActivity = null;

    public GroupShortcutListAdapter(FragmentActivity fragmentActivity, Context context, List<Group> lstGroups) {
        this.fragmentActivity = fragmentActivity;
        this.rootContext = context;
        this.lstGroups = lstGroups;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_shortcut, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Group group = lstGroups.get(position);
        PicassoHelper.execPicasso_CoverImage(rootContext, group.getCover(), holder.civCover);
        StringUtil.setText(holder.tvNameGroup, group.getName());
        holder.rlGroupShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentActivity.invalidateOptionsMenu();
                FragmentController.addFragment_BackStack_Animation(fragmentActivity, R.id.rlGroup, GroupFragment.newInstance(group.getId(), group.getName(), group.getCover()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstGroups == null ? 0 : lstGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.rlGroupShortcut)
        RelativeLayout rlGroupShortcut;
        @BindView(R.id.civCover)
        CircleImageView civCover;
        @BindView(R.id.tvNameGroup)
        TextView tvNameGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
