package com.huawei.fusionchargeapp.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.common.AppManager;
import com.corelibs.utils.ToastMgr;
import com.corelibs.utils.rxbus.RxBus;
import com.corelibs.views.NoScrollingListView;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapters.PayStyleAdpter;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.HomeRefreshBean;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;
import com.huawei.fusionchargeapp.model.beans.PayStyleBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.PayPresenter;
import com.huawei.fusionchargeapp.presenter.TaocanPayPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.utils.alipay.AuthResult;
import com.huawei.fusionchargeapp.utils.alipay.OrderInfoUtil2_0;
import com.huawei.fusionchargeapp.utils.alipay.PayResult;
import com.huawei.fusionchargeapp.views.interfaces.PayView;
import com.huawei.fusionchargeapp.views.interfaces.TaoCanPayView;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by john on 2018/5/8.
 */

public class TaoCanPayActivity extends BaseActivity<TaoCanPayView,TaocanPayPresenter> implements TaoCanPayView {

    @Bind(R.id.nav)
    NavBar nav;
    @Bind(R.id.list)
    NoScrollingListView listView;
    @Bind(R.id.tv_total_fee)
    TextView tv_total_fee;
    @Bind(R.id.tv_tao_can_name)
    TextView tv_tao_can_name;
    @Bind(R.id.tv_style)
    TextView tv_style;
    @Bind(R.id.ll_electronic)
    LinearLayout ll_electronic;
    @Bind(R.id.tv_electronic)
    TextView tv_electronic;
    @Bind(R.id.tv_validate_date)
    TextView tv_validate_date;

    private Context context=TaoCanPayActivity.this;
    private PayStyleAdpter adapter;
    private List<PayStyleBean> list;
    private int appBussinessId;
    private double totalFee;
    private int limitType;
    private String name;
    private int electronic;
    private String startTime;
    private String endTime;

    public static Intent getLauncher(Context context,int appBussinessId,double totalFee,
                                     int limitType,String name,int electronic,String startTime,String endTime){
        Intent intent=new Intent(context,TaoCanPayActivity.class);
        intent.putExtra("id",appBussinessId);
        intent.putExtra("total",totalFee);
        intent.putExtra("type",limitType);
        intent.putExtra("name",name);
        intent.putExtra("electronic",electronic);
        intent.putExtra("start",startTime);
        intent.putExtra("end",endTime);
        return intent;
    }

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(context));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tao_can_pay;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setNavTitle(getString(R.string.pay));
        nav.setImageBackground(R.drawable.nan_bg);

        appBussinessId=getIntent().getIntExtra("id",0);
        totalFee=getIntent().getDoubleExtra("total",0);
        limitType=getIntent().getIntExtra("type",0);
        name=getIntent().getStringExtra("name");
        electronic=getIntent().getIntExtra("electronic",0);
        startTime=getIntent().getStringExtra("start");
        endTime=getIntent().getStringExtra("end");

        tv_tao_can_name.setText(name);
        tv_total_fee.setText(totalFee+"");
        if(limitType==0){
            tv_style.setText(getString(R.string.pay_limite));
            tv_electronic.setText(electronic+"");
        }else{
            tv_style.setText(getString(R.string.pay_unlimite));
            ll_electronic.setVisibility(View.GONE);
        }
        tv_validate_date.setText(startTime.substring(0, Tools.BIRTHDAY_LENGTH)+"-"+endTime.substring(0,Tools.BIRTHDAY_LENGTH));
        adapter=new PayStyleAdpter(context);
        list =new ArrayList<>();
        PayStyleBean bean =new PayStyleBean();
        bean.imgRes=R.mipmap.pay_money;
        bean.name=getString(R.string.pay_balance);
        bean.hint=getString(R.string.pay_balance_hint);
        bean.type="3";
        PayStyleBean bean1=new PayStyleBean();
        bean1.imgRes=R.mipmap.list_ic_weixin;
        bean1.name=getString(R.string.pay_wechat);
        bean1.hint=getString(R.string.pay_wechat_hint);
        bean1.type="2";
        PayStyleBean bean2=new PayStyleBean();
        bean2.imgRes=R.mipmap.list_ic_zhi;
        bean2.name=getString(R.string.pay_alipay);
        bean2.hint=getString(R.string.pay_alipay_hint);
        bean2.type="1";
        list.add(bean);
        list.add(bean1);
        list.add(bean2);

        adapter.addAll(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setCurrentPosition(position);
            }
        });
    }

    @Override
    protected TaocanPayPresenter createPresenter() {
        return new TaocanPayPresenter();
    }

    @Override
    public void paySuccess(PayResultBean bean) {
        if(bean.payType==3){
            ToastMgr.show(getString(R.string.pay_success));
            finish();
        }else if(bean.payType==1){
            //支付包
            orderInfo=bean.orderInfo;
            payForAli();
        }else if(bean.payType==2){
            //微信
        }


    }

    private String orderInfo;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private void payForAli(){

            //新开一个线程，将orderInfo字串传入到PayTask任务中去
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    //新建一个PAyTask对象
                    PayTask alipay = new PayTask(TaoCanPayActivity.this);
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
                        while(!AppManager.getAppManager().currentActivity().getClass().equals( MainActivity.class)){
                            AppManager.getAppManager().finishActivity();
                        }
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

    @OnClick(R.id.tv_pay)
    public void goPay(){

        presenter.payTaocan(Integer.parseInt(list.get(adapter.getCurrentPosition()).type),appBussinessId,totalFee);
    }
}
