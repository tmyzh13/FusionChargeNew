package com.huawei.fusionchargeapp.views.mycount;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.common.AppManager;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.ToastMgr;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.CashPatternAdapter;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;
import com.huawei.fusionchargeapp.presenter.CashPatternPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.utils.alipay.AuthResult;
import com.huawei.fusionchargeapp.utils.alipay.PayResult;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.TaoCanPayActivity;
import com.huawei.fusionchargeapp.views.interfaces.CashPatternView;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.Map;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/11.
 */

public class CashPatternActivity extends BaseActivity<CashPatternView,CashPatternPresenter> implements CashPatternView {
    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.cash_num)
    EditText cashNum;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.cach_pattern)
    TextView cashPattern;

    @BindString(R.string.my_acount_recharge)
    String recharge;
    @BindString(R.string.my_acount_put_forward)
    String recover;

    public static final String KEY_CASH_OPERATION_PATTER = "key_cash_operation_pattern";
    private boolean isRechargeType = false;
    private CashPatternAdapter adapter;
    private Context context=CashPatternActivity.this;

    @Override
    public void goLogin() {
        PreferencesHelper.clearData();
        startActivity(LoginActivity.getLauncher(context));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash_pattern;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setColorRes(R.color.blue);
        isRechargeType = getIntent().getBooleanExtra(KEY_CASH_OPERATION_PATTER, false);
        bar.setNavTitle(isRechargeType ? recharge : recover);
        cashPattern.setText(getString(R.string.cash_select_pattern, isRechargeType ? recharge : recover));
        cashNum.setHint(getString(R.string.cash_input_with_pattern, isRechargeType ? recharge : recover));
        submit.setText(isRechargeType ? recharge : recover);

        adapter = new CashPatternAdapter(this);
        listView.setAdapter(adapter);
    }

    private int payType=1;
    @OnClick(R.id.submit)
    void submit(){
        if(Tools.isNull(cashNum.getText().toString())){
            ToastMgr.show(getString(R.string.cash_input_with_pattern, isRechargeType ? recharge : recover));
            return;
        }
        //提交
        if(adapter.getCurrentSelect()==0){
            payType=1;
        }else if(adapter.getCurrentSelect()==1){
            payType=2;
        }
        if(true){
            presenter.payBalance(Double.parseDouble(cashNum.getText().toString().trim()),payType);
        }
    }

    @Override
    protected CashPatternPresenter createPresenter() {
        return new CashPatternPresenter();
    }

    @Override
    public void paySuccess(PayResultBean bean) {
        if(bean.payType==1){
            //支付宝
            payForAli(bean.orderInfo);
        }else{
            //微信
        }
    }

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private void payForAli(final String orderInfo){

        //新开一个线程，将orderInfo字串传入到PayTask任务中去
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //新建一个PAyTask对象
                PayTask alipay = new PayTask(CashPatternActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.e("TAG","resultInfo:"+resultInfo);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastMgr.show(getString(R.string.pay_success));
                        finish();
                    } else {
                        Log.e("TAG","resultInfo:"+resultInfo);
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastMgr.show(getString(R.string.pay_fail));
                    }


                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(context,
                                getString(R.string.auth_success)+"\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();

                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(context,
                                getString(R.string.auth_failed) + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
