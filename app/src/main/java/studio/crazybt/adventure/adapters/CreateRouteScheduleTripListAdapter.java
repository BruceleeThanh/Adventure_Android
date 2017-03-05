package studio.crazybt.adventure.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.listeners.OnTimeSetupListener;
import studio.crazybt.adventure.models.Route;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 10/01/2017.
 */

public class CreateRouteScheduleTripListAdapter extends RecyclerView.Adapter<CreateRouteScheduleTripListAdapter.ViewHolder> {

    private Context rootContext;
    private List<Route> lstRoutes;
    private Calendar calendar;
    private Date startDate = null;
    private Date endDate = null;

    public CreateRouteScheduleTripListAdapter(Context rootContext, List<Route> lstRoutes) {
        this.rootContext = rootContext;
        this.lstRoutes = lstRoutes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_route_schedule_trip, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Chưa xử lý xong Date format 2

        final Route route = lstRoutes.get(position);
        holder.etStartTimeRoute.setText(route.getStartAt() == null ? "" : route.getStartAt());
        holder.etStartTimeRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                setOnTimeSetupListener(new OnTimeSetupListener() {
                    @Override
                    public void onTimeSetup(Date date) {
                        lstRoutes.get(position).setStartAt(setDateTime(holder.etStartTimeRoute, date, route.getStartAt(), route.getEndAt(),
                                ConvertTimeHelper.DATE_FORMAT_1));
                        notifyItemChanged(position);
                    }
                });
            }
        });
        holder.etEndTimeRoute.setText(route.getEndAt() == null ? "" : route.getEndAt());
        holder.etEndTimeRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                setOnTimeSetupListener(new OnTimeSetupListener() {
                    @Override
                    public void onTimeSetup(Date date) {
                        lstRoutes.get(position).setEndAt(setDateTime(holder.etEndTimeRoute, date, route.getStartAt(), route.getEndAt(),
                                ConvertTimeHelper.DATE_FORMAT_1));
                        notifyItemChanged(position);
                    }
                });
            }
        });
        holder.etTitleRoute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    lstRoutes.get(position).setTitle(holder.etTitleRoute.getText().toString());
                }
            }
        });
        holder.etContentRoute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    lstRoutes.get(position).setContent(holder.etContentRoute.getText().toString());
                }
            }
        });
        holder.ivDeleteRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstRoutes.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstRoutes == null ? 0 : lstRoutes.size();
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
                        timePicker();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void timePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(rootContext,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        if(onTimeSetupListener != null){
                            onTimeSetupListener.onTimeSetup(calendar.getTime());
                        }
                    }
                }, hour, minute, true);
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, rootContext.getResources().getString(R.string.cancel_dialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        if(onTimeSetupListener != null){
                            onTimeSetupListener.onTimeSetup(calendar.getTime());
                        }
                    }
                });
        timePickerDialog.show();
    }

    private String setDateTime(EditText editText, Date date, String strStartDate, String strEndDate, String format) {
        String resultDate = null;
        if (date.compareTo(new Date()) > 0) {
            if (editText.getId() == R.id.etStartTimeRoute) {
                if(startDate.compareTo(date) > 0){
                    ToastUtil.showToast(rootContext, R.string.error_start_time_route_before_start);
                }else {
                    if (strEndDate != null && ConvertTimeHelper.convertStringToDate(strEndDate, ConvertTimeHelper.DATE_FORMAT_1).compareTo(date) < 0) {
                        ToastUtil.showToast(rootContext, rootContext.getResources().getString(R.string.error_start_time_after_end));
                    } else {
                        resultDate = ConvertTimeHelper.convertDateToString(date, ConvertTimeHelper.DATE_FORMAT_1);
                    }
                }
            } else if (editText.getId() == R.id.etEndTimeRoute) {
                if(endDate != null && endDate.compareTo(date) < 0){
                    ToastUtil.showToast(rootContext, R.string.error_end_time_route_after_end);
                } else {
                    if (strStartDate != null && ConvertTimeHelper.convertStringToDate(strStartDate, ConvertTimeHelper.DATE_FORMAT_1).compareTo(date) > 0) {
                        ToastUtil.showToast(rootContext, rootContext.getResources().getString(R.string.error_end_time_before_start));
                    } else {
                        resultDate = ConvertTimeHelper.convertDateToString(date, ConvertTimeHelper.DATE_FORMAT_1);
                    }
                }
            }
        } else {
            ToastUtil.showToast(rootContext, rootContext.getResources().getString(R.string.error_time_before_now));
        }
        return resultDate;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.etStartTimeRoute)
        EditText etStartTimeRoute;
        @BindView(R.id.etEndTimeRoute)
        EditText etEndTimeRoute;
        @BindView(R.id.ivDeleteRoute)
        ImageView ivDeleteRoute;
        @BindView(R.id.etTitleRoute)
        EditText etTitleRoute;
        @BindView(R.id.etContentRoute)
        EditText etContentRoute;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnTimeSetupListener onTimeSetupListener;

    public void setOnTimeSetupListener(OnTimeSetupListener listener){
        onTimeSetupListener = listener;
    }
}
