package com.tobesoft.plugin.sharesheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.nexacro.NexacroActivity;
import com.nexacro.plugin.NexacroPlugin;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
                    execute();
                } else if (mServiceId.equals("sharesDataOtherApp")){
                    JSONObject param = params.getJSONObject("param");

                    String type = param.optString("type","text/plain");
                    String sendData = param.getString("sendData");

                    if (type.equals("text/plain")) {
                        sendTextDataToOtherApp(sendData);
                    } else if ( type.equals("singleImage")){
                        sendImageUriToOtherApp(sendData);
                    } else if (type.equals("multipleImages")) {
                        sendMultipleImagesToOtherApp(sendData);
                    }
                }
            } catch (Exception e) {
                send(CODE_ERROR, e);
            }
        }
    }

    public void execute() {
        String sendText = PreferenceManager.getString(mActivity.getApplicationContext(), "SharesObjectKey");
        if (!sendText.equals("")) {
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
        }
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


    public void sendTextDataToOtherApp(String sendText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        mActivity.startActivity(shareIntent);
    }

    public void sendImageUriToOtherApp(String filePath) {
        Log.e(LOG_TAG,filePath);
        Uri fileURI = getUriFromPath(filePath);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileURI);
        shareIntent.setType("image/jpeg");

        mActivity.startActivity(Intent.createChooser(shareIntent,null));
    }


    public void sendMultipleImagesToOtherApp(String files) {
        Log.e(LOG_TAG,files);

        ArrayList<Uri> imageUriList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(files);

            for (int i = 0; i<jsonArray.length(); i++ ) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                imageUriList.add(getUriFromPath(jsonObject.getString("fullpath")));
                Log.e(LOG_TAG, String.valueOf(jsonObject));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(LOG_TAG, String.valueOf(imageUriList));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUriList);
        shareIntent.setType("image/*");

        mActivity.startActivity(Intent.createChooser(shareIntent,null));

    }



    public Uri getUriFromPath(String path) {
        String fileName = "file://" + path;
        Uri fileUri = Uri.parse(fileName);
        String filePath = fileUri.getPath();
        Cursor cursor = mActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);
        cursor.moveToNext();
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        return uri;
    }
}
