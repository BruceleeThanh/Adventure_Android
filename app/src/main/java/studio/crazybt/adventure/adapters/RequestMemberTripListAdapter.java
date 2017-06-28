package studio.crazybt.adventure.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.listeners.OnStringCallbackListener;
import studio.crazybt.adventure.models.TripMember;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 04/02/2017.
 */

public class RequestMemberTripListAdapter extends RecyclerView.Adapter<RequestMemberTripListAdapter.ViewHolder> {

    private Context rootContext;
    private List<TripMember> lstTripMember;
    private AdventureRequest adventureRequest;
    private String token;
    private OnStringCallbackListener onStringCallbackListener;

    public RequestMemberTripListAdapter(Context rootContext, List<TripMember> lstTripMember) {
        this.rootContext = rootContext;
        this.lstTripMember = lstTripMember;
        token = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_TOKEN, "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TripMember tripMember = lstTripMember.get(position);
        PicassoHelper.execPicasso_ProfileImage(rootContext, tripMember.getOwner().getAvatar(), holder.ivProfileImage);
        holder.tvProfileName.setText(tripMember.getOwner().getFullName());
        holder.tvProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.btnLeftFriendTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnLeftFriendTemplate.setEnabled(false);
                acceptRequest(tripMember.getId());
                getAcceptRequestResponse(holder, position,
                        tripMember.getOwner().getFullName());
            }
        });
        holder.btnRightFriendTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnRightFriendTemplate.setEnabled(false);
                rejectRequest(tripMember.getId());
                getRejectRequestResponse(holder, position,
                        tripMember.getOwner().getFullName());
            }
        });
        if(tripMember.getMessage() == null || tripMember.getMessage().isEmpty()){
            holder.ivMessage.setVisibility(View.GONE);
        }else{
            holder.ivMessage.setVisibility(View.VISIBLE);
            holder.ivMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadDialogReadMessageRequest(tripMember.getMessage());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lstTripMember == null ? 0 : lstTripMember.size();
    }

    private void loadDialogReadMessageRequest(String message){
        LayoutInflater li = LayoutInflater.from(rootContext);
        View dialogView = li.inflate(R.layout.dialog_read_message_request_trip_member, null);
        final Dialog dialog = new Dialog(rootContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);

        TextView tvMessageRequestMember = (TextView) dialog.findViewById(R.id.tvMessageRequestMember);
        StringUtil.setText(tvMessageRequestMember, message);

        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    //region Accept Trip Member
    private void acceptRequest(String idTripMember) {
        adventureRequest = new AdventureRequest(rootContext, Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_ACCEPT_REQUEST_TRIP_MEMBER), getAcceptRequestParams(idTripMember), false);
    }

    private HashMap getAcceptRequestParams(String idTripMember) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP_MEMBER, idTripMember);
        return params;
    }

    private void getAcceptRequestResponse(final ViewHolder holder, final int position, final String name) {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                lstTripMember.remove(position);
                notifyDataSetChanged();
                ToastUtil.showToast(rootContext, name + " " + rootContext.getResources().getString(R.string.success_accept_trip_member));
                holder.btnLeftFriendTemplate.setEnabled(true);

                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                JSONObject tripSchedule = JsonUtil.getJSONObject(data, ApiConstants.KEY_SCHEDULE);
                if (onStringCallbackListener != null && tripSchedule != null) {
                    onStringCallbackListener.onStringCallback(tripSchedule.toString());
                }
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(rootContext, errorMsg);
                holder.btnLeftFriendTemplate.setEnabled(true);
            }
        });

    }
    //endregion

    //region Reject Trip Member
    private void rejectRequest(String idTripMember) {
        adventureRequest = new AdventureRequest(rootContext, Request.Method.POST,
                ApiConstants.getUrl(ApiConstants.API_REJECT_REQUEST_TRIP_MEMBER), getRejectRequestParams(idTripMember), false);
    }

    private HashMap getRejectRequestParams(String idTripMember) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP_MEMBER, idTripMember);
        return params;
    }

    private void getRejectRequestResponse(final ViewHolder holder, final int position, final String name) {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                lstTripMember.remove(position);
                notifyDataSetChanged();
                ToastUtil.showToast(rootContext, rootContext.getResources().getString(R.string.success_reject_trip_member) + " " + name);
                holder.btnRightFriendTemplate.setEnabled(true);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(rootContext, errorMsg);
                holder.btnRightFriendTemplate.setEnabled(true);
            }
        });
    }
    //endregion

    public void setOnStringCallbackListener(OnStringCallbackListener listener) {
        this.onStringCallbackListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.btnLeftFriendTemplate)
        Button btnLeftFriendTemplate;
        @BindView(R.id.btnRightFriendTemplate)
        Button btnRightFriendTemplate;
        @BindView(R.id.tvMutualFriend)
        TextView tvMutualFriend;
        @BindView(R.id.ivMessage)
        ImageView ivMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnLeftFriendTemplate.setText(rootContext.getString(R.string.accept_btn_trip_member));
            btnRightFriendTemplate.setText(rootContext.getString(R.string.reject_btn_trip_member));
        }
    }
}
