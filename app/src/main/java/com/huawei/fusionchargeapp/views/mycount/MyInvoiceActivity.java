package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;

/**
 * Created by john on 2018/5/7.
 */

public class MyInvoiceActivity extends BaseActivity {

    @Bind(R.id.nav)
    NavBar nav;

    public static Intent getLauncher(Context context){
        Intent intent=new Intent(context,MyInvoiceActivity.class);
        return intent;
    }

    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myinvoice;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setNavTitle(getString(R.string.my_acount_invoice));
        nav.setImageBackground(R.drawable.nan_bg);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
