package com.huawei.fusionchargeapp.views;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;

/**
 * Created by admin on 2018/5/8.
 */

public class AboutUsActivity extends BaseActivity{
    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.version_name)
    TextView version_name;

    private static final String VERSION_PREFIX = "v";

    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setColorRes(R.color.app_blue);
        bar.setNavTitle("关于我们");
        setVersionName();
    }

    //设置版本名
    public void setVersionName() {
        PackageInfo pi = null;
        try {
            PackageManager pm = getPackageManager();
            pi = pm.getPackageInfo(getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            version_name.setText(VERSION_PREFIX+pi.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
