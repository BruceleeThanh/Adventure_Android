package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.models.SpinnerItem;

/**
 * Created by Brucelee Thanh on 02/12/2016.
 */

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    int groupId;
    List<SpinnerItem> itemList;
    LayoutInflater inflater;
    @BindView(R.id.ivSpinnerPrivacy)
    ImageView ivSpinnerPrivacy;
    @BindView(R.id.tvSpinnerPrivacy)
    TextView tvSpinnerPrivacy;

    public SpinnerAdapter(Context context, int groupId, int resource, List<SpinnerItem> objects) {
        super(context, resource, objects);
        this.itemList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupId = groupId;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupId, parent, false);
        ButterKnife.bind(this, itemView);
        ivSpinnerPrivacy.setImageResource(itemList.get(pos).getIconId());
        tvSpinnerPrivacy.setText(itemList.get(pos).getLabel());
        return itemView;
    }

    public View getDropDownView(int pos, View convertView, ViewGroup parent) {
        return getView(pos, convertView, parent);
    }
}
