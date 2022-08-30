package com.example.androidruntime;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.nexacro.NexacroActivity;
import com.tobesoft.plugin.sharesheet.PreferenceManager;
import com.tobesoft.plugin.sharesheet.ShareSheetObject;
import com.tobesoft.plugin.sharesheet.plugininterface.ShareSheetInterface;
import com.tobesoft.plugin.sharesheet.viewmodel.SharesDataViewModel;
import com.tobesoft.plugin.sharesheet.viewmodel.SharesDataViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;


public class NexacroActivityExt extends NexacroActivity implements ShareSheetInterface {

    String LOG_TAG = this.getClass().getSimpleName();
    private SharesDataViewModel mSharesDataViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSharesDataViewModel = new ViewModelProvider((ViewModelStoreOwner) getApplicationContext(), new SharesDataViewModelFactory())
                .get(SharesDataViewModel.class);

//        PreferenceManager.setSharesDataViewModel(mSharesDataViewModel);

        Observer<String> SharesDataObserver = newData -> mShareSheetObject.execute();
        mSharesDataViewModel.getSharesData().observe(this, SharesDataObserver);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mShareSheetObject != null) {
//            mShareSheetObject.execute();
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
    }


    /** Sharesheet 연동 코드 ****************************************************************************/


}

