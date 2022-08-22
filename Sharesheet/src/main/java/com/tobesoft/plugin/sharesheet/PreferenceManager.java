package com.tobesoft.plugin.sharesheet;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;

import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PreferenceManager {

    public static final String PREFERENCES_NAME = "rebuild_preference";
    private static final String DEFAULT_VALUE_STRING = "";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = -1;
    private static final long DEFAULT_VALUE_LONG = -1L;
    private static final float DEFAULT_VALUE_FLOAT = -1F;
    public static final int CODE_SUCCES = 0;
    public static final int CODE_ERROR = -1;

    private static ShareSheetObject mShareSheetObject;


    /**
     * Json 형태로 값 저장.
     *
     * @param context
     * @param key
     * @param intent
     */
    @SuppressLint("LongLogTag")
    public static void setIntentToJson(Context context, String key, Intent intent) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        String action = intent.getAction();
        String type = intent.getType();
        String someValue= "";

        ArrayList<Uri> someMultipleImageUris;

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                someValue = intent.getStringExtra(Intent.EXTRA_TEXT);
            } else if (type.startsWith("image/")) {
                someValue = intent.getParcelableExtra(Intent.EXTRA_STREAM).toString();
                Log.e(TAG, "setIntentToJson someText: " + someValue);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {

            someValue = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM).toString();
            Log.e(TAG, someValue);
        } else {
            Log.d(TAG, "Can Not Start setIntentToJson(): type = " + type);
        }
        Log.d("PreferenceManager", "setIntentToJson: " + someValue);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", action);
            jsonObject.put("type", type);
            jsonObject.put("value", someValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putString(key, String.valueOf(jsonObject));
        editor.commit();
    }


    /**
     * String 값 저장
     *
     * @param context
     * @param key
     * @param value
     */

    public static void setString(Context context, String key, String value) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        Log.d("PreferenceManager", "setString: " + value);

        editor.putString(key, value);
        editor.commit();
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * boolean 값 저장
     *
     * @param context
     * @param key
     * @param value
     */

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }


    /**
     * int 값 저장
     *
     * @param context
     * @param key
     * @param value
     */

    public static void setInt(Context context, String key, int value) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(key, value);
        editor.commit();

    }


    /**
     * long 값 저장
     *
     * @param context
     * @param key
     * @param value
     */

    public static void setLong(Context context, String key, long value) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(key, value);
        editor.commit();

    }


    /**
     * float 값 저장
     *
     * @param context
     * @param key
     * @param value
     */

    public static void setFloat(Context context, String key, float value) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat(key, value);
        editor.commit();

    }


    /**
     * String 값 로드
     *
     * @param context
     * @param key
     * @return
     */

    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);

        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        Log.d("PreferenceManager", "getString: " + value);

        return value;
    }


    /**
     * boolean 값 로드
     *
     * @param context
     * @param key
     * @return
     */

    public static boolean getBoolean(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);
        boolean value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);

        return value;

    }


    /**
     * int 값 로드
     *
     * @param context
     * @param key
     * @return
     */

    public static int getInt(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);
        int value = prefs.getInt(key, DEFAULT_VALUE_INT);

        return value;

    }


    /**
     * long 값 로드
     *
     * @param context
     * @param key
     * @return
     */

    public static long getLong(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);
        long value = prefs.getLong(key, DEFAULT_VALUE_LONG);

        return value;

    }


    /**
     * float 값 로드
     *
     * @param context
     * @param key
     * @return
     */

    public static float getFloat(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);
        float value = prefs.getFloat(key, DEFAULT_VALUE_FLOAT);

        return value;

    }


    /**
     * 키 값 삭제
     *
     * @param context
     * @param key
     */

    public static void removeKey(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();

        edit.remove(key);
        edit.commit();

    }


    /**
     * 모든 저장 데이터 삭제
     *
     * @param context
     */

    public static void clear(Context context) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();

        edit.clear();
        edit.commit();
    }

}

