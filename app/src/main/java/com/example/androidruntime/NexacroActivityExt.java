package com.example.androidruntime;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.nexacro.NexacroActivity;
import com.tobesoft.plugin.sharesheet.DataBidingViewModel;
import com.tobesoft.plugin.sharesheet.PreferenceManager;
import com.tobesoft.plugin.sharesheet.ShareSheetObject;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.DisposableObserver;


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
        String sendText = PreferenceManager.getString(getApplicationContext(), "testKey");
        if (!sendText.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(sendText);

                if(mShareSheetObject != null ) {
                    mShareSheetObject.execute(jsonObject);
                    Log.e(LOG_TAG, "::::::::::::::::::::::::::" + sendText);
                    Log.e(LOG_TAG, "onResume");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
    }

    /** Sharesheet 연동 코드 ****************************************************************************/


}

