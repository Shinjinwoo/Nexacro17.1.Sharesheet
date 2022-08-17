package com.tobesoft.plugin.sharesheet;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataBidingViewModel extends ViewModel {

    public String LOG_TAG = this.getClass().getSimpleName();
    private MutableLiveData<String> data;

    public LiveData<String> getMutableData() {
        if (data == null) {
            data = new MutableLiveData<>("Empty Text");
        }
        return data;
    }

    public void addMutableData(String argument){
        data.setValue(getMutableData()+argument);
        Log.d(LOG_TAG,data.toString());
    }

    public void delete() {
        data.setValue("");
        Log.d(LOG_TAG, String.valueOf(data));
    }


}
