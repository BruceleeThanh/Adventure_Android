package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.listeners.OnTextWatcher;
import studio.crazybt.adventure.models.ImageUpload;
import studio.crazybt.adventure.utils.StringUtil;

/**
 * Created by Brucelee Thanh on 25/05/2017.
 */

public class EditImageCreateStatusListAdapter extends RecyclerView.Adapter<EditImageCreateStatusListAdapter.ViewHolder> {

    private Context rootContext = null;
    private List<ImageUpload> lstImageUploads = null;
    private List<Image> lstImages = null;
    private OnTextWatcher onTextWatcher;
    private MyCustomEditTextListener myCustomEditTextListener;


    public EditImageCreateStatusListAdapter(Context rootContext, List<ImageUpload> lstImageUploads, List<Image> lstImages) {
        this.lstImageUploads = lstImageUploads;
        this.lstImages = lstImages;
        this.rootContext = rootContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_image_create_status, parent, false);
        return new ViewHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ImageUpload imageUpload = lstImageUploads.get(holder.getAdapterPosition());
        StringUtil.setText(holder.etEditDescriptionImageCreateStatus, imageUpload.getDescription());
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.ivEditImageCreateStatus.setImageBitmap(imageUpload.getBitmap());
    }



    @Override
    public int getItemCount() {
        return lstImageUploads == null ? 0 : lstImageUploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivEditImageCreateStatus)
        ImageView ivEditImageCreateStatus;
        @BindView(R.id.ivRemoveImageCreateStatus)
        ImageView ivRemoveImageCreateStatus;
        @BindView(R.id.etEditDescriptionImageCreateStatus)
        EditText etEditDescriptionImageCreateStatus;

        public MyCustomEditTextListener myCustomEditTextListener;

        public ViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.myCustomEditTextListener = myCustomEditTextListener;
            ivRemoveImageCreateStatus.setOnClickListener(this);
            etEditDescriptionImageCreateStatus.addTextChangedListener(this.myCustomEditTextListener);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.ivRemoveImageCreateStatus){
                lstImageUploads.remove(getAdapterPosition());
                lstImages.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), lstImageUploads.size() - getAdapterPosition() - 1);
            }
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            lstImageUploads.get(position).setDescription(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
