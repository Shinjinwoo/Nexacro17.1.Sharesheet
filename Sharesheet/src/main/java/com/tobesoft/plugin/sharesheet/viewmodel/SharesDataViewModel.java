package com.tobesoft.plugin.sharesheet.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharesDataViewModel extends ViewModel {

    private MutableLiveData<String> mSharesData;

    public MutableLiveData<String> getSharesData() {
        if (mSharesData == null) {
            mSharesData = new MutableLiveData<>();
        }

        return mSharesData;
    }


}
