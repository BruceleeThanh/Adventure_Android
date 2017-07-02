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
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.MessageFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 29/06/2017.
 */

public class UserOnlineListAdapter extends RecyclerView.Adapter<UserOnlineListAdapter.ViewHolder>{

    private Context rootContext = null;
    private List<User> lstUsers = null;
    private FragmentActivity fragmentActivity;

    public UserOnlineListAdapter(FragmentActivity fragmentActivity, Context context, List<User> lstUsers) {
        this.fragmentActivity = fragmentActivity;
        this.rootContext = context;
        this.lstUsers = lstUsers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_online, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PicassoHelper.execPicasso_ProfileImage(rootContext, lstUsers.get(position).getAvatar(), holder.civProfileImage);
        StringUtil.setText(holder.tvProfileName, lstUsers.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return lstUsers == null ? 0 : lstUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View rootView;

        @BindView(R.id.rlUserOnline)
        RelativeLayout rlUserOnline;
        @BindView(R.id.civProfileImage)
        CircleImageView civProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ButterKnife.bind(this, rootView);
        }

        @OnClick(R.id.rlUserOnline)
        protected void onUserOnlineClick(){
            FragmentController.addFragment_BackStack_Animation(fragmentActivity, R.id.rlMessageActivity, MessageFragment.newInstance(lstUsers.get(getAdapterPosition())));
        }
    }
}
