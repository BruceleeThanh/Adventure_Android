package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.GroupFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Group;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 22/06/2017.
 */

public class GroupShortcutInviteListAdapter extends RecyclerView.Adapter<GroupShortcutInviteListAdapter.ViewHolder> {

    private Context rootContext = null;
    private List<Group> lstGroups = null;
    private FragmentActivity fragmentActivity = null;
    private AdventureRequest adventureRequest;
    private AdventureRequest.OnNotifyResponseReceived onNotifyResponseReceived;

    public GroupShortcutInviteListAdapter(FragmentActivity fragmentActivity, Context context, List<Group> lstGroups) {
        this.fragmentActivity = fragmentActivity;
        this.rootContext = context;
        this.lstGroups = lstGroups;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_shortcut_cardview, parent, false);
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

    public void setOnNotifyResponseReceived(AdventureRequest.OnNotifyResponseReceived listener){
        this.onNotifyResponseReceived = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rlGroupShortcut)
        RelativeLayout rlGroupShortcut;
        @BindView(R.id.civCover)
        CircleImageView civCover;
        @BindView(R.id.tvNameGroup)
        TextView tvNameGroup;
        @BindView(R.id.ivRemoveInvite)
        ImageView ivRemoveInvite;

        private String token;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            token = SharedPref.getInstance(rootContext).getAccessToken();
        }

        @OnClick(R.id.ivRemoveInvite)
        protected void onRemoveInviteClick() {
            ivRemoveInvite.setEnabled(false);
            removeInviteRequest(getAdapterPosition());
        }

        private void removeInviteRequest(int index) {
            adventureRequest = new AdventureRequest(rootContext, Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_REMOVE_INVITE_GROUP_MEMBER), getRemoveInviteParams(index), false);
            getRemoveInviteResponse(index);
        }

        private Map<String, String> getRemoveInviteParams(int index) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiConstants.KEY_TOKEN, token);
            params.put(ApiConstants.KEY_ID_GROUP, lstGroups.get(index).getId());
            return params;
        }

        private void getRemoveInviteResponse(final int index) {
            adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
                @Override
                public void onAdventureResponse(JSONObject response) {
                    ToastUtil.showToast(rootContext,
                            rootContext.getResources().getString(R.string.success_remove_invite_member_group) + " " + lstGroups.get(index).getName());
                    lstGroups.remove(index);
                    notifyDataSetChanged();
                    ivRemoveInvite.setEnabled(true);
                    if (onNotifyResponseReceived != null) {
                        onNotifyResponseReceived.onNotify();
                    }
                }

                @Override
                public void onAdventureError(int errorCode, String errorMsg) {
                    ivRemoveInvite.setEnabled(true);
                    ToastUtil.showToast(rootContext, errorMsg);
                }
            });
        }
    }
}
