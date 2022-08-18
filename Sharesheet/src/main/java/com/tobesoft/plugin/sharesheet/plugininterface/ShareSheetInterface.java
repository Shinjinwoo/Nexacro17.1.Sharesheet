package com.tobesoft.plugin.sharesheet.plugininterface;

import com.tobesoft.plugin.sharesheet.ShareSheetObject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.DisposableObserver;

public interface ShareSheetInterface {

    public void setShareSheetObject(ShareSheetObject obj);

}
