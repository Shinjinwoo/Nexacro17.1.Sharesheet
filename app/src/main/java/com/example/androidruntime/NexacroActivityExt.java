package com.example.androidruntime;

import android.os.Bundle;
import android.view.View;


import androidx.annotation.NonNull;

import com.nexacro.NexacroActivity;
import com.tobesoft.plugin.sharesheetobject.ShareSheetObject;
import com.tobesoft.plugin.sharesheetobject.plugininterface.ShareSheetInterface;



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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * SharesheetPlugin 연동 코드
     *************************************************************************************************/

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

    @Override
    protected void onResume() {
        super.onResume();
        if (mShareSheetObject != null) {
            mShareSheetObject.callSharedData();
        }
    }

    /** SharesheetPlugin 연동 코드 ****************************************************************************/


}

