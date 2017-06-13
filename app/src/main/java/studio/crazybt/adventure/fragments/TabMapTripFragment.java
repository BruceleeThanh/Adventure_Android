package studio.crazybt.adventure.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.models.Place;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 12/09/2016.
 */
public class TabMapTripFragment extends Fragment {

    private View rootView;
    private SupportMapFragment supportMapFragment;
    private GoogleMap map;
    private LocationManager locationManager;

    private List<Place> lstPlace;
    private DrawableHelper drawableHelper;

    @BindDimen(R.dimen.item_icon_size_big)
    float itemSizeBig;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_map_trip, container, false);
        }
        ButterKnife.bind(this, rootView);
        initControl();
        prepareMap();
        return rootView;
    }

    private void initControl() {
        drawableHelper = new DrawableHelper(getContext());
        lstPlace = new ArrayList<>();
    }

    public List<Place> getLstPlace() {
        return lstPlace;
    }

    public void setLstPlace(List<Place> lstPlace) {
        this.lstPlace = lstPlace;
        initData();
    }

    private void prepareMap() {
        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.fmMapTrip);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.fmMapTrip, supportMapFragment).commit();
        }
        if (map == null) {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    if (!lstPlace.isEmpty()) {
                        initData();
                    }
                }
            });
/*            if (ActivityCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);

            if (Build.VERSION.SDK_INT >= 10) {
                int accessCoarsePermission
                        = ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
                int accessFinePermission
                        = ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);


                if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                        || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

                    // Các quyền cần người dùng cho phép.
                    String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION};

                    // Hiển thị một Dialog hỏi người dùng cho phép các quyền trên.
                    ActivityCompat.requestPermissions(this.getActivity(), permissions, REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);

                }
            }*/
        }
    }

    private void currentLocation() {
        Location lastLocation = this.getLastKnownLocation();
        if (lastLocation != null) {
            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //Thêm MarketOption cho Map:
            MarkerOptions option = new MarkerOptions();
            option.title("Bạn đang ở đây!");
            //option.snippet("Near some where.");
            option.position(latLng);
            Marker currentMarker = map.addMarker(option);
            currentMarker.showInfoWindow();
        } else {
            Toast.makeText(this.getContext(), "Không thể tìm thấy vị trí của bạn.", Toast.LENGTH_SHORT).show();
        }
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) this.getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void initData() {

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        if (map != null) {
            if (!lstPlace.isEmpty()) {
                if (lstPlace.size() == 1) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(lstPlace.get(0).getLatitude(), lstPlace.get(0).getLongitude()), 13));
                } else {
                    LatLng latLng1 = new LatLng(lstPlace.get(0).getLatitude(), lstPlace.get(0).getLongitude());
                    LatLng latLng2 = new LatLng(lstPlace.get(1).getLatitude(), lstPlace.get(1).getLongitude());
                    for (Place temp : lstPlace) {
                        if (temp.getType() == 1) {
                            latLng1 = new LatLng(temp.getLatitude(), temp.getLongitude());
                        } else {
                            latLng2 = new LatLng(temp.getLatitude(), temp.getLongitude());
                        }
                    }

                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                            .include(latLng1).include(latLng2).build();
                    int margin = getResources().getDimensionPixelSize(R.dimen.margin_big);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, margin);
                    map.animateCamera(cameraUpdate);
                }
                MarkerOptions marker;
                for (Place temp : lstPlace) {
                    marker = new MarkerOptions();
                    if (temp.getType() == 1) {
                        marker.icon(BitmapDescriptorFactory.fromBitmap(drawableHelper.resizeMapIcons(R.drawable.ic_flag_filled_96, itemSizeBig, itemSizeBig)));
                    } else if (temp.getType() == 2) {
                        marker.icon(BitmapDescriptorFactory.fromBitmap(drawableHelper.resizeMapIcons(R.drawable.ic_marker_normal_red_96, itemSizeBig, itemSizeBig)));
                    } else {
                        marker.icon(BitmapDescriptorFactory.fromBitmap(drawableHelper.resizeMapIcons(R.drawable.ic_marker_normal_yellow_96, itemSizeBig, itemSizeBig)));
                    }
                    marker.title(temp.getAddress());
                    marker.position(new LatLng(temp.getLatitude(), temp.getLongitude()));
                    map.addMarker(marker);
                }
            } else {
                ToastUtil.showToast(getContext(), getResources().getString(R.string.error_null_places));
            }
        }
    }

}
