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

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class ShareSheetObject extends NexacroPlugin implements DefaultLifecycleObserver {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String SVCID = "svcid";
    private static final String REASON = "reason";
    private static final String RETVAL = "returnvalue";

    private static final String CALL_BACK = "_oncallback";
    private static final String METHOD_CALLMETHOD = "callMethod";

    public static final int CODE_SUCCES = 0;
    public static final int CODE_ERROR = -1;
    public String mServiceId = "";

    private Activity mActivity;
    private ShareSheetInterface mShareSheetInterface;
    private DataBidingViewModel mViewModel;
    private ViewModelProvider.AndroidViewModelFactory mViewModelFactory;


    public ShareSheetObject(String objectId) {
        super(objectId);
        mShareSheetInterface = (ShareSheetInterface) NexacroActivity.getInstance();
        mShareSheetInterface.setShareSheetObject(this);
        mActivity = (Activity) NexacroActivity.getInstance();


        Log.e(LOG_TAG,objectId);
    }

    @Override
    public void init(JSONObject jsonObject) {
        if (mViewModelFactory == null) {
            mViewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(mActivity.getApplication());
        }
        mViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity,mViewModelFactory).get(DataBidingViewModel.class);
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

                    // App(Main)에서 저장한 데이터를 NexacroActivityExt에서 꺼내서 모듈로 리턴.
                    send(CODE_SUCCES, mShareSheetInterface.getShareSheetData());

                }
            }catch (Exception e) {
                send(CODE_ERROR,e);
            }
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

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onDestroy(owner);
    }


    //    public void handleSendText(Intent intent) {
//        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//        if (sharedText != null) {
//            send("text/plain", CODE_SUCCES, sharedText);
//        }
//    }
//
//    public void handleSendImage(Intent intent) {
//        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
//        if (imageUri != null) {
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageUri);
//                ImageUtil imageUtil = ImageUtil.getInstance();
//
//                Bitmap resizeBitmap = imageUtil.resizeBitmap(bitmap, 400);
//                String sharedImage = imageUtil.bitmapToBase64(resizeBitmap);
//
//                send("singleImage", CODE_SUCCES, sharedImage);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void handleSendMultipleImages(Intent intent) {
//        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//
//        Log.e(LOG_TAG, String.valueOf(imageUris));
//        if (imageUris != null) {
//            try {
//                ImageUtil imageUtil = ImageUtil.getInstance();
//                ArrayList<Bitmap> bitmaps = new ArrayList<>();
//                ArrayList<String> sharedImages = new ArrayList<>();
//
//                for (int i = 0; i == imageUris.size(); i++) {
//                    bitmaps.add(i, MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageUris.get(i)));
//                    Log.e(LOG_TAG, bitmaps.toString());
//                    sharedImages.add(i, imageUtil.bitmapToBase64(bitmaps.get(i)));
//                }
//
//                Log.e(LOG_TAG, String.valueOf(bitmaps));
//
//                //send("multipleImages",CODE_SUCCES,sharedImages);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
