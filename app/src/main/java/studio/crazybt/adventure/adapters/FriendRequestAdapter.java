package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.models.Friend;

/**
 * Created by Brucelee Thanh on 13/09/2016.
 */
public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder>{

    private Context rootContext;
    private List<Friend> listFriendRequest;

    public FriendRequestAdapter(Context rootContext) {
        this.rootContext = rootContext;
    }

    public FriendRequestAdapter(Context rootContext, List<Friend> listFriendRequest) {
        this.rootContext = rootContext;
        this.listFriendRequest = listFriendRequest;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        private ImageView ivProfileImage;
        private TextView tvProfileName;
        private TextView tvMutualFriend;
        private Button btnAcceptFriendRequest;
        private Button btnRejectFriendRequest;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvProfileName = (TextView) itemView.findViewById(R.id.tvProfileName);
            tvMutualFriend = (TextView) itemView.findViewById(R.id.tvMutualFriend);
            btnAcceptFriendRequest = (Button) itemView.findViewById(R.id.btnAcceptFriendRequest);
            btnRejectFriendRequest = (Button) itemView.findViewById(R.id.btnRejectFriendRequest);
        }
    }
}
