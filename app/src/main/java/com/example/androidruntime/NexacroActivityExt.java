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
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            String someText = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.e(LOG_TAG, someText);
            String sendText = PreferenceManager2.getString(getApplicationContext(),"testKey");
            Log.e(LOG_TAG, "::::::::::::::::::::::::::"+sendText);
        }

        // 정리를 해보자
        // NexacroActivity로 바로 Action 인텐트를 호출받는 하는 경우는 앱이 죽게 된다.
        // 구조적으로 UpdaterActivity로 인텐트를 받아야 한다.
        // UpdaterActivity로 데이터를 받을시 엔진이 종료 된 후 다시 실행되게 된다.

        // 앱이 실행이 안되어 있을때는 Main -> NexacroActivity -> 모듈 -> 화면 순으로 데이터 플로우를 가지게 된다.
        // 앱이 실행 중일때는 NexacroActivity에서 -> 모듈 -> 화면순으로 데이터를 받게 처리하면 된다.

        // 이때 필요한게 LifeCycle Observer 이다.
        // LifeCycle Observer를 통해서 Main이 죽어있는 상태라는 걸 알 수있다.
        // ex ) -> NexacroAcitvity가 onPause 상태일때 옵저버에 알린다. ( 왜냐하면 초기 실행시 Main의 onDestory를 거치며 NexacroAcitvity가 onResume 상태로 들어오기 때문 )
        // 이제 여기서 문제가 생긴다. Intent를 action 필터를 통해 받게 되는데에.........
        // 얘는 Main이나 Nexacro의 Activity 상태가 어떤지 1도 관심이 없고 필터링 할 방법이 없다.
        //

        Log.e(LOG_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager2.clear(getApplicationContext());
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
        String sendText = PreferenceManager.getString(getApplicationContext(),"testKey");
        String action = mIntent.getAction();
        String type = mIntent.getType();
        String someText = "";

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            someText = mIntent.getStringExtra(Intent.EXTRA_TEXT);
            Log.e(LOG_TAG, someText);
            Log.e(LOG_TAG,sendText);
        }

        return someText;

        /** Sharesheet 연동 코드 ****************************************************************************/
    }

}

