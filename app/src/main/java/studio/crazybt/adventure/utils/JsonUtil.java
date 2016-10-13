package studio.crazybt.adventure.utils;

import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dunghn on 15/01/2016.
 */
public class JsonUtil {

    public static JSONObject createJSONObject(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return null;
        }
    }

    public static JSONArray createJSONArray(String jsonString) {
        try {
            return new JSONArray(jsonString);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONObject obj, String key) {
        try {
            return obj.getJSONObject(key);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONArray array, int index) {
        try {
            return array.getJSONObject(index);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return null;
        }
    }

    public static JSONArray getJSONArray(JSONObject obj, String key) {
        try {
            return obj.getJSONArray(key);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return null;
        }
    }

    public static String getString(JSONObject obj, String key, String defaultValue) {
        try {
            return obj.getString(key);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static String getString(JSONArray array, int index, String defaultValue) {
        try {
            return array.getString(index);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static boolean getBoolean(JSONObject obj, String key, boolean defaultValue) {
        try {
            return obj.getBoolean(key);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static boolean getBoolean(JSONArray array, int index, boolean defaultValue) {
        try {
            return array.getBoolean(index);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static int getInt(JSONObject obj, String key, int defaultValue) {
        try {
            return obj.getInt(key);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static int getInt(JSONArray array, int index, int defaultValue) {
        try {
            return array.getInt(index);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static long getLong(JSONObject obj, String key, long defaultValue) {
        try {
            return obj.getLong(key);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static long getLong(JSONArray array, int index, long defaultValue) {
        try {
            return array.getLong(index);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static double getDouble(JSONObject obj, String key, double defaultValue) {
        try {
            return obj.getDouble(key);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static double getDouble(JSONArray array, int index, double defaultValue) {
        try {
            return array.getDouble(index);
        } catch (JSONException e) {
            RLog.e(e.getMessage());
            return defaultValue;
        }
    }

    public static JSONArray removeItem(JSONArray array, int index) {
        try {
            if (index < 0) return array;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                JSONArray jsaNew = new JSONArray();
                for (int i = 0; i < array.length(); i++)
                    if (i != index)
                        jsaNew.put(array.get(i));
                return jsaNew;
            } else {
                array.remove(index);
                return array;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return array;
        }
    }
}
