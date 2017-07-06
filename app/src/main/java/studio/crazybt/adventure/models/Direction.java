package studio.crazybt.adventure.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Brucelee Thanh on 07/07/2017.
 */

public class Direction {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;

}
