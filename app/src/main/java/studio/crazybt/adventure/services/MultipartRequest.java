package studio.crazybt.adventure.services;

//
//import android.util.Log;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.error.AuthFailureError;
//import com.android.volley.error.ParseError;
//import com.android.volley.toolbox.HttpHeaderParser;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MultipartRequest extends Request<String> {
//    private MultipartEntityBuilder entityBuilder;
//    private Response.Listener listener;
//
//    public MultipartRequest(String url, String fileKey, File file, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
//        super(Method.POST, url, errorListener);
//        Log.e("Log",url.toString());
//        this.listener = responseListener;
//
//        entityBuilder = MultipartEntityBuilder.create();
//        entityBuilder.addPart(fileKey, new FileBody(file, ContentType.create("image/*"), file.getName()));
//        //entityBuilder.addBinaryBody(fileKey, file, ContentType.create("image/jpeg"), file.getName());
//        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        entityBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
//        entityBuilder.build();
//        this.setRetryPolicy(new DefaultRetryPolicy(
//                30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//
//    @Override
//    protected Response<String> parseNetworkResponse(NetworkResponse response) {
//        try {
//            String stringResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            return Response.success(stringResponse, HttpHeaderParser.parseCacheHeaders(response));
//        } catch (UnsupportedEncodingException e) {
//            return Response.error(new ParseError(e));
//        }
//    }
//
//    @Override
//    protected void deliverResponse(String response) {
//        if (listener != null) listener.onResponse(response);
//    }
//
//
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        return super.getHeaders();
//    }
//
//    @Override
//    public String getBodyContentType() {
//        return entityBuilder.build().getContentType().getValue();
//    }
//
//    @Override
//    public byte[] getBody() throws AuthFailureError {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        DataOutputStream dos = new DataOutputStream(bos);
//        try {
//            entityBuilder.build().writeTo(dos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bos.toByteArray();
//    }
//}


//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyLog;
//import com.android.volley.error.AuthFailureError;
//import com.android.volley.toolbox.HttpHeaderParser;
//
//
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
//
//public class MultipartRequest extends Request<String> {
//
//    private MultipartEntityBuilder entityBuilder;
//
//    private static final String FILE_PART_NAME = "file";
//    private static final String STRING_PART_NAME = "text";
//
//    private final Response.Listener<String> mListener;
//    private final File mFilePart;
//    private final String mStringPart;
//
//    public MultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, File file, String stringPart) {
//        super(Method.POST, url, errorListener);
//
//        mListener = listener;
//        mFilePart = file;
//        mStringPart = stringPart;
//        buildMultipartEntity();
//    }
//
//    private void buildMultipartEntity() {
//        entityBuilder = MultipartEntityBuilder.create();
//        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        entityBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
//        entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));
//        try {
//            entity.addPart(STRING_PART_NAME, new StringBody(mStringPart));
//        } catch (UnsupportedEncodingException e) {
//            VolleyLog.e("UnsupportedEncodingException");
//        }
//    }
//
//    @Override
//    public String getBodyContentType() {
//        return entity.getContentType().getValue();
//    }
//
//    @Override
//    public byte[] getBody() throws AuthFailureError {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try {
//            entity.writeTo(bos);
//        } catch (IOException e) {
//            VolleyLog.e("IOException writing to ByteArrayOutputStream");
//        }
//        return bos.toByteArray();
//    }
//
//    @Override
//    protected Response<String> parseNetworkResponse(NetworkResponse response) {
//        String stringResponse = null;
//        try {
//            stringResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return Response.success(stringResponse, HttpHeaderParser.parseCacheHeaders(response));
//    }
//
//    @Override
//    protected void deliverResponse(String response) {
//        mListener.onResponse(response);
//    }
//}


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Custom request to make multipart header and upload file.
 *
 * Sketch Project Studio
 * Created by Angga on 27/04/2016 12.05.
 */
public class MultipartRequest extends Request<JSONObject> {
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();

    private Response.Listener mListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private Map<String, DataPart> mByteData;

//    /**
//     * Default constructor with predefined header and post method.
//     *
//     * @param url           request destination
//     * @param headers       predefined custom header
//     * @param listener      on success achieved 200 code from request
//     * @param errorListener on error http or library timeout
//     */
//    public MultipartRequest(String url, Map<String, String> headers,
//                                  Response.Listener<NetworkResponse> listener,
//                                  Response.ErrorListener errorListener) {
//        super(Method.POST, url, errorListener);
//        this.mListener = listener;
//        this.mErrorListener = errorListener;
//        this.mHeaders = headers;
//    }

    /**
     * Constructor with option method and default header configuration.
     *
     * @param url           request destination
     * @param listener      on success event handler
     * @param errorListener on error event handler
     */
    public MultipartRequest(String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    /**
     * Constructor with option method and default header configuration.
     *
     * @param url           request destination
     * @param params        parameters
     * @param byteData      file convert to byte data
     * @param listener      on success event handler
     * @param errorListener on error event handler
     */
    public MultipartRequest(String url, Map<String ,String > params, Map<String, DataPart> byteData,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mParams = params;
        this.mByteData = byteData;
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // populate text payload
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, getParamsEncoding());
            }

            // populate data byte payload
            Map<String, DataPart> data = getByteData();
            if (data != null && data.size() > 0) {
                dataParse(dos, data);
            }

            // close multipart form data after text and file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        if (mListener != null) mListener.onResponse(response);
    }

    /**
     * Custom method handle data payload.
     *
     * @return Map data part label with data byte
     * @throws AuthFailureError
     */
    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return mByteData;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    /**
     * Parse string map into data output stream by key and value.
     *
     * @param dataOutputStream data output stream handle string parsing
     * @param params           string inputs collection
     * @param encoding         encode the inputs, default UTF-8
     * @throws IOException
     */
    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    /**
     * Parse data into data output stream.
     *
     * @param dataOutputStream data output stream handle file attachment
     * @param data             loop through data
     * @throws IOException
     */
    private void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> data) throws IOException {
        for (Map.Entry<String, DataPart> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    /**
     * Write string data into header and data output stream.
     *
     * @param dataOutputStream data output stream handle string parsing
     * @param parameterName    name of input
     * @param parameterValue   value of input
     * @throws IOException
     */
    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    /**
     * Write data file into header and data output stream.
     *
     * @param dataOutputStream data output stream handle data parsing
     * @param dataFile         data byte as DataPart from collection
     * @param inputName        name of data input
     * @throws IOException
     */
    private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String inputName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + lineEnd);
        }
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

}