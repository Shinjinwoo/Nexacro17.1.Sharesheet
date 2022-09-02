package com.tobesoft.plugin.sharesheetobject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.kh.plugin.plugincommonlib.info.PermissionRequest;
import com.kh.plugin.plugincommonlib.util.PermissionUtil;
import com.nexacro.NexacroActivity;
import com.nexacro.plugin.NexacroPlugin;
import com.tobesoft.plugin.sharesheetobject.plugininterface.ShareSheetInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShareSheetObject extends NexacroPlugin {


    //ShareSheet < Object >

    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String SVCID = "svcid";
    private static final String REASON = "reason";
    private static final String RETVAL = "returnvalue";

    private static final String CALL_BACK = "_oncallback";
    private static final String METHOD_CALLMETHOD = "callMethod";

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = -1;
    public String mServiceId = "";

    public int mResizeScale;

    private Activity mActivity;
    private ShareSheetInterface mShareSheetInterface;

    public ShareSheetObject(String objectId) {
        super(objectId);
        mShareSheetInterface = (ShareSheetInterface) NexacroActivity.getInstance();
        mShareSheetInterface.setShareSheetObject(this);

        mActivity = (Activity) NexacroActivity.getInstance();
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
                if (mServiceId.equals("callSharedData")) {
                    callSharedData();
                } else if (mServiceId.equals("sharesDataOtherApp")) {

                    JSONObject param = params.getJSONObject("param");

                    String type = param.optString("type", "text/plain");
                    String sendData = param.getString("sendData");


                    if (type.equals("text/plain")) {
                        sendTextDataToOtherApp(sendData);
                    } else if (type.equals("singleImage")) {
                        if (isCheckPermission()) {
                            sendImageToOtherApp(sendData);
                        }
                    } else if (type.equals("multipleImages")) {
                        if (isCheckPermission()) {
                            sendMultipleImagesToOtherApp(sendData);
                        }
                    } else {
                        send(CODE_ERROR, "Invalid Type");
                    }
                }
            } catch (Exception e) {
                send(CODE_ERROR, e);
            }
        }
    }

    public void callSharedData() {
        String sendData = PreferenceManager.getString(mActivity.getApplicationContext(), "SharesObjectKey");
        PreferenceManager.removeKey(mActivity.getApplicationContext(), "SharesObjectKey");
        if (!sendData.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(sendData);

                String getAction = jsonObject.getString("action");
                if (!getAction.equals("android.intent.action.MAIN")) {

                    String getType = jsonObject.getString("type");
                    String getValue = jsonObject.getString("value");

                    if (!getValue.equals("")) {
                        if (getAction.equals("android.intent.action.SEND") || getAction.equals("android.intent.action.SEND_MULTIPLE")) {
                            if (Intent.ACTION_SEND.equals(getAction) && "text/plain".equals(getType)) {
                                send("text/plain", CODE_SUCCESS, getValue);
                            } else if (Intent.ACTION_SEND.equals(getAction) && getType.startsWith("image/")) {
                                send("singleImage", CODE_SUCCESS, getValue);
                            } else if (Intent.ACTION_SEND_MULTIPLE.equals(getAction) && getType.startsWith("image/")) {
                                send("multipleImages", CODE_SUCCESS, new JSONObject(getValue));
                            } else {
                                send(CODE_ERROR, jsonObject);
                            }
                        } send (CODE_ERROR,METHOD_CALLMETHOD+ " : Unrecognized Action");
                    } else {
                        send(CODE_ERROR,METHOD_CALLMETHOD+ "No Value For Send");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                send(CODE_ERROR, e);
            }
        }
    }

//    public void callSharedDataByJsonObject(JSONObject jsonObject) {
//        // execute() 메소드병 바꾸기.
//        try {
//            String action = jsonObject.getString("action");
//            String type = jsonObject.getString("type");
//            String value = jsonObject.getString("value");
//
//            if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
//                send("text/plain", CODE_SUCCESS, value);
//            } else if (Intent.ACTION_SEND.equals(action) && type.startsWith("image/")) {
//                send("singleImage", CODE_SUCCESS, value);
//            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type.startsWith("image/")) {
//                send("multipleImages", CODE_SUCCESS, new JSONObject(value));
//            } else {
//                send(CODE_ERROR, jsonObject);
//            }
//
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, String.valueOf(e));
//            send(CODE_ERROR, e);
//            e.printStackTrace();
//        }
//    }


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


    public boolean isCheckPermission() {

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> requestPermissions = PermissionUtil.hasPermissions(mActivity, permissions);

        if (!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity, requestPermissions.toArray(new String[requestPermissions.size()]), PermissionRequest.SHARESSHEET_PERMISSION_REQUEST);

            isCheckPermission();
            return false;
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionRequest.SHARESSHEET_PERMISSION_REQUEST: {
                boolean isPermissionGranted = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d(LOG_TAG, "permissions[" + i + "] = " + permissions[i] + " : PERMISSION_DENIED");
                        isPermissionGranted = false;
                    }
                }
                if (isPermissionGranted) {
                    try {
                        send(CODE_SUCCESS, "Permission Granted");
                    } catch (Exception e) {
                        send(CODE_ERROR, METHOD_CALLMETHOD + ":" + e.getMessage());
                    }
                } else {
                    send(CODE_ERROR, "permission denied");
                }
                break;
            }
        }
    }


    public void sendTextDataToOtherApp(String sendText) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sendText);
        shareIntent.setType("text/plain");

        mActivity.startActivity(Intent.createChooser(shareIntent, null));
    }

    public void sendImageToOtherApp(String filePath) {
        Log.e(LOG_TAG, filePath);
        Uri fileURI = getUriFromPath(filePath);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileURI);
        shareIntent.setType("image/*");

        mActivity.startActivity(Intent.createChooser(shareIntent, null));
    }


    public void sendMultipleImagesToOtherApp(String files) {
        Log.e(LOG_TAG, files);

        ArrayList<Uri> imageUriList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(files);

            for (int i = 0; i < jsonArray.length(); i++) {
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

        mActivity.startActivity(Intent.createChooser(shareIntent, null));
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
