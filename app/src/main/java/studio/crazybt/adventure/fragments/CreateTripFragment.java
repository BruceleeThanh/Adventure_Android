package studio.crazybt.adventure.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.CreateRouteScheduleTripListAdapter;
import studio.crazybt.adventure.adapters.SpinnerAdapter;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Route;
import studio.crazybt.adventure.models.SpinnerItem;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.TagsEditText;
import studio.crazybt.adventure.utils.ToastUtil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Brucelee Thanh on 06/01/2017.
 */

public class CreateTripFragment extends Fragment implements View.OnClickListener {

    private View rootView = null;
    private Place startPositionPlace = null;
    private List<studio.crazybt.adventure.models.Place> lstPlaceTrip = null;
    private List<Place> lstDestinationPlace = null;
    private List<String> lstDestinationName = null;
    private Calendar calendar = null;
    private Date startDate = null;
    private Date endDate = null;

    @BindView(R.id.spiPrivacy)
    AppCompatSpinner spiPrivacy;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.etCreateTripName)
    EditText etCreateTripName;
    @BindView(R.id.etCreateTripDescription)
    EditText etCreateTripDescription;
    @BindView(R.id.tetCreateTripStartPosition)
    TagsEditText tetCreateTripStartPosition;
    @BindView(R.id.btnPlacesStartPosition)
    Button btnPlacesStartPosition;
    @BindView(R.id.etCreateTripStartTime)
    EditText etCreateTripStartTime;
    @BindView(R.id.etCreateTripEndTime)
    EditText etCreateTripEndTime;
    @BindView(R.id.btnResetTime)
    Button btnResetTime;
    @BindView(R.id.etCreateTripDescriptionSummary)
    EditText etCreateTripDescriptionSummary;
    @BindView(R.id.tetCreateTripDestination)
    TagsEditText tetCreateTripDestination;
    @BindView(R.id.btnPlacesDestination)
    Button btnPlacesDestination;
    @BindView(R.id.etCreateTripMoney)
    EditText etCreateTripMoney;
    @BindView(R.id.etCreateTripMember)
    EditText etCreateTripMember;
    @BindView(R.id.tetCreateTripVehicle)
    TagsEditText tetCreateTripVehicle;
    @BindView(R.id.btnAddTripRoute)
    Button btnAddTripRoute;
    @BindView(R.id.rvCreateTripRoute)
    RecyclerView rvCreateTripRoute;
    @BindView(R.id.etCreateTripToolbox)
    EditText etCreateTripToolbox;
    @BindView(R.id.etCreateTripNote)
    EditText etCreateTripNote;

    @BindDimen(R.dimen.item_icon_size_small)
    float itemSizeSmall;

    private DrawableHelper drawableHelper;
    private CreateRouteScheduleTripListAdapter crstlaAdapter;
    private List<Route> lstRoutes;
    private AdventureRequest adventureRequest;

    private String token;
    private final String CURRENT_TRIP_PRIVACY = "current_trip_privacy";
    private final int PLACE_START_POSITION_REQUEST_CODE = 100;
    private final int PLACE_DESTINATION_REQUEST_CODE = 200;
    private final int SET_START_TIME = 1;
    private final int SET_END_TIME = 2;
    private boolean isPlaceInStartPosition = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_create_trip, container, false);
        }
        ButterKnife.bind(this, rootView);
        this.initControls();
        this.initCreator();
        this.initSpinnerPrivacy();
        this.initTripRoute();
        this.initDrawableView();
        return rootView;
    }

    private void initControls() {
        token = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_TOKEN, "");
        btnPlacesStartPosition.setOnClickListener(this);
        btnPlacesDestination.setOnClickListener(this);
        etCreateTripStartTime.setOnClickListener(this);
        etCreateTripEndTime.setOnClickListener(this);
        btnResetTime.setOnClickListener(this);
        tetCreateTripDestination.setTagsListener(new TagsEditText.TagsEditListener() {
            @Override
            public void onTagsChanged(Collection<String> tags) {

            }

            @Override
            public void onTagRemoved(int index) {
                lstDestinationName.remove(index);
                lstDestinationPlace.remove(index);
            }

            @Override
            public void onEditingFinished() {

            }
        });
        tetCreateTripVehicle.setTagsWithSpacesEnabled(true);
        tetCreateTripVehicle.setAdapter(new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.vehicle)));
        tetCreateTripVehicle.setThreshold(1);
        btnAddTripRoute.setOnClickListener(this);

        lstDestinationPlace = new ArrayList<>();
        lstDestinationName = new ArrayList<>();
        lstRoutes = new ArrayList<>();
        lstPlaceTrip = new ArrayList<>();
    }

    private void initCreator() {
        tvProfileName.setText(SharedPref.getInstance(rootView.getContext()).getString(ApiConstants.KEY_FIRST_NAME, "") + " " + SharedPref.getInstance(rootView.getContext()).getString(ApiConstants.KEY_LAST_NAME, ""));
    }

    private void initDrawableView() {
        drawableHelper = new DrawableHelper(getContext());
        drawableHelper.setEditTextDrawableFitSize(etCreateTripName, R.drawable.ic_signpost_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setEditTextDrawableFitSize(etCreateTripDescription, R.drawable.ic_information_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setEditTextDrawableFitSize(tetCreateTripStartPosition, R.drawable.ic_flag_filled_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setButtonDrawableFitSize(btnPlacesStartPosition, R.drawable.ic_map_marker_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setEditTextDrawableFitSize(etCreateTripStartTime, R.drawable.ic_clock_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setButtonDrawableFitSize(btnResetTime, R.drawable.ic_delete_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setEditTextDrawableFitSize(etCreateTripDescriptionSummary, R.drawable.ic_marker_normal_red_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setButtonDrawableFitSize(btnPlacesDestination, R.drawable.ic_map_marker_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setEditTextDrawableFitSize(etCreateTripMoney, R.drawable.ic_money_bag_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setEditTextDrawableFitSize(etCreateTripMember, R.drawable.ic_user_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setEditTextDrawableFitSize(tetCreateTripVehicle, R.drawable.ic_vehicle_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setEditTextDrawableFitSize(etCreateTripToolbox, R.drawable.ic_toolbox_96, itemSizeSmall, itemSizeSmall);
        drawableHelper.setEditTextDrawableFitSize(etCreateTripNote, R.drawable.ic_note_96, itemSizeSmall, itemSizeSmall);
    }

    private void initSpinnerPrivacy() {
        int currentPrivacy = SharedPref.getInstance(getContext()).getInt(CURRENT_TRIP_PRIVACY, 2);
        List<SpinnerItem> spinnerItemList = new ArrayList<>();
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.only_me_privacy), R.drawable.ic_private_96));
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.friend_privacy), R.drawable.ic_friend_96));
        spinnerItemList.add(new SpinnerItem(getResources().getString(R.string.public_privacy), R.drawable.ic_public_96));
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this.getContext(), R.layout.spinner_privacy, R.id.tvSpinnerPrivacy, spinnerItemList);
        spiPrivacy.setAdapter(spinnerAdapter);
        spiPrivacy.setSelection(currentPrivacy);
    }

    private void initPlaceAutoComplete(int requestCode) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this.getActivity());
            startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException e) {
            ToastUtil.showToast(getContext(), e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            ToastUtil.showToast(getContext(), e.getMessage());
        }
    }

    private void initTripRoute() {
        crstlaAdapter = new CreateRouteScheduleTripListAdapter(this.getContext(), lstRoutes);
        rvCreateTripRoute.setVisibility(View.GONE);
        rvCreateTripRoute.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCreateTripRoute.setAdapter(crstlaAdapter);
    }

    private void insertPlaceStartPostion(Place place) {
        tetCreateTripStartPosition.setTags(place.getAddress());
        this.deletePlaceStartButton();
    }

    private void deletePlaceStartPosition() {
        startPositionPlace = null;
        tetCreateTripStartPosition.setText("");
        this.setupPlaceStartButton();
    }

    private void setupPlaceStartButton() {
        btnPlacesStartPosition.setBackgroundResource(R.drawable.selector_green_white_btn_2);
        btnPlacesStartPosition.setText(getResources().getString(R.string.map_create_trip));
        btnPlacesStartPosition.setTextColor(getResources().getColor(R.color.primary));
        drawableHelper.setButtonDrawableFitSize(btnPlacesStartPosition, R.drawable.ic_map_marker_96, itemSizeSmall, itemSizeSmall);
    }

    private void deletePlaceStartButton() {
        btnPlacesStartPosition.setBackgroundResource(R.drawable.selector_orange_white_btn_2);
        btnPlacesStartPosition.setText(getResources().getString(R.string.reset_create_trip));
        btnPlacesStartPosition.setTextColor(getResources().getColor(R.color.accent));
        drawableHelper.setButtonDrawableFitSize(btnPlacesStartPosition, R.drawable.ic_delete_96, itemSizeSmall, itemSizeSmall);
    }

    private void insertPlaceDestinationPosition(Place place) {
        lstDestinationName.add(place.getAddress().toString());
        tetCreateTripDestination.setTags(lstDestinationName);
    }

    private void datePicker(final int type) {
        // Get Current Date
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        timePicker(type);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void timePicker(final int type) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        setDateTime(type, calendar.getTime(), ConvertTimeHelper.DATE_FORMAT_1);
                    }
                }, hour, minute, true);
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_dialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        setDateTime(type, calendar.getTime(), ConvertTimeHelper.DATE_FORMAT_2);
                    }
                });
        timePickerDialog.show();
    }

    private void displayDateTime(EditText editText, Date date, String format) {
        editText.setText(ConvertTimeHelper.convertDateToString(date, format));
    }

    private void setDateTime(int type, Date date, String format) {
        if (date.compareTo(new Date()) > 0) {
            if (type == SET_START_TIME) {
                if (endDate != null && endDate.compareTo(date) < 0) {
                    ToastUtil.showToast(this.getContext(), getResources().getString(R.string.error_start_time_after_end));
                } else {
                    startDate = date;
                    crstlaAdapter.setStartDate(startDate);
                    displayDateTime(etCreateTripStartTime, date, format);
                }
            } else if (type == SET_END_TIME) {
                if (startDate != null && startDate.compareTo(date) > 0) {
                    ToastUtil.showToast(this.getContext(), getResources().getString(R.string.error_end_time_before_start));
                } else {
                    endDate = date;
                    crstlaAdapter.setEndDate(endDate);
                    displayDateTime(etCreateTripEndTime, date, format);
                }
            }
        } else {
            ToastUtil.showToast(this.getContext(), getResources().getString(R.string.error_time_before_now));
        }
    }

    public AdventureRequest getRequest() {
        return adventureRequest;
    }

    public void uploadTrip() {
        if(StringUtil.isEmpty(etCreateTripName)){
            etCreateTripName.setError(getResources().getString(R.string.field_can_not_empty));
            etCreateTripName.requestFocus();
        } else if(StringUtil.isEmpty(tetCreateTripStartPosition)){
            tetCreateTripStartPosition.setError(getResources().getString(R.string.field_can_not_empty));
            tetCreateTripStartPosition.requestFocus();
        } else if (StringUtil.isEmpty(etCreateTripStartTime)){
            etCreateTripStartTime.setError(getResources().getString(R.string.field_can_not_empty));
            etCreateTripStartTime.requestFocus();
        } else if(StringUtil.isEmpty(etCreateTripMember)){
            etCreateTripMember.setError(getResources().getString(R.string.field_can_not_empty));
            etCreateTripMember.requestFocus();
        }else {
            SharedPref.getInstance(getContext()).putInt(CURRENT_TRIP_PRIVACY, spiPrivacy.getSelectedItemPosition());
            String url = ApiConstants.getUrl(ApiConstants.API_CREATE_TRIP);
            adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, url, getTripParams(), false);
            getTripRequestListener();
        }
    }

    private HashMap getTripParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_NAME, StringUtil.getText(etCreateTripName));
        params.put(ApiConstants.KEY_DESCRIPTION, StringUtil.getText(etCreateTripDescription));
        params.put(ApiConstants.KEY_START_POSITION, startPositionPlace.getAddress().toString());
        params.put(ApiConstants.KEY_START_AT, startDate != null ? ConvertTimeHelper.convertDateToISOFormat(startDate) : "");
        params.put(ApiConstants.KEY_END_AT, endDate != null ? ConvertTimeHelper.convertDateToISOFormat(endDate) : "");
        params.put(ApiConstants.KEY_DESTINATION_SUMMARY, StringUtil.getText(etCreateTripDescriptionSummary));
        params.put(ApiConstants.KEY_EXPENSE, StringUtil.getText(etCreateTripMoney));
        params.put(ApiConstants.KEY_AMOUNT_PEOPLE, StringUtil.getText(etCreateTripMember));
        params.put(ApiConstants.KEY_VEHICLES, getVehicles());
        params.put(ApiConstants.KEY_ROUTES, getRoutes());
        params.put(ApiConstants.KEY_IMAGES, null); // chưa có images
        params.put(ApiConstants.KEY_PREPARE, StringUtil.getText(etCreateTripToolbox));
        params.put(ApiConstants.KEY_NOTE, StringUtil.getText(etCreateTripNote));
        params.put(ApiConstants.KEY_PERMISSION, String.valueOf(spiPrivacy.getSelectedItemPosition() + 1));
        params.put(ApiConstants.KEY_TYPE, "1");
        return params;
    }

    private String getVehicles() {
        JSONArray arr = new JSONArray(tetCreateTripVehicle.getTagsIndex());
        return arr.toString() == null ? "" : arr.toString();
    }

    private String getRoutes() {
        if (lstRoutes.isEmpty()) {
            return "";
        } else {
            JSONArray arr = new JSONArray();
            Map<String, String> map;
            for(Route temp : lstRoutes){
                map = new HashMap<>();
                map.put(ApiConstants.KEY_START_AT, temp.getISOStartAt());
                map.put(ApiConstants.KEY_END_AT, temp.getISOEndAt());
                map.put(ApiConstants.KEY_TITLE, temp.getTitle());
                map.put(ApiConstants.KEY_CONTENT, temp.getContent());
                arr.put(new JSONObject(map));
            }
            return arr.toString() == null ? "" : arr.toString();
        }
    }

    private void getTripRequestListener() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                String id = JsonUtil.getString(data, ApiConstants.KEY_ID, "");
                uploadPlaces(id);
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(getContext(), errorMsg);
                if (adventureRequest.onNotifyResponseReceived != null) {
                    adventureRequest.onNotifyResponseReceived.onNotify();
                }
            }
        });
    }

    private void uploadPlaces(String idTrip) {
        lstPlaceTrip.add(new studio.crazybt.adventure.models.Place(idTrip, startPositionPlace.getAddress().toString(),
                startPositionPlace.getLatLng().latitude, startPositionPlace.getLatLng().longitude, 1));
        for (Place temp : lstDestinationPlace) {
            lstPlaceTrip.add(new studio.crazybt.adventure.models.Place(idTrip, temp.getAddress().toString(),
                    temp.getLatLng().latitude, temp.getLatLng().longitude, 2));
        }
        int leng = lstPlaceTrip.size();
        for (int i = 0; i < leng; i++) {
            uploadPlace(i);
        }
    }

    private void uploadPlace(final int index) {
        String url = ApiConstants.getUrl(ApiConstants.API_CREATE_PLACE_TRIP_MAP);
        AdventureRequest placeRequest = new AdventureRequest(getContext(), Request.Method.POST, url,
                getPlacesParams(lstPlaceTrip.get(index)), false);
        placeRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {
                if(index == lstPlaceTrip.size() - 1){
                    ToastUtil.showToast(getContext(), R.string.success_post_trip);
                    getActivity().finish();
                    RLog.i(response.toString());
                }
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {

            }
        });
    }

    private HashMap getPlacesParams(studio.crazybt.adventure.models.Place place) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_TRIP, place.getIdTrip());
        params.put(ApiConstants.KEY_ORDER, String.valueOf(place.getOrder()));
        params.put(ApiConstants.KEY_TITLE, place.getTitle());
        params.put(ApiConstants.KEY_ADDRESS, place.getAddress());
        params.put(ApiConstants.KEY_LATITUDE, String.valueOf(place.getLatitude()));
        params.put(ApiConstants.KEY_LONGITUDE, String.valueOf(place.getLongitude()));
        params.put(ApiConstants.KEY_CONTENT, place.getContent());
        params.put(ApiConstants.KEY_TYPE, String.valueOf(place.getType()));
        params.put(ApiConstants.KEY_STATUS, String.valueOf(place.getStatus()));
        RLog.i("params day: " + params.toString());
        return params;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlacesStartPosition) {
            if (!isPlaceInStartPosition) {
                isPlaceInStartPosition = true;
                this.initPlaceAutoComplete(PLACE_START_POSITION_REQUEST_CODE);
            } else {
                isPlaceInStartPosition = false;
                this.deletePlaceStartPosition();
            }
        } else if (v.getId() == R.id.btnPlacesDestination) {
            this.initPlaceAutoComplete(PLACE_DESTINATION_REQUEST_CODE);
        } else if (v.getId() == R.id.etCreateTripStartTime) {
            datePicker(SET_START_TIME);
        } else if (v.getId() == R.id.etCreateTripEndTime) {
            datePicker(SET_END_TIME);
        } else if (v.getId() == R.id.btnResetTime) {
            startDate = null;
            endDate = null;
            etCreateTripStartTime.setText("");
            etCreateTripEndTime.setText("");
        } else if (v.getId() == R.id.btnAddTripRoute) {
            if (crstlaAdapter.getStartDate() != null) {
                rvCreateTripRoute.setVisibility(View.VISIBLE);
                lstRoutes.add(new Route());
                crstlaAdapter.notifyItemInserted(lstRoutes.size() - 1);
            } else {
                ToastUtil.showToast(getContext(), R.string.error_null_start_time);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_START_POSITION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                startPositionPlace = PlaceAutocomplete.getPlace(this.getContext(), data);
                this.insertPlaceStartPostion(startPositionPlace);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this.getContext(), data);
                // TODO: Handle the error.
                RLog.i(status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == PLACE_DESTINATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                lstDestinationPlace.add(PlaceAutocomplete.getPlace(this.getContext(), data));
                this.insertPlaceDestinationPosition(lstDestinationPlace.get(lstDestinationPlace.size() - 1));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this.getContext(), data);
                // TODO: Handle the error.
                RLog.i(status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
