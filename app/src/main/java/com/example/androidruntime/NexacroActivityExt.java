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
        if ( mShareSheetObject != null ) {
            mShareSheetObject.execute();
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


    public void execute() {
        String sendText = PreferenceManager.getString(getApplicationContext(), "testKey");
        try {
            JSONObject jsonObject = new JSONObject(sendText);
            String getAction = jsonObject.getString("action");
            if (getAction.equals("android.intent.action.SEND") || getAction.equals("android.intent.action.SEND_MULTIPLE")) {
                if (mShareSheetObject != null) {
                    mShareSheetObject.execute(jsonObject);
                    Log.e(LOG_TAG, "::::::::::::::::::::::::::" + sendText);
                    Log.e(LOG_TAG, "onResume");
                } else {
                    //TODO 아 비동기로 처리도 왜 안되냐...

                    Handler handler = new Handler(getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            execute();
                        }
                    },1000);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** Sharesheet 연동 코드 ****************************************************************************/


}

