package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.os.Bundle;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.views.LoginActivity;

/**
 * Created by john on 2018/5/9.
 */

public class ApplyOrderListActivity extends BaseActivity {

    private Context context=ApplyOrderListActivity.this;

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(context));
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
