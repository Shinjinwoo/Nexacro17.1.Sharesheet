package com.tobesoft.plugin.sharesheet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.nexacro.Nexacro;
import com.nexacro.NexacroActivity;
import com.nexacro.plugin.NexacroPlugin;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;
import com.tobesoft.plugin.plugincommonlib.util.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


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

                Log.d(LOG_TAG,sharedImage);

                send("singleImage", CODE_SUCCES, sharedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);

        Log.e(LOG_TAG, String.valueOf(imageUris));
        if (imageUris != null) {
            try {
                ImageUtil imageUtil = ImageUtil.getInstance();
                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                ArrayList<String> sharedImages = new ArrayList<>();

                for (int i = 0; i == imageUris.size(); i++) {
                    bitmaps.add(i, MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageUris.get(i)));
                    Log.e(LOG_TAG, bitmaps.toString());
                    sharedImages.add(i, imageUtil.bitmapToBase64(bitmaps.get(i)));
                }

                Log.e(LOG_TAG, String.valueOf(bitmaps));

                send("multipleImages", CODE_SUCCES, sharedImages);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
