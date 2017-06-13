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
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 24/09/2016.
 */

public class ImageStatusDetailListAdapter extends RecyclerView.Adapter<ImageStatusDetailListAdapter.ViewHolder>{

    private Context rootContext;
    private List<ImageContent> imageContents;

    public ImageStatusDetailListAdapter(Context rootContext, List<ImageContent> imageContents) {
        this.rootContext = rootContext;
        this.imageContents = imageContents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_status_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PicassoHelper.execPicasso(rootContext, imageContents.get(position).getUrl(), holder.ivImageStatusDetail);
        if (imageContents.get(position).getDescription() == null || imageContents.get(position).getDescription().equals("")){
            holder.tvDescriptionImageStatusDetail.setVisibility(View.GONE);
        }else{
            holder.tvDescriptionImageStatusDetail.setVisibility(View.VISIBLE);
            StringUtil.setText_Gone(holder.tvDescriptionImageStatusDetail, imageContents.get(position).getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return imageContents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        @BindView(R.id.ivImageStatusDetail)
        ImageView ivImageStatusDetail;
        @BindView(R.id.tvDescriptionImageStatusDetail)
        TextView tvDescriptionImageStatusDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
