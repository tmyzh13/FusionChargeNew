package com.huawei.fusionchargeapp.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.common.AppManager;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.rxbus.RxBus;
import com.huawei.fusionchargeapp.App;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.presenter.SettingPresenter;
import com.huawei.fusionchargeapp.views.interfaces.SettingView;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.OnClick;


public class SettingActivity extends BaseActivity<SettingView, SettingPresenter> implements SettingView, View.OnClickListener {
    private static final String TAG = SettingActivity.class.getSimpleName();
    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.tv_login_out)
    TextView tv_login_out;
    @Bind(R.id.tv_exit)
    TextView tv_exit;

    public static Intent startActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getResources().getString(R.string.setting_title));
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
            navBar.setBackground(getResources().getDrawable(R.drawable.nan_bg));
        else
            navBar.setColor(getResources().getColor(R.color.app_blue));
    }

    @OnClick(R.id.tv_login_out)
    public void gotoLoginout() {
        final CommonDialog dialog = new CommonDialog(this,"","您确定要注销账户？",2);
        dialog.show();
        dialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                RxBus.getDefault().send(new Object(), Constant.LOGIN_OUT_SET_APPOINT_VIEW_GONE);
                PreferencesHelper.clearData();
                Intent intent=MainActivity.getLauncher(SettingActivity.this);
                intent.putExtra(MainActivity.ACTION,MainActivity.LOGINT_OUT);
                startActivity(intent);

                SettingActivity.this.finish();
            }
        });
        dialog.setNagitiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.tv_exit)
    public void gotoExit() {
        final CommonDialog dialog = new CommonDialog(this,"","您确定要清除账户信息且退出应用？",2);
        dialog.show();
        dialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                RxBus.getDefault().send(new Object(), Constant.LOGIN_OUT_SET_APPOINT_VIEW_GONE);
                PreferencesHelper.clearData();
                finish();
                AppManager.getAppManager().appExit();
            }
        });
        dialog.setNagitiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.tv_advice)
    void goSuggestion(){
        startActivity(new Intent(SettingActivity.this,SuggestionActivity.class));
    }

    @OnClick(R.id.tv_about_us)
    void goAboutUs(){
        startActivity(new Intent(SettingActivity.this,AboutUsActivity.class));
    }


    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @Override
    public void goLogin() {

    }
}
