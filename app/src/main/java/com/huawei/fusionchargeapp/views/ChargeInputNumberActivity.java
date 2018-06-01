package com.huawei.fusionchargeapp.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.corelibs.utils.ToastMgr;
import com.google.gson.Gson;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.MyOrderData;
import com.huawei.fusionchargeapp.model.beans.RequestMyOrderBean;
import com.huawei.fusionchargeapp.model.beans.RequestMyOrderChildBean;
import com.huawei.fusionchargeapp.model.beans.RequestScanBean;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.ChargeInputNumberPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.ChargeInputNumberView;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.trello.rxlifecycle.ActivityEvent;

import butterknife.Bind;
import butterknife.OnClick;

public class ChargeInputNumberActivity extends BaseActivity<ChargeInputNumberView,ChargeInputNumberPresenter> implements ChargeInputNumberView {



    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.et_input_number)
    EditText etInputNumber;
    @Bind(R.id.sure)
    TextView sure;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_input_number;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            navBar.setBackground(getResources().getDrawable(R.drawable.nan_bg));
        } else {
            navBar.setColor(getResources().getColor(R.color.app_blue));
        }
        navBar.setNavTitle(this.getString(R.string.charge));

    }

    @Override
    protected ChargeInputNumberPresenter createPresenter() {
        return new ChargeInputNumberPresenter();
    }

    @OnClick(R.id.sure)
    public void onViewClicked() {
        String str = etInputNumber.getText().toString();

        showLoading();
        presenter.getData(str);

//        testt();
    }


    @Override
    public void goLogin() {
        ToastMgr.show(getString(R.string.login_fail));
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(ChargeInputNumberActivity.this));
    }

    @Override
    public void onGetDataSuccess(ScanChargeInfo info) {
        hideLoading();

        if(info!= null) {
            Gson gson = new Gson();
            String data = gson.toJson(info);
            Intent intent = new Intent(ChargeInputNumberActivity.this,ChargeOrderDetailsActivity.class);
            intent.putExtra("data",data);
            startActivity(intent);
        } else {
            showToast(getString(R.string.no_user_info));
        }

        finish();
    }

    @Override
    public void onGetDataFail(String msg) {
        hideLoading();

    }
}
