package studio.crazybt.adventure.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.listeners.OnTimeSetupListener;
import studio.crazybt.adventure.models.DetailDiary;
import studio.crazybt.adventure.utils.RLog;

/**
 * Created by Brucelee Thanh on 01/03/2017.
 */

public class CreateDetailDiaryListAdapter extends RecyclerView.Adapter<CreateDetailDiaryListAdapter.ViewHolder> {

    private List<DetailDiary> lstDetailDiaries;
    private Context rootContext;
    private Calendar calendar;

    private OnTimeSetupListener onTimeSetupListener;

    public void setOnTimeSetupListener(OnTimeSetupListener listener) {
        onTimeSetupListener = listener;
    }

    public CreateDetailDiaryListAdapter(List<DetailDiary> lstDetailDiaries, Context rootContext) {
        this.lstDetailDiaries = lstDetailDiaries;
        this.rootContext = rootContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_detail_diary_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DetailDiary detailDiary = lstDetailDiaries.get(position);
        holder.etDateDetailDiaryTrip.setText(detailDiary.getDate() == null ? "" : detailDiary.getDate());
        holder.etDateDetailDiaryTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                setOnTimeSetupListener(new OnTimeSetupListener() {
                    @Override
                    public void onTimeSetup(Date date) {
                        lstDetailDiaries.get(position).setDate(ConvertTimeHelper.convertDateToString(date, ConvertTimeHelper.DATE_FORMAT_2));
                        notifyItemChanged(position);
                    }
                });
            }
        });
        holder.ivDeleteDetailDiaryTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstDetailDiaries.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.etTitleDetailDiaryTrip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (position < lstDetailDiaries.size())
                        lstDetailDiaries.get(position).setTitle(holder.etTitleDetailDiaryTrip.getText().toString());
                    else
                        lstDetailDiaries.get(position - 1).setTitle(holder.etTitleDetailDiaryTrip.getText().toString());
                }
            }
        });
        holder.etContentDetailDiaryTrip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (position < lstDetailDiaries.size())
                        lstDetailDiaries.get(position).setContent(holder.etContentDetailDiaryTrip.getText().toString());
                    else
                        lstDetailDiaries.get(position - 1).setContent(holder.etContentDetailDiaryTrip.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstDetailDiaries == null ? 0 : lstDetailDiaries.size();
    }

    private void datePicker() {
        // Get Current Date
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(rootContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if (onTimeSetupListener != null) {
                            onTimeSetupListener.onTimeSetup(calendar.getTime());
                        }
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.etDateDetailDiaryTrip)
        EditText etDateDetailDiaryTrip;
        @BindView(R.id.ivDeleteDetailDiaryTrip)
        ImageView ivDeleteDetailDiaryTrip;
        @BindView(R.id.etTitleDetailDiaryTrip)
        EditText etTitleDetailDiaryTrip;
        @BindView(R.id.etContentDetailDiaryTrip)
        EditText etContentDetailDiaryTrip;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
