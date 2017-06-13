package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.models.ImageContent;

/**
 * Created by Brucelee Thanh on 31/05/2017.
 */

public class ImageGroupListAdapter extends RecyclerView.Adapter<ImageGroupListAdapter.ViewHolder>{

    private Context rootContext = null;
    private List<ImageContent> lstImageContents = null;

    public ImageGroupListAdapter(Context rootContext, List<ImageContent> lstImageContents) {
        this.rootContext = rootContext;
        this.lstImageContents = lstImageContents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PicassoHelper.execPicasso(rootContext, lstImageContents.get(position).getUrl(), holder.ivImage);
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lstImageContents == null ? 0 : lstImageContents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivImage)
        ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
