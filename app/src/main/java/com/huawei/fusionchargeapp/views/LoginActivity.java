package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.ToastMgr;
import com.corelibs.utils.rxbus.RxBus;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.presenter.LoginPresenter;
import com.huawei.fusionchargeapp.utils.AndroidBug5497Workaround;
import com.huawei.fusionchargeapp.utils.ChoiceManager;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.ForgetPwdActivity;
import com.huawei.fusionchargeapp.views.interfaces.LoginView;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by issuser on 2018/4/18.
 */

public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView, View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Bind(R.id.phone_iv)
    ImageView phoneIv;
    @Bind(R.id.phone_dropdown_iv)
    ImageView phoneDropDownIv;
    @Bind(R.id.phone_cut_line)
    View phoneCutLine;
    @Bind(R.id.phone_et)
    EditText phoneNumberEt;
    @Bind(R.id.input_pwd_ll)
    LinearLayout inputPwdLl;//密码登录ui
    @Bind(R.id.lock_cut_line)
    View lockCutLine;
    @Bind(R.id.pwd_et)
    EditText pwdEt;
    @Bind(R.id.type_code_ll)
    LinearLayout typeCodeLl;//验证码登录ui
    @Bind(R.id.code_et)
    EditText codeEt;
    @Bind(R.id.get_code_tv)
    TextView getCodeTv;
    @Bind(R.id.login_tv)
    TextView loginTv;
    @Bind(R.id.forget_pwd_register_rl)
    RelativeLayout forgetPwdandRegisterRl;
    @Bind(R.id.forget_pwd_tv)
    TextView forgetPwdTv;
    @Bind(R.id.register_tv)
    TextView registerTv;
    @Bind(R.id.cb_appointment)
    CheckBox cb_appointment;
    @Bind(R.id.rl_login)
    RelativeLayout rl_login;
    @Bind(R.id.pwd_ic_del)
    ImageView pwd_ic_del;
    @Bind(R.id.pwd_ic_see)
    ImageView pwd_ic_see;
    @Bind(R.id.tv_appointment_)
    TextView tv_appointment;
    @Bind(R.id.nav)
    NavBar navBar;

    private Context context = LoginActivity.this;
    private String phoneNumber;
    private String pwd;
    private String code;
    private PopupWindow popupWindow;
    private TextView phoneTypeTv;
    private TextView workerTypeTv;
    private TextView codeTypeTv;
    private int type = 1;//登录方式

    private boolean isRegister = false;
    private MyCountDownTimer timer;
 
    
    public static Intent getLauncher(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://手机号登录
                    phoneDropDownIv.setVisibility(View.GONE);
                    phoneCutLine.setVisibility(View.INVISIBLE);
                    inputPwdLl.setVisibility(View.VISIBLE);
                    lockCutLine.setVisibility(View.GONE);
                    typeCodeLl.setVisibility(View.GONE);
                    forgetPwdandRegisterRl.setVisibility(View.VISIBLE);
                    loginTv.setText(getText(R.string.login));
                    cb_appointment.setVisibility(View.GONE);
                    tv_appointment.setVisibility(View.GONE);
                    rl_login.setVisibility(View.GONE);
                    pwdEt.setText("");
                    isRegister = false;
                    break;
                case 2://员工账号登录
                    loginTv.setText(getText(R.string.login));
                    isRegister = false;
                    break;
                case 3://验证码登录
                    phoneDropDownIv.setVisibility(View.GONE);
                    phoneCutLine.setVisibility(View.INVISIBLE);
                    inputPwdLl.setVisibility(View.GONE);
//                    forgetPwdandRegisterRl.setVisibility(View.GONE);
                    typeCodeLl.setVisibility(View.VISIBLE);
                    loginTv.setText(getText(R.string.login));
                    isRegister = false;
                    break;
                case 4://忘记密码
                    startActivity(ForgetPwdActivity.getLaunch(context));
                    break;
                case 5://注册账号
                    phoneDropDownIv.setVisibility(View.GONE);
                    phoneCutLine.setVisibility(View.INVISIBLE);
                    lockCutLine.setVisibility(View.GONE);
                    inputPwdLl.setVisibility(View.VISIBLE);
                    typeCodeLl.setVisibility(View.VISIBLE);
                    forgetPwdandRegisterRl.setVisibility(View.GONE);
                    loginTv.setText(getText(R.string.regist));
                    cb_appointment.setVisibility(View.VISIBLE);
                    tv_appointment.setVisibility(View.VISIBLE);
                    rl_login.setVisibility(View.VISIBLE);
                    isRegister = true;
                    pwdEt.setText("");
                    break;
            }
        }
    };

    /**
     * 用户注册
     */
    private boolean registerUser() {

        if(getUserInput()){
            code = codeEt.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                showHintDialog(getString(R.string.hint),getString(R.string.hint_input_code));
                return false;
            }
            if(code.length()<6){
                showHintDialog(getString(R.string.hint),getString(R.string.hint_input_code_6));
                return false;
            }
            if(!cb_appointment.isChecked()){
                showHintDialog(getString(R.string.hint),getString(R.string.choice_appointment));
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {

        AndroidBug5497Workaround.assistActivity(this);
        navBar.hideBack();
        navBar.showCancle();
        navBar.setColorRes(R.color.black_bg);

        typeCodeLl.setVisibility(View.GONE);
        phoneIv.setOnClickListener(this);
        forgetPwdTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        loginTv.setOnClickListener(this);
        getCodeTv.setOnClickListener(this);
        if (null !=PreferencesHelper.getData("phone")) {
            phoneNumber = PreferencesHelper.getData("phone");
            phoneNumberEt.setText(phoneNumber);
            phoneNumberEt.setSelection(phoneNumber.length());
        }
        final PopupWindow popupWindow;
        popupWindow = new PopupWindow(context);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pwd_hint, null);
        popupWindow.setContentView(view);

        pwdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    popupWindow.showAsDropDown(v);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            popupWindow.dismiss();
                        }
                    },1000);
                }
            }
        });
        pwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence == null || charSequence.toString().isEmpty()){
                    if (pwd_ic_del.getVisibility() != View.INVISIBLE) {
                        pwd_ic_del.setVisibility(View.INVISIBLE);
//                        pwd_ic_see.setVisibility(View.GONE);
                    }
                } else {
                    if (pwd_ic_del.getVisibility() != View.VISIBLE) {
                        pwd_ic_del.setVisibility(View.VISIBLE);
//                        pwd_ic_see.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @OnClick(R.id.pwd_ic_del)
    void resetPwdEditContent(){
        pwdEt.setText("");
        pwd_ic_del.setVisibility(View.INVISIBLE);
//        pwd_ic_see.setVisibility(View.GONE);
        pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwd_ic_see.setImageResource(R.drawable.icon_eye_in);
    }

    @OnClick(R.id.pwd_ic_see)
    void showPwdEditContent(){
        if (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == pwdEt.getInputType()) {
            pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            pwd_ic_see.setImageResource(R.drawable.icon_eye_in);
        } else {
            pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            pwd_ic_see.setImageResource(R.drawable.icon_eye_on);
        }
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    public void getCode() {
        phoneNumber = phoneNumberEt.getText().toString().trim();
        if (Tools.isChinaPhoneLegal(phoneNumber)) {
            if(timer == null) {
                timer = new MyCountDownTimer(getCodeTv, 60000, 1000);
            }
            timer.start();
        } else {
            showHintDialog(getString(R.string.hint),getString(R.string.input_correct_phone));
            return;
        }
        if (type == 1) {
            presenter.getCodeAction(type, phoneNumber);
        } else if (type == 3) {
            presenter.getCodeAction(3, phoneNumber);
        }
    }

    private void showHintDialog(String title,String meg) {
        CommonDialog dialog = new CommonDialog(context,title,meg,1);
        dialog.show();
    }

    private boolean getUserInput() {
        phoneNumber = phoneNumberEt.getText().toString().trim();
        pwd = pwdEt.getText().toString();
        if(pwd != null && pwd.contains(" ")){
            showHintDialog(getString(R.string.hint),getString(R.string.pwd_cannot_contain_space));
            return false;
        }
        if (TextUtils.isEmpty(phoneNumber) || !Tools.isChinaPhoneLegal(phoneNumber)) {
            showHintDialog(getString(R.string.hint),getString(R.string.input_correct_phone));
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            showHintDialog(getString(R.string.hint),getString(R.string.hint_input_password));
            return false;
        } else if (!Tools.isPwdRight(pwd)) {
            showHintDialog(getString(R.string.hint),getString(R.string.regist_password_mismatch));
            return false;
        }
        return true;
    }

    @Override
    public void loginSuccess() {
        RxBus.getDefault().send(new Object(), Constant.REFRESH_HOME_STATUE);
        startActivity(MainActivity.getLauncher(context));
        finish();
    }

    @Override
    public void loginFailure() {
        showHintDialog(getString(R.string.hint),getString(R.string.account_pwd_fault));
    }

    @Override
    public void checkCodeSuccess() {

    }

    @OnClick(R.id.tv_back_login)
    public void backLogin(){
        handler.sendEmptyMessage(1);
    }

    @Override
    public void registerSuccess() {
        ToastMgr.show(getString(R.string.register_success));
        handler.sendEmptyMessage(1);
    }

    @Override
    public void registerFailure() {

    }

    @Override
    public void getCodeSuccess() {
        Log.e(TAG, "----getCodeSuccess:");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_iv:
                showPopuWindow(v);
                break;
            case R.id.forget_pwd_tv:
                handler.sendEmptyMessage(4);
                break;
            case R.id.register_tv:
                handler.sendEmptyMessage(5);
                break;
            case R.id.get_code_tv:
                getCode();
                break;
            case R.id.login_tv:
                if (!isRegister) {
                    if (type == 1) {
//                        if(getUserInput()){
                            //手机号密码登录
                        if (TextUtils.isEmpty(phoneNumberEt.getText().toString().trim()) || !Tools.isChinaPhoneLegal(phoneNumberEt.getText().toString().trim())) {
                            showHintDialog(getString(R.string.hint),getString(R.string.input_correct_phone));

                        } else if (TextUtils.isEmpty(pwdEt.getText().toString().trim())) {
                            showHintDialog(getString(R.string.hint),getString(R.string.hint_input_password));

                        }else{
                            presenter.loginAction(0, phoneNumberEt.getText().toString().trim(), pwdEt.getText().toString().trim(), code);
                        }

//                        }
                    } else if (type == 3) {
                        code = codeEt.getText().toString().trim();
                        if (TextUtils.isEmpty(code)) {
                            showHintDialog(getString(R.string.hint),getString(R.string.hint_input_code));
                            return;
                        }
                        if(code.length()<6){
                            showHintDialog(getString(R.string.hint),getString(R.string.hint_input_code_6));
                            return;
                        }
                        phoneNumber = phoneNumberEt.getText().toString().trim();
                        if(TextUtils.isEmpty(phoneNumber)) {
                            showHintDialog(getString(R.string.hint),getString(R.string.hint_input_phone));
                            return;
                        }
                        if(!Tools.isChinaPhoneLegal(phoneNumber)) {
                            showHintDialog(getString(R.string.hint),getString(R.string.input_correct_phone));
                            return;
                        }
                        //手机号验证码登录
                        presenter.loginAction(1, phoneNumber, pwd, code);
                    }
                }else {
                    //调用注册接口
                    if(registerUser()){
                        presenter.registerAction(phoneNumber, pwd, code);
                    }

                }
                break;
            default:
                break;
        }
    }

    /**
     * 登录方式选择
     *
     * @param v
     */
    private void showPopuWindow(View v) {
        if(popupWindow==null){
            popupWindow = new PopupWindow(context);
        }

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.title_popuwindow, null);
        phoneTypeTv = view.findViewById(R.id.phone_login_tv);
        workerTypeTv = view.findViewById(R.id.worker_login_tv);
        codeTypeTv = view.findViewById(R.id.code_login_tv);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        if(!popupWindow.isShowing()){
            popupWindow.showAsDropDown(v);
        }

        getLoginType();
    }

    private void getLoginType() {
        phoneTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                handler.sendEmptyMessage(type);

                popupWindow.dismiss();
            }
        });
        workerTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                handler.sendEmptyMessage(type);
                popupWindow.dismiss();
            }
        });
        codeTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 3;
                handler.sendEmptyMessage(type);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void goLogin() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null ) {
            timer.cancel();
            timer = null;
        }
    }

    public static class MyCountDownTimer extends CountDownTimer {
        WeakReference<TextView> view;

        public MyCountDownTimer(TextView v, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.view = new WeakReference<TextView>(v);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            view.get().setClickable(false);
            view.get().setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            view.get().setClickable(true);
            view.get().setText(R.string.action_get_code_again);
        }

    }

    @OnClick(R.id.iv_finish)
    public void actionFinish(){
        finish();
    }

    @OnClick(R.id.tv_appointment_)
    public void goAppointment(){
        startActivity(WebActivity.getLauncher(context,0));
    }
}
