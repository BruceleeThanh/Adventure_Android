package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.Friend;
import studio.crazybt.adventure.models.User;

/**
 * Created by Brucelee Thanh on 22/09/2016.
 */

public class AllFriendListAdapter extends RecyclerView.Adapter<AllFriendListAdapter.ViewHolder> {

    private Context rootContext;
    private List<User> users;

    public AllFriendListAdapter(Context rootContext) {
        this.rootContext = rootContext;
    }

    public AllFriendListAdapter(Context rootContext, List<User> users) {
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PicassoHelper.execPicasso_ProfileImage(rootContext, users.get(position).getAvatar(), holder.ivProfileImage);
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
            btnLeftFriendTemplate.setText(rootContext.getString(R.string.message_btn_friend_and_follow));
            btnRightFriendTemplate = (Button) itemView.findViewById(R.id.btnRightFriendTemplate);
            btnRightFriendTemplate.setText(rootContext.getString(R.string.cancel_friend_btn_friend_and_follow));
        }
    }
}
