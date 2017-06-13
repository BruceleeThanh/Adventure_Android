package studio.crazybt.adventure.services;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.ServerError;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.utils.JsonUtil;

/**
 * Created by Brucelee Thanh on 15/12/2016.
 */

public class AdventureRequest {

    private CustomRequest customRequest;
    private Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                if (onAdventureRequestListener != null)
                    onAdventureRequestListener.onAdventureResponse(response);
            } else if (onAdventureRequestListener != null)
                onAdventureRequestListener.onAdventureError(JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0),
                        JsonUtil.getString(response, ApiConstants.DEF_MSG, ""));

        }
    };

    private Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            NetworkResponse response = error.networkResponse;
            if (error instanceof ServerError && response != null) {
                try {
                    String res = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    // Now you can use any deserializer to make sense of data
                    JSONObject obj = new JSONObject(res);
                } catch (UnsupportedEncodingException e1) {
                    // Couldn't properly decode data to string
                    e1.printStackTrace();
                } catch (JSONException e2) {
                    // returned data is not JSONObject?
                    e2.printStackTrace();
                }
            }
            if (onAdventureRequestListener != null)
                onAdventureRequestListener.onAdventureError(-404, "Sự cố kết nối. Vui lòng kiểm tra kết nối mạng hoặc thử lại sau!");
        }
    };

    public AdventureRequest (){

    }

    public AdventureRequest(Context context, int method, String url, Map<String, String> params, boolean hasCache) {
        customRequest = new CustomRequest(method, url, params, response, error);
        MySingleton.getInstance(context).addToRequestQueue(customRequest, hasCache);
    }

    public AdventureRequest(int method, String url) {
        customRequest = new CustomRequest(method, url, response, error);
    }

    public AdventureRequest(int method, String url, Map<String, String> params) {
        customRequest = new CustomRequest(method, url, params, response, error);
    }

    public void execute(Context context, boolean hasCache){
        MySingleton.getInstance(context).addToRequestQueue(customRequest, hasCache);
    }

    public void setParams(Map<String, String> params) {
        customRequest.setParams(params);
    }

    public void setUrl(String url){
        customRequest.setUrl(url);
    }

    private OnAdventureRequestListener onAdventureRequestListener;
    public OnNotifyResponseReceived onNotifyResponseReceived;

    public void setOnAdventureRequestListener(OnAdventureRequestListener listener) {
        onAdventureRequestListener = listener;
    }

    public void setOnNotifyResponseReceived(OnNotifyResponseReceived listener){
        onNotifyResponseReceived = listener;
    }

    public interface OnAdventureRequestListener {
        void onAdventureResponse(JSONObject response);
        void onAdventureError(int errorCode, String errorMsg);
    }

    public interface OnNotifyResponseReceived{
        void onNotify();
    }
}
