package studio.crazybt.adventure.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import studio.crazybt.adventure.libs.ApiConstants;

/**
 * Created by Brucelee Thanh on 09/10/2016.
 */

public class SharedPref {

    private static final String SHARED_PREF_KEY = "SHARED_PREF_KEY";
    private static SharedPref instance;
    private SharedPreferences preferences;

    public static void clearAllData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    private SharedPref(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Use to initialization and get object MyPreferenceII
     *
     * @param context
     */
    public static SharedPref getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPref(context);
        }

        return instance;
    }

    /**
     * Get the Access Token of current user
     *
     * @return return value of Access Token if it exist, else return null
     */
    public String getAccessToken() {
        return preferences.getString(ApiConstants.KEY_TOKEN, null);
    }

    /**
     * Set an int value in the preferences editor
     *
     * @param key   : name of preference to modify
     * @param value : the new value of preference
     * @return : MyPreferenceII to continue edit
     */
    public SharedPref putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
        return instance;
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key          : the name of preference to retrieve
     * @param defaultValue : value to return if this preference does not exist
     * @return : return the preference value if it exist, else return default
     * value
     */
    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    /**
     * Set a long value in preference editor
     *
     * @param key   : name of preference to modify
     * @param value : the new value of preference
     * @return : MyPreferenceII to continue edit
     */
    public SharedPref putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
        return instance;
    }

    /**
     * Retrieve an long value from the preferences
     *
     * @param key          : the name of preference to retrieve
     * @param defaultValue : value to return if this preference does not exist
     * @return : return the preference value if exist, else return default value
     */
    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    /**
     * Set a String value in preference editor
     *
     * @param key   : name of preference to modify
     * @param value : the new value of preference
     * @return : return MyPreferenceII to continue edit
     */
    public SharedPref putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
        return instance;
    }

    /**
     * Retrieve a String value from the preference
     *
     * @param key          : the name of preference to retrieve
     * @param defaultValue : value to return if preference does not exist
     * @return : return the preference value if exist, else return default value
     */
    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    /**
     * Set a boolean value in preference editor
     *
     * @param key   : name of preference to modify
     * @param value : the new value of preference
     * @return : return MyPreferenceII to continue edit
     */
    public SharedPref putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
        return instance;
    }

    /**
     * Retrieve a boolean value from the preferences
     *
     * @param key          : the name of preference to retrieve
     * @param defaultValue : value to return if preference does not exist
     * @return : return the preference value if exist, else return default value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * Set a float value in preference editor
     *
     * @param key   : name of preference to modify
     * @param value : the new value of preference
     * @return : return MyPreferenceII to continue edit
     */
    public SharedPref putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
        return instance;
    }

    /**
     * Retrieve a float value from the preferences
     *
     * @param key          : the name of preference to retrieve
     * @param defaultValue : value to return if preference dose not exist
     * @return : return the preference value if exist, else return default value
     */
    public float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

}
