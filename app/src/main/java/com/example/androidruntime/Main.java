package com.example.androidruntime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.nexacro.NexacroResourceManager;
import com.nexacro.NexacroUpdatorActivity;
import com.tobesoft.plugin.sharesheet.PreferenceManager;

public class Main extends NexacroUpdatorActivity {

    private final String LOG_TAG = this.getClass().getSimpleName();


    public Main() {
        super();

        setBootstrapURL("http://smart.tobesoft.co.kr/NexacroN/Sharesheet/start_android.json");
        setProjectURL("http://smart.tobesoft.co.kr/NexacroN/Sharesheet/");

        this.setStartupClass(NexacroActivityExt.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NexacroResourceManager.createInstance(this);
        NexacroResourceManager.getInstance().setDirect(true);

        Intent intent = getIntent();

        if(intent != null) {

            /**
             * PreferenceManager.setIntentToJson 함수설명
             * @param Context : 앱의 컨텍스트 정보를 삽입합니다.
             * @param Key : PreferenceManager에 데이터를 저장할때 필요한 키 입니다.
             * @param intent : 앱간 Intent Filter 로 공유 받은 데이터가 담긴 intent를 전달 합니다.
             * @param resizeScale : 넥사크로로 리턴받을 이미지의 리사이즈 스케일 값을 적습니다.
             */
            PreferenceManager.setIntentToJson(getApplicationContext(),"SharesObjectKey",intent,300);

            String bootstrapURL = intent.getStringExtra("bootstrapURL");
            String projectUrl = intent.getStringExtra("projectUrl");

            if (bootstrapURL != null) {
                setBootstrapURL(bootstrapURL);
                setProjectURL(projectUrl);
            }

        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

