package com.tobesoft.plugin.sharesheet.plugininterface;

import com.tobesoft.plugin.sharesheet.ShareSheetObject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.DisposableObserver;

public interface ShareSheetInterface {

    public void setShareSheetObject(ShareSheetObject obj);
    public String getShareSheetData();
    public ObservableOnSubscribe<String> getObserver(ObservableOnSubscribe<String> observable);
    public DisposableObserver<String> setObserver (DisposableObserver<String> observer);

}
