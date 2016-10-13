package studio.crazybt.adventure.services;


import android.content.ContentValues;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PostRequest extends Request<JSONObject> {
    private ContentValues params;
    private Response.Listener listener;

    public PostRequest(String url, ContentValues params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        Log.d("AppLog",url);
        this.params = params;
        this.listener = responseListener;
        this.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        if (listener != null) listener.onResponse(response);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<String, String>();
        Set<Map.Entry<String, Object>> set = params.valueSet();
        Iterator itr = set.iterator();
        StringBuilder pr = new StringBuilder();
        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry) itr.next();
            map.put(me.getKey().toString(), me.getValue().toString());
            pr.append(me.getKey().toString()).append("=").append(me.getValue().toString()).append("&");
        }
        Log.d("AppLog", pr.deleteCharAt(pr.length() - 1).toString());
        return map;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "close");
        return headers;
    }
}
