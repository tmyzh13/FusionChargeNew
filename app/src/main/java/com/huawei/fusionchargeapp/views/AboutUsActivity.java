package com.huawei.fusionchargeapp.views;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.utils.Tools;
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
        bar.setNavTitle(getString(R.string.activity_about_us));
        version_name.setText(Tools.getVersionName(this));
    }



    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
