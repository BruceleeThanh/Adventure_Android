package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.one.EmojiOneProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.MessageFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.models.Conversation;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 29/06/2017.
 */

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder>{

    private Context rootContext;
    private FragmentActivity fragmentActivity;
    private List<Conversation> lstConversations;

    public ConversationListAdapter(Context rootContext, FragmentActivity fragmentActivity, List<Conversation> lstConversations) {
        this.rootContext = rootContext;
        this.fragmentActivity = fragmentActivity;
        this.lstConversations = lstConversations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EmojiManager.install(new EmojiOneProvider());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Conversation conversation = lstConversations.get(position);

        PicassoHelper.execPicasso_ProfileImage(rootContext, conversation.getPartner().getAvatar(), holder.civProfileImage);
        StringUtil.setText(holder.tvProfileName, conversation.getPartner().getFullName());
        StringUtil.setText(holder.etvLatestMessage, conversation.getLatestMessage().getContent());
        StringUtil.setText(holder.tvLatestMessageTime, conversation.getLatestMessage().getCreatedAtPrettyTimeStamp());
    }

    @Override
    public int getItemCount() {
        return lstConversations == null ? 0 : lstConversations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.rlConversationItem)
        RelativeLayout rlConversationItem;
        @BindView(R.id.civProfileImage)
        CircleImageView civProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.etvLatestMessage)
        EmojiTextView etvLatestMessage;
        @BindView(R.id.tvLatestMessageTime)
        TextView tvLatestMessageTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.rlConversationItem)
        protected void onConversationItemClick(){
            FragmentController.addFragment_BackStack_Animation(fragmentActivity, R.id.rlMessageActivity, MessageFragment.newInstance(lstConversations.get(getAdapterPosition()).getPartner()));
        }
    }

}
