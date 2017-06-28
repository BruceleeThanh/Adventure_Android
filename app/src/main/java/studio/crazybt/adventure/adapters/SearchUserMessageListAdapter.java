package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.models.User;

/**
 * Created by Brucelee Thanh on 12/05/2017.
 */

public class SearchUserMessageListAdapter extends RecyclerView.Adapter<SearchUserMessageListAdapter.ViewHolder>{

    private Context rootContext = null;
    private List<User> lstUsers = null;

    public SearchUserMessageListAdapter(Context context, List<User> lstUsers) {
        this.rootContext = context;
        this.lstUsers = lstUsers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user_message, parent, false);
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

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
