package com.huawei.fusionchargeapp.views.interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.CheckCodeBean;
import com.huawei.fusionchargeapp.presenter.LoginPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.InputNewPwdActivity;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;

public class ForgetPwdActivity extends BaseActivity<LoginView,LoginPresenter> implements View.OnClickListener,LoginView {

    private Context context = ForgetPwdActivity.this;
    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.input_your_phone_et)
    EditText phoneNumEt;
    @Bind(R.id.input_message_code_et)
    EditText inputMessageCodeEt;
    @Bind(R.id.get_message_code_tv)
    TextView getMessageCodeTv;
    @Bind(R.id.next_tv)
    TextView nextTv;

    private String phoneNum;
    private String code;

    public static Intent getLaunch(Context context) {
        Intent intent = new Intent(context, ForgetPwdActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getResources().getString(R.string.reset_password));
        navBar.setColor(getResources().getColor(R.color.app_blue));
        clicks();
    }

    private void clicks() {
        getMessageCodeTv.setOnClickListener(this);
        nextTv.setOnClickListener(this);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.not_received_code_tv:
                break;
            case R.id.get_message_code_tv:
                phoneNum = phoneNumEt.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNum) && Tools.isChinaPhoneLegal(phoneNum)) {
                    LoginActivity.MyCountDownTimer timer = new LoginActivity.MyCountDownTimer(getMessageCodeTv, 60000, 1000);
                    timer.start();
                    presenter.getCodeAction(2,phoneNum);
                } else {
                    showHintDialog(getString(R.string.hint),getString(R.string.input_correct_phone));
                    return;
                }
                break;
            case R.id.next_tv:
                phoneNum = phoneNumEt.getText().toString().trim();
                code = inputMessageCodeEt.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum) || !Tools.isChinaPhoneLegal(phoneNum)) {
                    showHintDialog(getString(R.string.hint),getString(R.string.input_correct_phone));
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showHintDialog(getString(R.string.hint),getString(R.string.hint_input_code));
                    return;
                }
                if(code.length()<6){
                    showHintDialog(getString(R.string.hint),getString(R.string.hint_input_code_6));
                    return;
                }
                CheckCodeBean bean=new CheckCodeBean();
                bean.type="2";
                bean.phone=phoneNum;
                bean.captcha=code;
                presenter.checkCode(bean);


                break;
            default:
                break;
        }
    }

    private void showHintDialog(String title,String meg) {
        CommonDialog dialog = new CommonDialog(context,title,meg,1);
        dialog.show();
    }
    @Override
    public void goLogin() {

    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFailure() {

    }

    @Override
    public void checkCodeSuccess() {
        startActivity(InputNewPwdActivity.getLaunch(context,phoneNum, code));
    }

    @Override
    public void registerSuccess() {

    }

    @Override
    public void registerFailure() {

    }

    @Override
    public void getCodeSuccess() {

    }
}
