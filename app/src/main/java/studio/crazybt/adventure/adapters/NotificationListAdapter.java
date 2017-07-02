package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.Notification;

/**
 * Created by Brucelee Thanh on 22/12/2016.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private Context rootContext;
    private List<Notification> notifications;

    public NotificationListAdapter(Context rootContext, List<Notification> notifications) {
        this.rootContext = rootContext;
        this.notifications = notifications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        if(notification.getType() == 1){
            holder.ivIconNotification.setImageResource(R.drawable.ic_chat_96);
        }else if(notification.getType() == 2){
            holder.ivIconNotification.setImageResource(R.drawable.ic_thumb_up_96);
        }
        holder.tvContentNotification.setText(Html.fromHtml(notification.getContent()));
        holder.tvTimeUpload.setText(ConvertTimeHelper.convertISODateToPrettyTimeStamp(notification.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.rlNotificationItem)
        RelativeLayout rlNotificationItem;
        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvContentNotification)
        TextView tvContentNotification;
        @BindView(R.id.ivIconNotification)
        ImageView ivIconNotification;
        @BindView(R.id.tvTimeUpload)
        TextView tvTimeUpload;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.rlNotificationItem)
        protected void onNotificationItemClick(){
            rootContext.startActivity(StatusActivity.newInstance(rootContext, CommonConstants.ACT_STATUS_DETAIL, notifications.get(getAdapterPosition())));
        }
    }
}
