package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.activities.StatusActivity;
import studio.crazybt.adventure.helpers.DrawableProcessHelper;
import studio.crazybt.adventure.helpers.FragmentController;

/**
 * Created by Brucelee Thanh on 28/09/2016.
 */

public class StatusShortcutListAdapter extends RecyclerView.Adapter<StatusShortcutListAdapter.ViewHolder> {

    private Context rootContext;
    private FragmentController fragmentController;

    public static final int STATUS_DETAIL = 1;
    public static final int STATUS_LIKES = 2;
    public static final int STATUS_COMMENTS = 3;

    public StatusShortcutListAdapter(Context context) {
        this.rootContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status_shortcut, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.tvProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.etvContentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_DETAIL);
                rootContext.startActivity(intent);
            }
        });
        holder.llImageStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_DETAIL);
                rootContext.startActivity(intent);
            }
        });
        holder.tvCountLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_LIKES);
                rootContext.startActivity(intent);
            }
        });
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cbLike.isChecked()) {
                    holder.cbLike.setChecked(false);
                    holder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.secondary_text));
                    int countLike = Integer.parseInt(holder.tvCountLike.getText().toString());
                    holder.tvCountLike.setText(String.valueOf(countLike - 1));
                }else{
                    holder.cbLike.setChecked(true);
                    holder.cbLike.setTextColor(rootContext.getResources().getColor(R.color.primary));
                    int countLike = Integer.parseInt(holder.tvCountLike.getText().toString());
                    holder.tvCountLike.setText(String.valueOf(countLike + 1));
                }
            }
        });
        holder.tvCountComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_COMMENTS);
                rootContext.startActivity(intent);
            }
        });
        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, StatusActivity.class);
                intent.putExtra("TYPE_SHOW", STATUS_COMMENTS);
                rootContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public DrawableProcessHelper drawableProcessHelper;

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.etvContentStatus)
        EmojiTextView etvContentStatus;
        @BindView(R.id.llImageStatus)
        LinearLayout llImageStatus;
        @BindView(R.id.tvCountLike)
        TextView tvCountLike;
        @BindView(R.id.tvCountComment)
        TextView tvCountComment;
        @BindView(R.id.llLike)
        LinearLayout llLike;
        @BindView(R.id.cbLike)
        CheckBox cbLike;
        @BindView(R.id.tvComment)
        TextView tvComment;
        @BindDimen(R.dimen.item_icon_size_small)
        float itemSizeSmall;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            drawableProcessHelper = new DrawableProcessHelper(itemView);
            drawableProcessHelper.setTextViewDrawableFitSize(tvCountLike, R.drawable.ic_thumb_up_96, itemSizeSmall, itemSizeSmall);
            drawableProcessHelper.setTextViewDrawableFitSize(tvCountComment, R.drawable.ic_chat_96, itemSizeSmall, itemSizeSmall);
        }
    }
}