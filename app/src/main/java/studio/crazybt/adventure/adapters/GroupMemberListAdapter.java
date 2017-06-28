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
 * Created by Brucelee Thanh on 05/06/2017.
 */

public class GroupMemberListAdapter extends RecyclerView.Adapter<GroupMemberListAdapter.ViewHolder>{

    private Context rootContext = null;
    private List<GroupMember> lstGroupMembers = null;
    private boolean isGroupAdmin = false;

    public GroupMemberListAdapter(Context rootContext, List<GroupMember> lstGroupMembers, boolean isGroupAdmin) {
        this.rootContext = rootContext;
        this.lstGroupMembers = lstGroupMembers;
        this.isGroupAdmin = isGroupAdmin;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupMember groupMember = lstGroupMembers.get(position);
        PicassoHelper.execPicasso_ProfileImage(rootContext, groupMember.getOwner().getAvatar(), holder.ivProfileImage);
        StringUtil.setText(holder.tvProfileName, groupMember.getOwner().getFullName());
        if(groupMember.getPermission() == CommonConstants.VAL_PER_GROUP_CREATOR){
            StringUtil.setText(holder.tvJoinedAt, rootContext.getResources().getString(R.string.label_created_group_at) + " " + groupMember.getShortCreatedAt());
        }else{
            StringUtil.setText(holder.tvJoinedAt, rootContext.getResources().getString(R.string.label_joined_group_at) + " " + groupMember.getShortCreatedAt());
        }
        if(isGroupAdmin){
            holder.ivGroupMemberOptions.setVisibility(View.VISIBLE);
        }else{
            holder.ivGroupMemberOptions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lstGroupMembers == null ? 0 : lstGroupMembers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
        void onIvGroupMemberOptions(View v){
            int[] values = new int[2];
            v.getLocationOnScreen(values);
            loadOptionMemberGroup(rootContext, values[0], values[1]);
        }

        private void loadOptionMemberGroup(Context context, int x, int y) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_option_member_group);
            dialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.LEFT;
            wmlp.x = x;
            wmlp.y = y;
            TextView tvMakeAdmin = (TextView) dialog.findViewById(R.id.tvMakeAdmin);
            tvMakeAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeAdmin(getAdapterPosition());
                    dialog.cancel();
                }
            });
            TextView tvRemoveMember = (TextView) dialog.findViewById(R.id.tvRemoveMember);
            tvRemoveMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeMember(getAdapterPosition());
                    dialog.cancel();
                }
            });
            TextView tvBlockMember = (TextView) dialog.findViewById(R.id.tvBlockMember);
            tvBlockMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blockMember(getAdapterPosition());
                    dialog.cancel();
                }
            });

            dialog.show();
        }

        //region Make admin in group
        private void makeAdmin(int index){
            adventureRequest = new AdventureRequest(rootContext, Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_MAKE_ADMIN_GROUP_MEMBER), getMakeAdminParams(index), false);
            getMakeAdminResponse(index);
        }

        private Map<String ,String > getMakeAdminParams(int index){
            Map<String , String > params = new HashMap<>();
            params.put(ApiConstants.KEY_TOKEN, token);
            params.put(ApiConstants.KEY_ID_GROUP_MEMBER, lstGroupMembers.get(index).getId());
            return params;
        }

        private void getMakeAdminResponse(final int index){
            adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
                @Override
                public void onAdventureResponse(JSONObject response) {
                    ToastUtil.showToast(rootContext, lstGroupMembers.get(index).getOwner().getFullName() + " " + rootContext.getResources().getString(R.string.success_make_admin_group));
                    lstGroupMembers.remove(index);
                    notifyDataSetChanged();
                }

                @Override
                public void onAdventureError(int errorCode, String errorMsg) {
                    ToastUtil.showToast(rootContext, errorMsg);
                }
            });
        }
        //endregion

        //region Remove member form group
        private void removeMember(int index){
            adventureRequest = new AdventureRequest(rootContext, Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_REMOVE_MEMBER_GROUP_MEMBER), getRemoveMemberParams(index), false);
            getRemoveMemberResponse(index);
        }

        private Map<String ,String > getRemoveMemberParams(int index){
            Map<String , String > params = new HashMap<>();
            params.put(ApiConstants.KEY_TOKEN, token);
            params.put(ApiConstants.KEY_ID_GROUP_MEMBER, lstGroupMembers.get(index).getId());
            return params;
        }

        private void getRemoveMemberResponse(final int index){
            adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
                @Override
                public void onAdventureResponse(JSONObject response) {
                    ToastUtil.showToast(rootContext, lstGroupMembers.get(index).getOwner().getFullName() + " " + rootContext.getResources().getString(R.string.success_remove_member_group));
                    lstGroupMembers.remove(index);
                    notifyDataSetChanged();
                }

                @Override
                public void onAdventureError(int errorCode, String errorMsg) {
                    ToastUtil.showToast(rootContext, errorMsg);
                }
            });
        }
        //endregion

        //region Block member
        private void blockMember(int index){
            adventureRequest = new AdventureRequest(rootContext, Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_BLOCK_MEMBER_GROUP_MEMBER), getBlockMemberParams(index), false);
            getBlockMemberResponse(index);
        }

        private Map<String ,String > getBlockMemberParams(int index){
            Map<String , String > params = new HashMap<>();
            params.put(ApiConstants.KEY_TOKEN, token);
            params.put(ApiConstants.KEY_ID_GROUP_MEMBER, lstGroupMembers.get(index).getId());
            return params;
        }

        private void getBlockMemberResponse(final int index){
            adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
                @Override
                public void onAdventureResponse(JSONObject response) {
                    ToastUtil.showToast(rootContext, lstGroupMembers.get(index).getOwner().getFullName() + " " + rootContext.getResources().getString(R.string.success_block_member_group));
                    lstGroupMembers.remove(index);
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
