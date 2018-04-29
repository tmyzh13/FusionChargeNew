package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.corelibs.base.BaseActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.presenter.UserInfoPresenter;
import com.huawei.fusionchargeapp.views.interfaces.UserInfoView;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;


public class UserInfoActivity extends BaseActivity<UserInfoView, UserInfoPresenter> implements UserInfoView, View.OnClickListener {
    private static final String TAG = UserInfoActivity.class.getSimpleName();
    @Bind(R.id.nav)
    NavBar navBar;
    public static Intent startActivity(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        return intent;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getResources().getString(R.string.user_info_title));
        navBar.setColor(getResources().getColor(R.color.app_blue));
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }

    @Override
    public void goLogin() {

    }
}
