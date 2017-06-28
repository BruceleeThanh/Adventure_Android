package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Created by Brucelee Thanh on 07/06/2017.
 */

public class GroupRequestMemberListAdapter extends RecyclerView.Adapter<GroupRequestMemberListAdapter.ViewHolder> {

    private Context rootContext;
    private List<GroupMember> lstGroupMembers = null;
    private AdventureRequest.OnNotifyResponseReceived onNotifyResponseReceived;

    public GroupRequestMemberListAdapter(Context rootContext, List<GroupMember> lstGroupMembers) {
        this.rootContext = rootContext;
        this.lstGroupMembers = lstGroupMembers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupMember groupMember = lstGroupMembers.get(position);
        PicassoHelper.execPicasso_ProfileImage(rootContext, groupMember.getOwner().getAvatar(), holder.ivProfileImage);
        StringUtil.setText(holder.tvProfileName, groupMember.getOwner().getFullName());
        StringUtil.setText(holder.tvMutualFriend, rootContext.getResources().getString(R.string.label_request_member_group_at) + " " + groupMember.getShortCreatedAt());
    }

    @Override
    public int getItemCount() {
        return lstGroupMembers == null ? 0 : lstGroupMembers.size();
    }


    public void setOnNotifyResponseReceived(AdventureRequest.OnNotifyResponseReceived listener){
        this.onNotifyResponseReceived = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdventureRequest adventureRequest = null;
        private String token = null;

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.tvMutualFriend)
        TextView tvMutualFriend;
        @BindView(R.id.btnLeftFriendTemplate)
        Button btnLeftFriendTemplate;
        @BindView(R.id.btnRightFriendTemplate)
        Button btnRightFriendTemplate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            token = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_TOKEN, null);
            btnLeftFriendTemplate.setText(R.string.label_accept_request_member);
            btnRightFriendTemplate.setText(R.string.label_reject_request_member);
            initAcceptMemberRequest();
            initRejectMemberRequest();
        }

        @OnClick({R.id.ivProfileImage, R.id.tvProfileName})
        void onProfileClick() {

        }

        @OnClick(R.id.btnLeftFriendTemplate)
        void onAcceptMemberClick() {
            btnLeftFriendTemplate.setEnabled(false);
            execAcceptMemberRequest(getAdapterPosition());
        }

        @OnClick(R.id.btnRightFriendTemplate)
        void onRejectMemberClick() {
            btnRightFriendTemplate.setEnabled(false);
            execRejectMemberRequest(getAdapterPosition());
        }

        private void initAcceptMemberRequest() {
            adventureRequest = new AdventureRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_ACCEPT_REQUEST_GROUP_MEMBER));
        }

        private void execAcceptMemberRequest(int index){
            adventureRequest.setUrl(ApiConstants.getUrl(ApiConstants.API_ACCEPT_REQUEST_GROUP_MEMBER));
            adventureRequest.setParams(getAcceptMemberParams(index));
            getAcceptMemberResponse(index);
            adventureRequest.execute(rootContext, false);
        }

        private Map<String, String> getAcceptMemberParams(int index) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiConstants.KEY_TOKEN, token);
            params.put(ApiConstants.KEY_ID_GROUP_MEMBER, lstGroupMembers.get(index).getId());
            return params;
        }

        private void getAcceptMemberResponse(final int index) {
            adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
                @Override
                public void onAdventureResponse(JSONObject response) {
                    ToastUtil.showToast(rootContext, lstGroupMembers.get(index).getOwner().getFullName() + " " +
                            rootContext.getResources().getString(R.string.success_accept_request_member_group));
                    lstGroupMembers.remove(index);
                    notifyDataSetChanged();
                    btnLeftFriendTemplate.setEnabled(true);
                    if (onNotifyResponseReceived != null) {
                        onNotifyResponseReceived.onNotify();
                    }

                }

                @Override
                public void onAdventureError(int errorCode, String errorMsg) {
                    btnLeftFriendTemplate.setEnabled(true);
                    ToastUtil.showToast(rootContext, errorMsg);
                }
            });
        }

        private void initRejectMemberRequest() {
            adventureRequest = new AdventureRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_REJECT_REQUEST_GROUP_MEMBER));
        }

        private void execRejectMemberRequest(int index){
            adventureRequest.setUrl(ApiConstants.getUrl(ApiConstants.API_REJECT_REQUEST_GROUP_MEMBER));
            adventureRequest.setParams(getRejectMemberParams(index));
            getRejectMemberResponse(index);
            adventureRequest.execute(rootContext, false);
        }

        private Map<String, String> getRejectMemberParams(int index) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiConstants.KEY_TOKEN, token);
            params.put(ApiConstants.KEY_ID_GROUP_MEMBER, lstGroupMembers.get(index).getId());
            return params;
        }

        private void getRejectMemberResponse(final int index) {
            adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
                @Override
                public void onAdventureResponse(JSONObject response) {
                    lstGroupMembers.remove(index);
                    notifyDataSetChanged();
                    btnRightFriendTemplate.setEnabled(true);
                    if (onNotifyResponseReceived != null) {
                        onNotifyResponseReceived.onNotify();
                    }
                }

                @Override
                public void onAdventureError(int errorCode, String errorMsg) {
                    btnRightFriendTemplate.setEnabled(true);
                    ToastUtil.showToast(rootContext, errorMsg);
                }
            });
        }


    }
}
