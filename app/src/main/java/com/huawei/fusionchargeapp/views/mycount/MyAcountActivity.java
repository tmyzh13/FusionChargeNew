package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by john on 2018/5/7.
 */

public class MyAcountActivity extends BaseActivity {

    @Bind(R.id.nav)
    NavBar nav;

    private Context context =MyAcountActivity.this;

    public static Intent getLuancher(Context context){
        Intent intent =new Intent(context,MyAcountActivity.class);
        return intent;
    }

    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_acount;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setNavTitle(getString(R.string.my_acount));
        nav.setImageBackground(R.drawable.nan_bg);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.rl_rechage)
    public void goRechage(){

    }

    @OnClick(R.id.rl_detail)
    public void godetail(){

    }

    @OnClick(R.id.rl_put_foward)
    public void goPutFoward(){

    }

    @OnClick(R.id.rl_invoice)
    public void goInvoice(){
        startActivity(MyInvoiceActivity.getLauncher(context));
    }
}
