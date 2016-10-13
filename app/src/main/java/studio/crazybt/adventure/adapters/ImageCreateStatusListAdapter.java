package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.models.ImageUpload;
import studio.crazybt.adventure.utils.RLog;

/**
 * Created by Brucelee Thanh on 11/10/2016.
 */

public class ImageCreateStatusListAdapter extends RecyclerView.Adapter<ImageCreateStatusListAdapter.ViewHolder> {

    private List<ImageUpload> imageUploadList;
    private Context rootContext;

    public ImageCreateStatusListAdapter(List<ImageUpload> imageUploadList, Context rootContext) {
        this.imageUploadList = imageUploadList;
        this.rootContext = rootContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_create_status, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivImageCreateStatus.setImageBitmap(imageUploadList.get(position).getBitmap());
        RLog.i(BitmapCompat.getAllocationByteCount(imageUploadList.get(position).getBitmap()) + "; " +
                imageUploadList.get(position).getBitmap().getWidth() + "; " +
                imageUploadList.get(position).getBitmap().getHeight());
        if(imageUploadList.get(position).isDone()){
            holder.pbItemUploadImage.setVisibility(View.GONE);
        }else{
            holder.pbItemUploadImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return imageUploadList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivImageCreateStatus)
        ImageView ivImageCreateStatus;
        @BindView(R.id.pbItemUploadImage)
        ProgressBar pbItemUploadImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
