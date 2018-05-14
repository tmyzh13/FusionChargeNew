package com.huawei.fusionchargeapp.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * create by zhangwei
 */

public class W3AccountBindPhoneActivity extends BaseActivity {

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.et_bind_input_phone)
    EditText etBindInputPhone;
    @Bind(R.id.et_bind_input_vercode)
    EditText etBindInputVercode;
    @Bind(R.id.tv_getVerCode)
    TextView tvGetVerCode;
    @Bind(R.id.done)
    Button done;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_w3_account_bind_phone;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void goLogin() {

    }

    @OnClick({R.id.tv_getVerCode, R.id.done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getVerCode:
                break;
            case R.id.done:
                break;
        }
    }
}
