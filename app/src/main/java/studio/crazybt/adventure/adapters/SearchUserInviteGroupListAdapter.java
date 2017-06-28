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
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 14/06/2017.
 */

public class SearchUserInviteGroupListAdapter extends RecyclerView.Adapter<SearchUserInviteGroupListAdapter.ViewHolder>{

    private Context rootContext = null;
    private List<User> lstUsers = null;
    private String idGroup = null;

    public SearchUserInviteGroupListAdapter(Context context, List<User> lstUsers, String idGroup) {
        this.rootContext = context;
        this.lstUsers = lstUsers;
        this.idGroup = idGroup;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user_invite_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PicassoHelper.execPicasso_ProfileImage(rootContext, lstUsers.get(position).getAvatar(), holder.ivProfileImage);
        holder.tvProfileName.setText(lstUsers.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return lstUsers == null ? 0 : lstUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private AdventureRequest adventureRequest;
        private String token = null;

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.btnRightSearchTemplate)
        Button btnRightSearchTemplate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            token = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_TOKEN, null);
        }

        @OnClick(R.id.btnRightSearchTemplate)
        void onInviteMemberClick(){
            btnRightSearchTemplate.setEnabled(false);
            inviteMember();
        }

        private void inviteMember(){
            adventureRequest = new AdventureRequest(rootContext, Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_INVITE_GROUP_MEMBER), getInviteMemberParams(), false);
            getInviteMemberResponse();
        }

        private Map<String ,String > getInviteMemberParams(){
            Map<String ,String > params = new HashMap<>();
            params.put(ApiConstants.KEY_TOKEN, token);
            params.put(ApiConstants.KEY_ID_GROUP, idGroup);
            params.put(ApiConstants.KEY_ID_USER, lstUsers.get(getAdapterPosition()).getId());
            return params;
        }

        private void getInviteMemberResponse(){
            adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
                @Override
                public void onAdventureResponse(JSONObject response) {
                    ToastUtil.showToast(rootContext, rootContext.getResources().getString(R.string.success_invite_member_group) + " " + lstUsers.get(getAdapterPosition()).getFullName());
                    btnRightSearchTemplate.setEnabled(true);
                    lstUsers.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }

                @Override
                public void onAdventureError(int errorCode, String errorMsg) {
                    ToastUtil.showToast(rootContext, errorMsg);
                    btnRightSearchTemplate.setEnabled(true);
                }
            });
        }
    }
}
