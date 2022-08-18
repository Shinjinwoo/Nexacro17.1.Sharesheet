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

            PreferenceManager.setIntentToJson(getApplicationContext(),"testKey",intent);

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

