package com.jack.autostart.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.jack.autostart.app.BaseApplication;

public final class SharedPrefsUtils {

    /**
     * Helper method to retrieve a String value from {@link SharedPreferences}.
     *
     * @param key Preference key
     * @return The value from shared preferences, or null if the value could not be read.
     */
    public static String getStringPreference(String key) {
        String value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            value = preferences.getString(key, "");
        }
        return value;
    }

    /**
     * Helper method to retrieve a String value from {@link SharedPreferences}.
     *
     * @param key      Preference key
     * @param defValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static String getStringPreference(String key, String defValue) {
        String value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            value = preferences.getString(key, defValue);
        }
        return value;
    }

    /**
     * Helper method to write a String value to {@link SharedPreferences}.
     *
     * @param key   Preference key
     * @param value Preference value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setStringPreference(String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a float value from {@link SharedPreferences}.
     *
     * @param key          Preference key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static float getFloatPreference(String key, float defaultValue) {
        float value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            value = preferences.getFloat(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a float value to {@link SharedPreferences}.
     *
     * @param key   Preference key
     * @param value Preference value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setFloatPreference(String key, float value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a long value from {@link SharedPreferences}.
     *
     * @param key          Preference key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static long getLongPreference(String key, long defaultValue) {
        long value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            value = preferences.getLong(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a long value to {@link SharedPreferences}.
     *
     * @param key   Preference key
     * @param value Preference value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setLongPreference(String key, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve an integer value from {@link SharedPreferences}.
     *
     * @param key          Preference key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static int getIntegerPreference(String key, int defaultValue) {
        int value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            value = preferences.getInt(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write an integer value to {@link SharedPreferences}.
     *
     * @param key   Preference key
     * @param value Preference value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setIntegerPreference(String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a boolean value from {@link SharedPreferences}.
     *
     * @param key          Preference key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static boolean getBooleanPreference(String key, boolean defaultValue) {
        boolean value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a boolean value to {@link SharedPreferences}.
     *
     * @param key   Preference key
     * @param value Preference value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setBooleanPreference(String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to get the SharedPreferences instance that points to the default file that is used by the preference framework
     *
     * @return The SharedPreferences instance that can be used to retrieve and listen to values of the preferences
     */
    public static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
    }

    /**
     * Helper method to get the name used for storing default shared preferences.
     *
     * @return The name used for storing default shared preferences
     */
    public static String getDefaultSharedPreferencesName() {
        return PreferenceManager.getDefaultSharedPreferencesName(BaseApplication.getContext());
    }

    /**
     * Helper method to remove a value from {@link SharedPreferences}.
     *
     * @param key Preference key
     */
    public static void removePreference(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key).apply();
        }
    }

    /**
     * Helper method to remove all values from {@link SharedPreferences}.
     */
    public static void clearPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (preferences != null) {
            preferences.edit().clear().apply();
        }
    }
}