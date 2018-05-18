package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.presenter.MyTaocanPresenter;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.interfaces.MyTaocanView;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;

/**
 * Created by issuser on 2018/5/17.
 */

public class MyTaoCanActivity extends BaseActivity<MyTaocanView,MyTaocanPresenter> implements MyTaocanView {

    @Bind(R.id.nav)
    NavBar nav;

    private Context context=MyTaoCanActivity.this;

    public static Intent getLauncher(Context context){
        Intent intent =new Intent(context,MyTaoCanActivity.class);
        return intent;
    }

    @Override
    public void goLogin() {
        startActivity(LoginActivity.getLauncher(context));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_tao_can;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setNavTitle(getString(R.string.my_tao_can));
        nav.setImageBackground(R.drawable.nan_bg);
        presenter.getMyTaoCan();
    }

    @Override
    protected MyTaocanPresenter createPresenter() {
        return new MyTaocanPresenter();
    }
}
