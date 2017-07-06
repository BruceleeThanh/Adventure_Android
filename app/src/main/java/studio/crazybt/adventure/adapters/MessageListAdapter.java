package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Message;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 03/10/2016.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context rootContext;
    private List<Message> lstMessage;
    private String partnerLastName;

    private final int SENT = 1;
    private final int RECEIVED = 2;

    private String idUser;

    public MessageListAdapter(Context rootContext, List<Message> lstMessage, String partnerLastName) {
        this.rootContext = rootContext;
        this.lstMessage = lstMessage;
        this.partnerLastName = partnerLastName;
        this.idUser = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_ID, null);
    }

    @Override
    public int getItemViewType(int position) {
        String owner = lstMessage.get(position).getOwner();
        if (idUser.equals(owner))
            return SENT;
        else
            return RECEIVED;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(rootContext);
        if (viewType == SENT) {
            View viewSent = li.inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(viewSent);
        } else if (viewType == RECEIVED) {
            View viewSent = li.inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(viewSent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = lstMessage.get(position);
        if (getItemViewType(position) == SENT) {
            SentViewHolder sentViewHolder = (SentViewHolder) holder;

            sentViewHolder.etvMessageContent.getBackground().setColorFilter(ContextCompat.getColor(rootContext, R.color.primary), PorterDuff.Mode.SRC_IN);
            StringUtil.setText(sentViewHolder.etvMessageContent, message.getContent());

            StringUtil.setText(sentViewHolder.tvMessageCreatedAt, message.getCreatedAtLabel());

        } else if (getItemViewType(position) == RECEIVED) {
            ReceivedViewHolder receivedViewHolder = (ReceivedViewHolder) holder;

            StringUtil.setText(receivedViewHolder.etvMessageContent, message.getContent());

            StringUtil.setText(receivedViewHolder.tvMessageCreatedAt, message.getCreatedAtLabel());
        }
    }

    @Override
    public int getItemCount() {
        return lstMessage == null ? 0 : lstMessage.size();
    }

    class SentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.etvMessageContent)
        EmojiTextView etvMessageContent;
        @BindView(R.id.tvMessageCreatedAt)
        TextView tvMessageCreatedAt;

        SentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.etvMessageContent)
        void onMessageContentClick() {
            if (tvMessageCreatedAt.getVisibility() == View.GONE) {
                tvMessageCreatedAt.setVisibility(View.VISIBLE);
            } else {
                tvMessageCreatedAt.setVisibility(View.GONE);
            }
        }
    }

    class ReceivedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvMessageCreatedAt)
        TextView tvMessageCreatedAt;
        @BindView(R.id.etvMessageContent)
        EmojiTextView etvMessageContent;

        ReceivedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.etvMessageContent)
        void onMessageContentClick() {
            if (tvMessageCreatedAt.getVisibility() == View.GONE) {
                tvMessageCreatedAt.setVisibility(View.VISIBLE);
            } else {
                tvMessageCreatedAt.setVisibility(View.GONE);
            }
        }
    }
}
