package studio.crazybt.adventure.services;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.ServerError;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.utils.JsonUtil;

/**
 * Created by Brucelee Thanh on 18/05/2017.
 */

public class AdventureFileRequest {

    private MultipartRequest multipartRequest;
    private Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0) == 1) {
                if (onAdventureFileRequestListener != null)
                    onAdventureFileRequestListener.onAdventureFileResponse(response);
            } else if (onAdventureFileRequestListener != null)
                onAdventureFileRequestListener.onAdventureFileError(JsonUtil.getInt(response, ApiConstants.DEF_CODE, 0),
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
            if (onAdventureFileRequestListener != null)
                onAdventureFileRequestListener.onAdventureFileError(-404, "Sự cố kết nối. Vui lòng kiểm tra kết nối mạng hoặc thử lại sau!");
        }
    };

    public AdventureFileRequest(Context context, String url, Map<String, String> params, Map<String , DataPart> byteData, boolean hasCache) {
        multipartRequest = new MultipartRequest(url, params, byteData, response, error);
        MySingleton.getInstance(context).addToRequestQueue(multipartRequest, hasCache);
    }

    private OnAdventureFileRequestListener onAdventureFileRequestListener;
    public OnNotifyResponseReceived onNotifyResponseReceived;

    public void setOnAdventureFileRequestListener(OnAdventureFileRequestListener listener) {
        onAdventureFileRequestListener = listener;
    }

    public void setOnNotifyResponseReceived(OnNotifyResponseReceived listener){
        onNotifyResponseReceived = listener;
    }

    public interface OnAdventureFileRequestListener {
        void onAdventureFileResponse(JSONObject response);
        void onAdventureFileError(int errorCode, String errorMsg);
    }

    public interface OnNotifyResponseReceived{
        void onNotify();
    }
}
