package com.example.androidruntime;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.nexacro.NexacroActivity;
import com.tobesoft.plugin.sharesheet.PreferenceManager;
import com.tobesoft.plugin.sharesheet.ShareSheetObject;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;

import org.json.JSONException;
import org.json.JSONObject;


public class NexacroActivityExt extends NexacroActivity implements ShareSheetInterface {

    String LOG_TAG = this.getClass().getSimpleName();
    private Boolean mIsSharesSheetObjectNotNull = false;

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

        boolean handler = new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },1000);

        try {
            JSONObject jsonObject = new JSONObject(sendText);
            String getAction = jsonObject.getString("action");
            if (getAction.equals("android.intent.action.SEND") || getAction.equals("android.intent.action.SEND_MULTIPLE")) {
                if (mShareSheetObject != null) {
                    mShareSheetObject.execute(jsonObject);
                    Log.e(LOG_TAG, "::::::::::::::::::::::::::" + sendText);
                    Log.e(LOG_TAG, "onResume");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

