package com.huawei.fusionchargeapp.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.presenter.SettingPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.SettingView;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.OnClick;


public class SettingActivity extends BaseActivity<SettingView, SettingPresenter> implements SettingView {
    private static final String TAG = SettingActivity.class.getSimpleName();
    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.tv_login_out)
    TextView tv_login_out;
    @Bind(R.id.tv_exit)
    TextView tv_exit;
    @Bind(R.id.tv_version)
    TextView new_version;
    @Bind(R.id.cur_version)
    TextView cur_version;

    private boolean hasNewVersion = false;

    public static Intent getLauncher(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getResources().getString(R.string.setting_title));
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            navBar.setBackground(getResources().getDrawable(R.drawable.nan_bg));
        } else {
            navBar.setColor(getResources().getColor(R.color.app_blue));
        }
        resetVersionShow();
    }

    private void resetVersionShow(){
        if (hasNewVersion) {
            new_version.setVisibility(View.VISIBLE);
            cur_version.setVisibility(View.GONE);
        } else {
            cur_version.setText(Tools.getVersionName(this));
            cur_version.setVisibility(View.VISIBLE);
            new_version.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_login_out)
    public void gotoLoginout() {
        String message = String.format(getString(R.string.log_out_message), UserHelper.getSavedUser().phone);
        final CommonDialog dialog = new CommonDialog(this,getString(R.string.are_you_sure_to_logout),message,2);
        dialog.show();
        dialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showLoading();
                presenter.logout(UserHelper.getSavedUser().phone);
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
        final CommonDialog dialog = new CommonDialog(this,"",getString(R.string.confirm_clear_user),2);
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
    public void goAboutUs(){
        startActivity(new Intent(SettingActivity.this,AboutUsActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @Override
    public void goLogin() {

    }

    @Override
    public void onLogoutSuccess() {
        hideLoading();
        RxBus.getDefault().send(new Object(), Constant.LOGIN_OUT_SET_APPOINT_VIEW_GONE);
        PreferencesHelper.clearData();
        Intent intent=MainActivity.getLauncher(SettingActivity.this);
        intent.putExtra(MainActivity.ACTION,MainActivity.LOGINT_OUT);
        startActivity(intent);

        SettingActivity.this.finish();
    }

    @Override
    public void onLogoutFail(String message) {
        hideLoading();
        showToast(message);
    }

//    @OnClick(R.id.tv_clause)
//    public void goResgitment(){
//        startActivity(WebActivity.getLauncher(SettingActivity.this,0));
//    }
}
