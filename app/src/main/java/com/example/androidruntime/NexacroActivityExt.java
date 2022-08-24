package com.example.androidruntime;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.nexacro.NexacroActivity;
import com.tobesoft.plugin.sharesheet.PreferenceManager;
import com.tobesoft.plugin.sharesheet.ShareSheetObject;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;


public class NexacroActivityExt extends NexacroActivity implements ShareSheetInterface {

    String LOG_TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String sendText = PreferenceManager.getString(getApplicationContext(), "testKey");
//        if (!sendText.equals("")) {
//            try {
//                JSONObject jsonObject = new JSONObject(sendText);
//
//                if( mShareSheetObject != null ) {
//                    mShareSheetObject.execute(jsonObject);
//                    Log.e(LOG_TAG, "::::::::::::::::::::::::::" + sendText);
//                    Log.e(LOG_TAG, "onResume");
//                } else {
//                    Handler handler = new Handler(Looper.getMainLooper());
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mShareSheetObject.execute(jsonObject);
//                        }
//                    },3000);
//                    Log.e(LOG_TAG,"mShareSheetObject is null");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    protected void onPause() {
        PreferenceManager.clear(getApplicationContext());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.clear(getApplicationContext());
        super.onDestroy();
    }


    /**
     * Sharesheet 연동 코드
     ****************************************************************************/

    private ShareSheetObject mShareSheetObject;

    @Override
    public void setShareSheetObject(ShareSheetObject obj) {
        this.mShareSheetObject = obj;
        Observable<ShareSheetObject> source = Observable.create(emitter -> {
            emitter.onNext(mShareSheetObject);
            emitter.onComplete();
        });

        source.subscribe(executeShareSheetObject(mShareSheetObject));
    }

    public @NonNull Observer<? super ShareSheetObject> executeShareSheetObject(ShareSheetObject shareSheetObject) {
        String sendText = PreferenceManager.getString(getApplicationContext(), "testKey");
        if (!sendText.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(sendText);

                if( shareSheetObject != null ) {
                    shareSheetObject.execute(jsonObject);
                    Log.e(LOG_TAG, "::::::::::::::::::::::::::" + sendText);
                    Log.e(LOG_TAG, "onResume");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /** Sharesheet 연동 코드 ****************************************************************************/


}

