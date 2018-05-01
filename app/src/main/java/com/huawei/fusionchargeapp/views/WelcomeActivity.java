package com.huawei.fusionchargeapp.views;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.common.AppManager;
import com.huawei.fusionchargeapp.App;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;

/**
 * Created by issuser on 2018/4/27.
 */

public class WelcomeActivity extends BaseActivity {
    @Override
    public void goLogin() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("zw","getApplicationContext : " + (getApplicationContext() == null));
        AppManager.getAppManager().setContext(getApplicationContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                startActivity(MainActivity.getLauncher(WelcomeActivity.this));
                finish();
            }
        }, 2000);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
