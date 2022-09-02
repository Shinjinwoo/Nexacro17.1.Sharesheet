package com.example.androidruntime;

import android.os.Bundle;
import android.view.View;


import androidx.annotation.NonNull;

import com.nexacro.NexacroActivity;
import com.tobesoft.plugin.sharesheet.PreferenceManager;
import com.tobesoft.plugin.sharesheet.ShareSheetObject;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;



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
        if (mShareSheetObject != null) {
            mShareSheetObject.execute();
        }
    }


    @Override
    protected void onPause() {
        PreferenceManager.removeKey(getApplicationContext(),"SharesObjectKey");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.removeKey(getApplicationContext(),"SharesObjectKey");
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        mShareSheetObject.onRequestPermissionsResult(requestCode,permission,grantResults);
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
    }

    /** Sharesheet 연동 코드 ****************************************************************************/


}

