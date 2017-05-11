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

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.Friend;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 22/09/2016.
 */

public class SuggestionsFriendListAdapter extends RecyclerView.Adapter<SuggestionsFriendListAdapter.ViewHolder> {

    private Context rootContext;
    private List<User> users;

    public SuggestionsFriendListAdapter(Context rootContext) {
        this.rootContext = rootContext;
    }

    public SuggestionsFriendListAdapter(Context rootContext, List<User> users) {
        this.rootContext = rootContext;
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_template, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvProfileName.setText(users.get(position).getFirstName() + " " + users.get(position).getLastName());
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                intent.putExtra(CommonConstants.KEY_ID_USER, users.get(position).getId());
                intent.putExtra(CommonConstants.KEY_USERNAME, users.get(position).getFirstName() + " " + users.get(position).getLastName());
                rootContext.startActivity(intent);
            }
        });
        holder.tvProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                intent.putExtra(CommonConstants.KEY_ID_USER, users.get(position).getId());
                intent.putExtra(CommonConstants.KEY_USERNAME, users.get(position).getFirstName() + " " + users.get(position).getLastName());
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
                params.put(apiConstants.KEY_USER, users.get(position).getId());
                CustomRequest customRequest = new CustomRequest(Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_SEND_REQUEST_FRIEND),
                        params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(jsonUtil.getInt(response, apiConstants.DEF_CODE, 0) == 1){
                            Toast.makeText(rootContext, rootContext.getResources().getString(R.string.success_request_friend) + " "
                                    + users.get(position).getFirstName() + " " + users.get(position).getLastName(), Toast.LENGTH_LONG).show();
                        }
                        users.remove(position);
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
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProfileImage;
        public TextView tvProfileName;
        public TextView tvMutualFriend;
        public Button btnLeftFriendTemplate;
        public Button btnRightFriendTemplate;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvProfileName = (TextView) itemView.findViewById(R.id.tvProfileName);
            tvMutualFriend = (TextView) itemView.findViewById(R.id.tvMutualFriend);
            btnLeftFriendTemplate = (Button) itemView.findViewById(R.id.btnLeftFriendTemplate);
            btnLeftFriendTemplate.setText(rootContext.getString(R.string.add_friend_btn_friend_and_follow));
            btnRightFriendTemplate = (Button) itemView.findViewById(R.id.btnRightFriendTemplate);
            btnRightFriendTemplate.setText(rootContext.getString(R.string.follow_btn_friend_and_follow));
        }
    }
}
