package com.tobesoft.plugin.sharesheet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.nexacro.NexacroActivity;
import com.nexacro.plugin.NexacroPlugin;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;
import com.tobesoft.plugin.plugincommonlib.util.ImageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private static ShareSheetObject mShareSheetObject;

    private Activity mActivity;
    private ShareSheetInterface mShareSheetInterface;


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
                }
            } catch (Exception e) {
                send(CODE_ERROR, e);
            }
        }
    }

    public void execute(JSONObject jsonObject) {
        try {
            String action = jsonObject.getString("action");
            String type = jsonObject.getString("type");
            String value = jsonObject.getString("value");


            if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
                handleSendText(value);
            } else if (Intent.ACTION_SEND.equals(action) && type.startsWith("image/")) {
                handleSendImage(value);
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type.startsWith("image/")) {
                handleSendMultipleImages(value);
            } else {
                send(CODE_ERROR, jsonObject);
            }

        } catch (JSONException e) {
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


    private void handleSendText(String value) {
        if (value != null) {
            send("text/plain", CODE_SUCCES, value);
        }
    }

    public void handleSendImage(String value) {
        Uri imageUri = Uri.parse(value);
        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageUri);
                ImageUtil imageUtil = ImageUtil.getInstance();

                Bitmap resizeBitmap = imageUtil.resizeBitmap(bitmap, 400);
                String sharedImage = imageUtil.bitmapToBase64(resizeBitmap);

                Log.d(LOG_TAG, sharedImage);

                send("singleImage", CODE_SUCCES, sharedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleSendMultipleImages(String value) {

        ArrayList<Uri> someMultipleImageUris = new ArrayList<>();

        String optimizationString = value.replace("[", "");
        String finalOptimizationString = optimizationString.replace("]", "");

        List<String> someMultipleImageUrisToString = new ArrayList<>(Arrays.asList(finalOptimizationString.split(",")));
        Log.d(LOG_TAG, String.valueOf(someMultipleImageUrisToString));

        for (int i = 0; i < someMultipleImageUrisToString.size(); i++) {
            someMultipleImageUris.add(Uri.parse(someMultipleImageUrisToString.get(i)));
        }

        Log.d(LOG_TAG, String.valueOf(someMultipleImageUris));

        try {
            ImageUtil imageUtil = ImageUtil.getInstance();
            Bitmap bitmap;
            //ArrayList<Bitmap> bitmaps = new ArrayList<>();
            ArrayList<String> sharedImages = new ArrayList<>();

            for (int i = 0; i < someMultipleImageUris.size(); i++) {
                //bitmaps.add(i, MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), someMultipleImageUris.get(i)));

                //bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), someMultipleImageUris.get(i));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(mActivity.getContentResolver(),someMultipleImageUris.get(i)));

                    Bitmap resizeBitmap = imageUtil.resizeBitmap(bitmap, 400);

                    String sharedImage = imageUtil.bitmapToBase64(resizeBitmap);
                    sharedImages.add(sharedImage);

                    Log.e(LOG_TAG, sharedImages.toString());
                }
            }

            Log.e(LOG_TAG, String.valueOf(sharedImages));

            send("multipleImages", CODE_SUCCES, sharedImages);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
