package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.BalanceBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.MyAcountPresenter;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.RechargeAndConsumeDetailActivity;
import com.huawei.fusionchargeapp.views.interfaces.MyAcountView;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by john on 2018/5/7.
 */

public class MyAcountActivity extends BaseActivity<MyAcountView,MyAcountPresenter> implements MyAcountView {

    @Bind(R.id.nav)
    NavBar nav;
    @Bind(R.id.tv_blance)
    TextView tv_balance;
    private Context context =MyAcountActivity.this;

    public static Intent getLuancher(Context context){
        Intent intent =new Intent(context,MyAcountActivity.class);
        return intent;
    }

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(context));
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
    protected void onResume() {
        super.onResume();
        presenter.getMyBalance();
    }

    @Override
    protected MyAcountPresenter createPresenter() {
        return new MyAcountPresenter();
    }

    @OnClick({R.id.rl_put_foward,R.id.rl_rechage})
    void gePutForward(View view){
        Intent intent = new Intent(this,CashPatternActivity.class);
        intent.putExtra(CashPatternActivity.KEY_CASH_OPERATION_PATTER,view.getId() == R.id.rl_rechage);
        startActivity(intent);
    }

    @OnClick(R.id.rl_detail)
    public void godetail(){
        startActivity(new Intent(this, RechargeAndConsumeDetailActivity.class));
    }

    @OnClick(R.id.rl_invoice)
    public void goInvoice(){
        startActivity(MyInvoiceActivity.getLauncher(context));
    }

    @OnClick(R.id.rl_my_taocan)
    public void goMyTaocan(){
        startActivity(MyTaoCanActivity.getLauncher(context));
    }

    @Override
    public void renderBalance(BalanceBean bean) {
        tv_balance.setText(bean.balance+"");
    }
}
