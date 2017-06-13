package studio.crazybt.adventure.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.GroupMember;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 09/06/2017.
 */

public class GroupBlockMemberListAdapter extends RecyclerView.Adapter<GroupBlockMemberListAdapter.ViewHolder> {

    private Context rootContext = null;
    private List<GroupMember> lstGroupBlockMembers = null;
    private boolean isGroupAdmin = false;

    public GroupBlockMemberListAdapter(Context rootContext, List<GroupMember> lstGroupBlockMembers, boolean isGroupAdmin) {
        this.rootContext = rootContext;
        this.lstGroupBlockMembers = lstGroupBlockMembers;
        this.isGroupAdmin = isGroupAdmin;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupMember groupMember = lstGroupBlockMembers.get(position);
        PicassoHelper.execPicasso_ProfileImage(rootContext, groupMember.getOwner().getAvatar(), holder.ivProfileImage);
        StringUtil.setText(holder.tvProfileName, groupMember.getOwner().getFirstName() + " " + groupMember.getOwner().getLastName());
        StringUtil.setText(holder.tvJoinedAt, rootContext.getResources().getString(R.string.label_block_member_group_at) + " " + groupMember.getShortCreatedAt());
        holder.ivGroupMemberOptions.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return lstGroupBlockMembers == null ? 0 : lstGroupBlockMembers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdventureRequest adventureRequest = null;
        private String token = null;

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.tvJoinedAt)
        TextView tvJoinedAt;
        @BindView(R.id.ivGroupMemberOptions)
        ImageView ivGroupMemberOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            token = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_TOKEN, null);
        }

        @OnClick(R.id.ivGroupMemberOptions)
        void onIvGroupMemberOptions(View v) {
            int[] values = new int[2];
            v.getLocationOnScreen(values);
            loadOptionBlockMemberGroup(rootContext, values[0], values[1]);
        }

        private void loadOptionBlockMemberGroup(Context context, int x, int y) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_option_block_member_group);
            dialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.LEFT;
            wmlp.x = x;
            wmlp.y = y;
            TextView tvUnblock = (TextView) dialog.findViewById(R.id.tvUnblock);
            tvUnblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unblockMember(getAdapterPosition());
                    dialog.cancel();
                }
            });

            dialog.show();
        }

        //region Block member
        private void unblockMember(int index) {
            adventureRequest = new AdventureRequest(rootContext, Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_UNBLOCK_MEMBER_GROUP_MEMBER), getUnblockMemberParams(index), false);
            getUnblockMemberResponse(index);
        }

        private Map<String, String> getUnblockMemberParams(int index) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiConstants.KEY_TOKEN, token);
            params.put(ApiConstants.KEY_ID_GROUP_MEMBER, lstGroupBlockMembers.get(index).getId());
            return params;
        }

        private void getUnblockMemberResponse(final int index) {
            adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
                @Override
                public void onAdventureResponse(JSONObject response) {
                    ToastUtil.showToast(rootContext, lstGroupBlockMembers.get(index).getOwner().getFirstName() + " " +
                            lstGroupBlockMembers.get(index).getOwner().getLastName() + " " + rootContext.getResources().getString(R.string.success_unblock_member_group));
                    lstGroupBlockMembers.remove(index);
                    notifyDataSetChanged();
                }

                @Override
                public void onAdventureError(int errorCode, String errorMsg) {
                    ToastUtil.showToast(rootContext, errorMsg);
                }
            });
        }
        //endregion
    }
}
