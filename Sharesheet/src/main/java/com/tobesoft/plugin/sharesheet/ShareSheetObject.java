package com.tobesoft.plugin.sharesheet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.nexacro.NexacroActivity;
import com.nexacro.plugin.NexacroPlugin;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;
import com.tobesoft.plugin.plugincommonlib.util.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ShareSheetObject extends NexacroPlugin {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String SVCID = "svcid";
    private static final String REASON = "reason";
    private static final String RETVAL = "returnvalue";

    private static final String CALL_BACK = "_oncallback";
    private static final String METHOD_CALLMETHOD = "callMethod";

    public static final int CODE_SUCCES = 0;
    public static final int CODE_ERROR = -1;
    public String mServiceId = "";

    public int mResizeScale;

    private Activity mActivity;
    private ShareSheetInterface mShareSheetInterface;
    private static ShareSheetObject mShareSheetObject;


    public ShareSheetObject(String objectId) {
        super(objectId);
        mShareSheetInterface = (ShareSheetInterface) NexacroActivity.getInstance();
        mShareSheetInterface.setShareSheetObject(this);

        mActivity = (Activity) NexacroActivity.getInstance();
    }

    public static ShareSheetObject getInstance() {
        return mShareSheetObject;
    }

    @Override
    public void init(JSONObject jsonObject) {

    }

    @Override
    public void release(JSONObject jsonObject) {

    }

    @Override
    public void execute(String method, JSONObject paramObject) {
        if (method.equals(METHOD_CALLMETHOD)) {
            try {
                JSONObject params = paramObject.getJSONObject("params");
                mServiceId = params.getString("serviceid");
                if (mServiceId.equals("test")) {
                    send(CODE_SUCCES, "모듈 연동 성공");
                } else if (mServiceId.equals("init")) {
                    JSONObject param = params.getJSONObject("param");

                    execute();
                }
            } catch (Exception e) {
                send(CODE_ERROR, e);
            }
        }
    }

    public void execute() {
        String sendText = PreferenceManager.getString(mActivity.getApplicationContext(), "testKey");
        try {
            JSONObject jsonObject = new JSONObject(sendText);
            String getAction = jsonObject.getString("action");
            if (getAction.equals("android.intent.action.SEND") || getAction.equals("android.intent.action.SEND_MULTIPLE")) {
                execute(jsonObject);
                Log.e(LOG_TAG, "::::::::::::::::::::::::::" + sendText);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!sendText.equals(""))
            send(CODE_SUCCES, sendText);
    }

    public void execute(JSONObject jsonObject) {
        try {
            String action = jsonObject.getString("action");
            String type = jsonObject.getString("type");
            String value = jsonObject.getString("value");

            if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
                send("text/plain", CODE_SUCCES, value);
            } else if (Intent.ACTION_SEND.equals(action) && type.startsWith("image/")) {
                send("singleImage", CODE_SUCCES, value);
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type.startsWith("image/")) {
                send("multipleImages", CODE_SUCCES, new JSONObject(value));
            } else {
                send(CODE_ERROR, jsonObject);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, String.valueOf(e));
            send(CODE_ERROR, e);
            e.printStackTrace();
        }
    }


    public boolean send(int reason, Object retval) {

        JSONObject obj = new JSONObject();

        try {
            obj.put(SVCID, mServiceId);
            obj.put(REASON, reason);
            obj.put(RETVAL, retval);

            callback(CALL_BACK, obj);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mServiceId = null;
        }
        return false;
    }

    public boolean send(String svcid, int reason, Object retval) {

        JSONObject obj = new JSONObject();

        try {
            obj.put(SVCID, svcid);
            obj.put(REASON, reason);
            obj.put(RETVAL, retval);

            callback(CALL_BACK, obj);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mServiceId = null;
        }
        return false;
    }

}
