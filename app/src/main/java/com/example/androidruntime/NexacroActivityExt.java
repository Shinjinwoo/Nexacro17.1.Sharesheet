package com.example.androidruntime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.nexacro.NexacroActivity;
import com.tobesoft.plugin.sharesheet.DataBidingViewModel;
import com.tobesoft.plugin.sharesheet.PreferenceManager;
import com.tobesoft.plugin.sharesheet.ShareSheetObject;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;


public class NexacroActivityExt extends NexacroActivity implements ShareSheetInterface {

    Intent mIntent = null;
    String LOG_TAG = this.getClass().getSimpleName();
    private DataBidingViewModel mViewModel;
    private ViewModelProvider.AndroidViewModelFactory mViewModelFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String action = intent.getAction();

        Log.e(LOG_TAG, "onCreate");
        Log.e(LOG_TAG, "::::::::::::::::::::::::::"+action);


        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();

        Log.e(LOG_TAG, "onResume");
        Log.e(LOG_TAG, "::::::::::::::::::::::::::"+action);

        String sendText = PreferenceManager.getString(getApplicationContext(),"testKey");
//        if ( sendText != null && (!sendText.equals("")) ) {
//
//
//
//            mViewModel.addMutableData(sendText);
//        }
    }

    @Override
    protected void onPause() {
        PreferenceManager.clear(getApplicationContext());
        //mViewModel.delete();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.clear(getApplicationContext());
        //mViewModel.delete();
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

    @Override
    public String getShareSheetData() {
        //String sendText = PreferenceManager.getString(getApplicationContext(),"testKey");
        String action = mIntent.getAction();
        String type = mIntent.getType();
        String someText = "";

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            someText = mIntent.getStringExtra(Intent.EXTRA_TEXT);
            Log.e(LOG_TAG, someText);
        }

        return someText;

        /** Sharesheet 연동 코드 ****************************************************************************/
    }

}

