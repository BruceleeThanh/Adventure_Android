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

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultipartRequest extends Request<JSONObject> {
    private MultipartEntityBuilder entityBuilder;
    private ContentValues params;
    private Response.Listener listener;

    public MultipartRequest(String url, String fileKey, File file, ContentValues params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        Log.e("Log",url.toString());
        this.params = params;
        this.listener = responseListener;

        entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));

        StringBuilder pr = new StringBuilder();
        Iterator itr = params.valueSet().iterator();
        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry) itr.next();
            entityBuilder.addTextBody(me.getKey().toString(), me.getValue().toString());
            pr.append(me.getKey().toString()).append("=").append(me.getValue().toString()).append("&");
        }
        entityBuilder.addPart(fileKey, new FileBody(file));
        entityBuilder.build();
        pr.append(fileKey).append("=").append(file.getAbsolutePath());
        Log.e("Log nhé: ",pr.toString());
        this.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
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
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "close");
        headers.put("Accept", "application/json");
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return entityBuilder.build().getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entityBuilder.build().writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }
}
