package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.one.EmojiOneProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.models.CommentStatus;

/**
 * Created by Brucelee Thanh on 25/09/2016.
 */

public class CommentStatusListAdapter extends RecyclerView.Adapter<CommentStatusListAdapter.ViewHolder> {

    private Context rootContext;
    private List<CommentStatus> commentStatusList;

    public CommentStatusListAdapter(Context rootContext, List<CommentStatus> commentStatusList) {
        this.rootContext = rootContext;
        this.commentStatusList = commentStatusList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EmojiManager.install(new EmojiOneProvider());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_status, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CommentStatus commentStatus = commentStatusList.get(position);
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.tvProfileName.setText(commentStatus.getUser().getFirstName() + " " + commentStatus.getUser().getLastName());
        holder.tvProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.etvContentComment.setText(commentStatus.getContent());
        holder.tvTimeUpload.setText(new ConvertTimeHelper().convertISODateToPrettyTimeStamp(commentStatus.getCreatedAt()));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onAdapterClickListener != null){
                    onAdapterClickListener.onItemLongClick(v, position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentStatusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.tvTimeUpload)
        TextView tvTimeUpload;
        @BindView(R.id.etvContentComment)
        EmojiTextView etvContentComment;

        public ViewHolder(View itemView) {
            super(itemView);
            EmojiManager.install(new EmojiOneProvider());
            this.itemView=itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private AdapterClickListener onAdapterClickListener;
    public void setOnAdapterClickListener(AdapterClickListener listener){
        onAdapterClickListener = listener;
    }

    public interface AdapterClickListener{
        void onItemLongClick(View v, int pos);
    }
}
