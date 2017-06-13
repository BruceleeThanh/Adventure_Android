package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.EditImageCreateStatusFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.models.ImageUpload;
import studio.crazybt.adventure.utils.RLog;

/**
 * Created by Brucelee Thanh on 11/10/2016.
 */

public class ImageCreateStatusListAdapter extends RecyclerView.Adapter<ImageCreateStatusListAdapter.ViewHolder> {

    private FragmentActivity fragmentActivity = null;
    private ArrayList<ImageUpload> lstImageUploads = null;
    private ArrayList<Image> lstImages = null;
    private Context rootContext = null;
    private EditImageCreateStatusFragment editImageCreateStatusFragment = null;

    public ImageCreateStatusListAdapter(FragmentActivity fragmentActivity, ArrayList<ImageUpload> lstImageUploads, ArrayList<Image> lstImages, EditImageCreateStatusFragment editImageCreateStatusFragment, Context rootContext) {
        this.fragmentActivity = fragmentActivity;
        this.lstImageUploads = lstImageUploads;
        this.lstImages = lstImages;
        this.editImageCreateStatusFragment = editImageCreateStatusFragment;
        this.rootContext = rootContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_create_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivImageCreateStatus.setImageBitmap(lstImageUploads.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return lstImageUploads == null ? 0 : lstImageUploads.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.ivImageCreateStatus)
        ImageView ivImageCreateStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            fragmentActivity.invalidateOptionsMenu();
            FragmentController.addFragment_BackStack_Animation(fragmentActivity, R.id.rlInput, editImageCreateStatusFragment);
        }
    }
}
