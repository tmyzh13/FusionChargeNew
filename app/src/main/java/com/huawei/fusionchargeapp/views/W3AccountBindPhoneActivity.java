package com.huawei.fusionchargeapp.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.W3AccountBindPhonePresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.W3AccountBindPhoneView;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * create by zhangwei
 */

public class W3AccountBindPhoneActivity extends BaseActivity<W3AccountBindPhoneView,W3AccountBindPhonePresenter> implements W3AccountBindPhoneView {

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

    private String phone;
    private String w3Account;

    private String verCodePhone;
    private String verCode;
    private LoginActivity.MyCountDownTimer timer;
    private CommonDialog mDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_w3_account_bind_phone;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            navBar.setBackground(getResources().getDrawable(R.drawable.nan_bg));
        } else {
            navBar.setColor(getResources().getColor(R.color.app_blue));
        }
        navBar.setNavTitle(this.getString(R.string.bind_phone_number));
        navBar.findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        if(intent != null) {
            w3Account = getIntent().getStringExtra(MainActivity.W3_ACCOUNT);
            phone = getIntent().getStringExtra(MainActivity.W3_PHONE);
            etBindInputPhone.requestFocus();
            etBindInputPhone.setText(phone);
            etBindInputPhone.setSelection(TextUtils.isEmpty(phone) ?  0 : phone.length());
            if(TextUtils.isEmpty(w3Account)) {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    protected W3AccountBindPhonePresenter createPresenter() {
        return new W3AccountBindPhonePresenter();
    }

    @Override
    public void goLogin() {

    }

    @Override
    public void onBackPressed() {
        showToast(getString(R.string.you_must_bind_phone_number));
        super.onBackPressed();
    }

    @OnClick({R.id.tv_getVerCode, R.id.done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getVerCode:
                phone = etBindInputPhone.getText().toString();
                if(!Tools.isNull(phone)) {
                    if(Tools.isChinaPhoneLegal(phone)) {
                        if(timer == null) {
                            timer = new LoginActivity.MyCountDownTimer(tvGetVerCode,60000,1000);
                        }
                        timer.start();
                        presenter.getVerCode(phone);
                    } else {
                        showToast(getString(R.string.input_correct_phone));
                    }

                } else {
                    showToast(getString(R.string.input_can_not_be_null));
                }
                break;
            case R.id.done:
                verCode = etBindInputVercode.getText().toString();
                phone = etBindInputPhone.getText().toString();
                if(TextUtils.isEmpty(verCode)) {
                    showToast(getString(R.string.code_incorrect));
                } else if(TextUtils.isEmpty(verCodePhone)) {
                    showToast(getString(R.string.please_get_vercode_first));
                } else if(!verCodePhone.equals(phone)) {
                    showToast(getString(R.string.vercode_phone_differ_phone));
                }else {
                    presenter.iAdminRegister(verCode,verCodePhone,w3Account);
                }
                break;
        }
    }

    @Override
    public void getVerCodeSuccess(String verCodePhone) {
        this.verCodePhone = verCodePhone;
        showToast(getString(R.string.vercode_has_send));

    }

    @Override
    public void getVerCodeFailed(String verCodePhone,int status) {
        this.verCodePhone = verCodePhone;
        if(status == 202) {
            showToast(getString(R.string.vercode_incorrent));
        } else {
            showToast(getString(R.string.vercode_send_fail));
        }
        timer.cancel();
        timer = null;
        tvGetVerCode.setClickable(true);
        tvGetVerCode.setText(R.string.action_get_code_again);
    }

    @Override
    public void registerSuccess() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.IS_REGISTER_SUCCESS,true);
        setResult(MainActivity.W3_REGISTER_REQUEST_CODE,intent);
        finish();
    }

    @Override
    public void registerFail() {
        showToast(getString(R.string.register_fail));
        timer.cancel();
        timer = null;
        tvGetVerCode.setClickable(true);
        tvGetVerCode.setText(R.string.action_get_code_again);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
