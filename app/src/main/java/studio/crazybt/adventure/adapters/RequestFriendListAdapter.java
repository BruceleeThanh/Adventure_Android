package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.Friend;
import studio.crazybt.adventure.models.RequestFriend;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class RequestFriendListAdapter extends RecyclerView.Adapter<RequestFriendListAdapter.ViewHolder> {

    private Context rootContext;
    private List<RequestFriend> requestFriends;

    public RequestFriendListAdapter(Context rootContext) {
        this.rootContext = rootContext;
    }

    public RequestFriendListAdapter(Context rootContext, List<RequestFriend> requestFriends) {
        this.rootContext = rootContext;
        this.requestFriends = requestFriends;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_template, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PicassoHelper.execPicasso_ProfileImage(rootContext, requestFriends.get(position).getSender().getAvatar(), holder.ivProfileImage);
        holder.tvProfileName.setText(requestFriends.get(position).getSender().getFullName());
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                intent.putExtra(CommonConstants.KEY_ID_USER, requestFriends.get(position).getSender().getId());
                intent.putExtra(CommonConstants.KEY_USERNAME, requestFriends.get(position).getSender().getFullName());
                rootContext.startActivity(intent);
            }
        });
        holder.tvProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                intent.putExtra(CommonConstants.KEY_ID_USER, requestFriends.get(position).getSender().getId());
                intent.putExtra(CommonConstants.KEY_USERNAME, requestFriends.get(position).getSender().getFullName());
                rootContext.startActivity(intent);
            }
        });
        holder.btnLeftFriendTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnLeftFriendTemplate.setEnabled(false);
                final ApiConstants apiConstants = new ApiConstants();
                final String token = SharedPref.getInstance(rootContext).getString(apiConstants.KEY_TOKEN, "");
                final JsonUtil jsonUtil = new JsonUtil();
                Map<String, String> params = new HashMap<>();
                params.put(apiConstants.KEY_TOKEN, token);
                params.put(apiConstants.KEY_ID, requestFriends.get(position).getId());
                CustomRequest customRequest = new CustomRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_CONFIRM_REQUEST_FRIEND),
                        params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(jsonUtil.getInt(response, apiConstants.DEF_CODE, 0) == 1){
                            ToastUtil.showToast(rootContext, R.string.success_confirm_request_friend);
                        }
                        requestFriends.remove(position);
                        notifyDataSetChanged();
                        holder.btnLeftFriendTemplate.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(rootContext, R.string.error_connect);
                    }
                });
                MySingleton.getInstance(rootContext).addToRequestQueue(customRequest, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestFriends.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

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
            btnLeftFriendTemplate.setText(rootContext.getString(R.string.accept_btn_friend_and_follow));
            btnRightFriendTemplate.setText(rootContext.getString(R.string.reject_btn_friend_and_follow));
        }
    }
}
