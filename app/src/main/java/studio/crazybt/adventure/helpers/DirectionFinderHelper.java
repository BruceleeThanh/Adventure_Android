package studio.crazybt.adventure.helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.listeners.OnDirectionFinderListener;
import studio.crazybt.adventure.models.Direction;
import studio.crazybt.adventure.models.Distance;
import studio.crazybt.adventure.models.Duration;
import studio.crazybt.adventure.services.CustomRequest;
import studio.crazybt.adventure.services.MySingleton;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 07/07/2017.
 */

public class DirectionFinderHelper {

    private OnDirectionFinderListener listener;

    public DirectionFinderHelper(final Context context, OnDirectionFinderListener listener, LatLng origin, LatLng destination) {
        this.listener = listener;
        this.listener.onDirectionFinderStart();
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, ApiConstants.getGoogleMapDirectionUrl(origin, destination), new HashMap<String, String>(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJSon(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showToast(context.getResources().getString(R.string.error_direction_map));
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(customRequest, false);
    }

    private void parseJSon(JSONObject data) {
        if (data == null)
            return;

        List<Direction> Directions = new ArrayList<Direction>();
        JSONArray jsonDirections = JsonUtil.getJSONArray(data, "routes");
        if (jsonDirections != null)
            for (int i = 0; i < jsonDirections.length(); i++) {
                JSONObject jsonDirection = JsonUtil.getJSONObject(jsonDirections, i);
                Direction Direction = new Direction();

                JSONObject overview_polylineJson = JsonUtil.getJSONObject(jsonDirection, "overview_polyline");
                JSONArray jsonLegs = JsonUtil.getJSONArray(jsonDirection, "legs");
                JSONObject jsonLeg = JsonUtil.getJSONObject(jsonLegs, 0);
                JSONObject jsonDistance = JsonUtil.getJSONObject(jsonLeg, "distance");
                JSONObject jsonDuration = JsonUtil.getJSONObject(jsonLeg, "duration");
                JSONObject jsonEndLocation = JsonUtil.getJSONObject(jsonLeg, "end_location");
                JSONObject jsonStartLocation = JsonUtil.getJSONObject(jsonLeg, "start_location");

                Direction.distance = new Distance(JsonUtil.getString(jsonDistance, "text", null), JsonUtil.getInt(jsonDistance, "value", 0));
                Direction.duration = new Duration(JsonUtil.getString(jsonDuration, "text", null), JsonUtil.getInt(jsonDuration, "value", 0));
                Direction.endAddress = JsonUtil.getString(jsonLeg, "end_address", null);
                Direction.startAddress = JsonUtil.getString(jsonLeg, "start_address", null);
                Direction.startLocation = new LatLng(JsonUtil.getDouble(jsonStartLocation, "lat", 0), JsonUtil.getDouble(jsonStartLocation, "lng", 0));
                Direction.endLocation = new LatLng(JsonUtil.getDouble(jsonEndLocation, "lat", 0), JsonUtil.getDouble(jsonEndLocation, "lng", 0));
                Direction.points = decodePolyLine(JsonUtil.getString(overview_polylineJson, "points", null));

                Directions.add(Direction);
            }

        listener.onDirectionFinderSuccess(Directions);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
