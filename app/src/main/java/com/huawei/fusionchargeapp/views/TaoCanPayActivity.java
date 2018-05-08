package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.UserBean;

/**
 * Created by john on 2018/5/8.
 */

public class TaoCanPayActivity extends BaseActivity {

    private Context context=TaoCanPayActivity.this;
    public static Intent getLauncher(Context context){
        Intent intent=new Intent(context,TaoCanPayActivity.class);
        return intent;
    }

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(context));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tao_can_pay;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
