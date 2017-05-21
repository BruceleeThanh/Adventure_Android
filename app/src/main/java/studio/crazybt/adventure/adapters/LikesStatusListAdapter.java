package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.User;

/**
 * Created by Brucelee Thanh on 27/09/2016.
 */

public class LikesStatusListAdapter extends RecyclerView.Adapter<LikesStatusListAdapter.ViewHolder> {

    private Context rootContext;
    private List<User> userList;

    public LikesStatusListAdapter(Context rootContext, List<User> userList) {
        this.rootContext = rootContext;
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_like_status, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.rlItemLikeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                intent.putExtra(CommonConstants.KEY_ID_USER, user.getId());
                intent.putExtra(CommonConstants.KEY_USERNAME, user.getFirstName() + " " + user.getLastName());
                rootContext.startActivity(intent);
            }
        });
        PicassoHelper.execPicasso_ProfileImage(rootContext, user.getAvatar(), holder.ivProfileImage);
        holder.tvProfileName.setText(user.getFirstName() + " " + user.getLastName());
        if (user.getIsFriend() == -1) {
            holder.btnFriendOrFollow.setVisibility(View.GONE);
        } else {
            holder.btnFriendOrFollow.setVisibility(View.VISIBLE);
            if (user.getIsFriend() == 0){
                holder.btnFriendOrFollow.setText(R.string.add_friend_btn_friend_and_follow);
            }else if(user.getIsFriend() == 1){
                holder.btnFriendOrFollow.setText(R.string.friend_btn_friend_and_follow);
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        @BindView(R.id.rlItemLikeStatus)
        RelativeLayout rlItemLikeStatus;
        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.btnFriendOrFollow)
        Button btnFriendOrFollow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
