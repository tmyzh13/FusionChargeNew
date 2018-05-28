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
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.corelibs.base.BaseActivity;
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
import com.huawei.fusionchargeapp.model.beans.MyTaocanBean;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;
import com.huawei.fusionchargeapp.model.beans.PayStyleBean;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.PayPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.utils.alipay.AuthResult;
import com.huawei.fusionchargeapp.utils.alipay.OrderInfoUtil2_0;
import com.huawei.fusionchargeapp.utils.alipay.PayResult;
import com.huawei.fusionchargeapp.views.interfaces.PayView;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.huawei.fusionchargeapp.weights.PayFailDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by issuser on 2018/4/20.
 */

public class PayActivity extends BaseActivity<PayView,PayPresenter> implements PayView {

    @Bind(R.id.nav)
    NavBar nav;
    @Bind(R.id.list)
    NoScrollingListView listView;
    @Bind(R.id.tv_charger_enegry)
    TextView tv_charger_enegry;
    @Bind(R.id.tv_charger_fee)
    TextView tv_charger_fee;
    @Bind(R.id.tv_charge_service_fee)
    TextView tv_charge_service_fee;
    @Bind(R.id.tv_total_fee)
    TextView tv_total_fee;


    private PayStyleAdpter adapter;
    private Context context=PayActivity.this;
    private List<PayStyleBean> list;
    private CommonDialog commonDialog;
    private PayFailDialog dialog;
    private String orderNum;
    //type 0 订单查询  type 1 页面带值
    private String type;
    private long id;

    public static Intent getLauncher(Context context,String orderNum,String type){
        Intent intent =new Intent(context,PayActivity.class);
        intent.putExtra("num",orderNum);
        intent.putExtra("type",type);
        return intent;
    }

    public static Intent getLauncherWithBean(Context context,String orderNum,String type,PayInfoBean bean){
        Intent intent=new Intent(context,PayActivity.class);
        intent.putExtra("num",orderNum);
        intent.putExtra("type",type);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",bean);
        intent.putExtra("bundle",bundle);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.getMyTaoCan();
        nav.setNavTitle(getString(R.string.pay));
        nav.setImageBackground(R.drawable.nan_bg);

        orderNum=getIntent().getStringExtra("num");
        type=getIntent().getStringExtra("type");

        adapter=new PayStyleAdpter(context);
        list =new ArrayList<>();

        PayStyleBean bean =new PayStyleBean();
        bean.imgRes=R.mipmap.pay_money;
        bean.name=getString(R.string.pay_balance);
        bean.hint=getString(R.string.pay_balance_hint);
        bean.type="0";
        PayStyleBean bean1=new PayStyleBean();
        bean1.imgRes=R.mipmap.list_ic_weixin;
        bean1.name=getString(R.string.pay_wechat);
        bean1.hint=getString(R.string.pay_wechat_hint);
        bean1.type="1";
        PayStyleBean bean2=new PayStyleBean();
        bean2.imgRes=R.mipmap.list_ic_zhi;
        bean2.name=getString(R.string.pay_alipay);
        bean2.hint=getString(R.string.pay_alipay_hint);
        bean2.type="2";
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

        commonDialog =new CommonDialog(context,"",getString(R.string.pay_balance_not_enough),1);
        dialog=new PayFailDialog(context);

        if(type.equals("0")){
            presenter.getPayDetailInfo(orderNum);
        }else {
            Bundle bundle=getIntent().getBundleExtra("bundle");
            payInfoBean=(PayInfoBean) bundle.getSerializable("data");
            tv_charger_enegry.setText(payInfoBean.chargePowerAmount+getString(R.string.du));
            tv_charger_fee.setText(payInfoBean.eneryCharge+getString(R.string.yuan));
            tv_charge_service_fee.setText(payInfoBean.serviceCharge+getString(R.string.yuan));
            tv_total_fee.setText("￥"+payInfoBean.consumeTotalMoney);
        }

    }

    @Override
    public void renderMyTaoCan(MyTaocanBean tcBean) {
        if (tcBean != null) {
            id = tcBean.businessPackageId;
            PayStyleBean bean =new PayStyleBean();
            bean.type="5";
            bean.imgRes=R.drawable.list_01;
            bean.name=tcBean.businessName;
            if (tcBean.limitType == 0) {
                bean.hint = getString(R.string.remain_charge_num,tcBean.limitCondition+"");
            } else {
                bean.hint = getString(R.string.invalid_time,tcBean.businessStartTime.substring(0, Tools.DATE_LENGTH_FROM_SERVER)+"~"+tcBean.businessEndTime.substring(0, Tools.DATE_LENGTH_FROM_SERVER));
            }
            List<PayStyleBean> temp = new ArrayList<>();
            temp.add(bean);
            for (int i= 0;i<list.size();i++){
                temp.add(list.get(i));
            }
            list = temp;
            adapter.replaceAll(list);
        }
    }

    @Override
    protected PayPresenter createPresenter() {
        return new PayPresenter();
    }

    @OnClick(R.id.tv_pay)
    public void goPay(){
        //选中的支付类型
        String type=list.get(adapter.getCurrentPosition()).type;
        if(payInfoBean==null){
            ToastMgr.show(getString(R.string.hint_error_data));
            finish();
            return;
        }
        if(type.equals("0")){
            presenter.payAction(orderNum,payInfoBean.consumeTotalMoney,3);

        }else if(type.equals("1")){

        }else if(type.equals("2")){
            presenter.payAction(orderNum,payInfoBean.consumeTotalMoney,1);
        } else if (type.equals("5")) {

            presenter.payAction(orderNum,payInfoBean.consumeTotalMoney,5,id);
        }
    }

    private void payForWechat(){
//        presenter.getPrepayID(orderId);
    }

//    @Override
//    public void renderPrepayId(PrepayBean bean) {
//        PayWithWechat payWithWechat=new PayWithWechat(contex,bean.prepayId);
//    }

    // 商户PID
    public static final String APPID = "2018051760088828";
    // 商户收款账号
    public static final String SELLER = "2088131035173156";
    public static final String RSA_PRIVATE="";
    public  static final String RSA2_PRIVATE ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANBuGkjv4I+nCj6VKP7RAt" +
            "AmnvdeWI6SClAF0nFlYy3U2Xx+iyGmfBnURckhXjvABCwt9zr4tx76eJM2FLNTHRXvRtgzNLH9vrEdl9+kOk32+8ft0yCmcuQy7GVNHiss4" +
            "BPcHoC24r5AdHIuVHo9LtUaEoPRury5Zs98LkTSnCDdAgMBAAECgYAr7ulPcnCNFxKLunkXrQrAwUNEtPhJpPLTh0aibFKJeJIXMb017JfAAg" +
            "RrrsLkKUc34MB6B67hsr3zmbSnT9+TPHwk8zvA8aQ6Vp1xQseRcMT" +
            "wPpYQrPRS7yTa6Scrh87mBP/nO3ofI5b0l30/c8IaEdADyK2+muS7gcP3fKVagQJBAPVJUujwi2E267tCOxFkWkfoY9ipWvrhaUCmig" +
            "15UeqebsI+l9bEu8IiuFT3BHEtiU2x5nViR5OpDa1BuNw0X1ECQQDZiKs3y52r9toM1RIX17CXkft10ehOVYzSLfMBonbDFRjtb8AK5" +
            "p/7qtx1XqpyK6nLOhNlb1bavH1OGY5cPb3NAkEAnP3zPPhInU4jjtUTLUyHpCKPVWr/ujdvwFtXXMxbq8j/pv+c+28rpVPHm0oG49F7" +
            "YekNUfA3U8EN6eh4wygaIQJBAK9EOUd8K6549phuN+Sz6NlGSkdUd4pdzVcupGNFxdBKOEpdxmpCBNZdhMCgJE5WtbhDM4t/mIgmZAM" +
            "//f591GUCQE0l5ZfCoplOqCmARjABKTPLmPH/3PpDwkoKjukOPuJWuJYTj9ZiHzzut8fqwbsGtm3tnxJGgZHGx/VgSyoNDms=";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String orderInfo;
    private void payForAli(){
        /**
         * 如果APPID是空值或私钥两个全是空，则弹出警告对话框告诉开发者“需要配置APPID|RSA_PRIVATE”
         */
        if (TextUtils.isEmpty(APPID) ||  TextUtils.isEmpty(RSA2_PRIVATE)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；防止本地orderInfo被认为更改，例如买个冰箱改成买个鸡蛋
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);//rsa2私钥已经被赋值
        //在这里，传入APPID和私钥，得到请求map，即包含支付订单信息的map
        String url = "http://114.115.144.154:8088/charger/phone/aliNotify";
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID,SELLER, rsa2,"0.01","充电平台支付",orderNum,url);
        //将map解析成一个String类型的支付订单
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        //是rsa2类型的私钥吗？如果rsa2私钥已经被赋值，那么privateKey就被设置成rsa2，否则就被设置成rsa。这就是支付宝说的“如果你有俩私钥，优先使用RSa2私钥，靠！”
        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        //对，privateKey就是商户私钥！通过此方法对商户的私钥进行“签名”处理，处理后就会生成（返回）一个sign=GKHJL%……&*的一大串字串
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        //最终，结合orderparam参数与sign签名字串，搞成orderInfo字串；
        orderInfo = orderParam + "&" + sign;
        if(Tools.isNull(payResultBean.orderInfo)){
            orderInfo = orderParam + "&" + sign;
        }else{
            orderInfo=payResultBean.orderInfo;
        }
        //新开一个线程，将orderInfo字串传入到PayTask任务中去
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //新建一个PAyTask对象
                PayTask alipay = new PayTask(PayActivity.this);
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
                        ToastMgr.show(getString(R.string.pay_error));
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
        };
    };

    private PayInfoBean payInfoBean;

    @Override
    public void renderData(PayInfoBean bean) {
        this.payInfoBean=bean;
        tv_charger_enegry.setText(bean.chargePowerAmount+getString(R.string.du));
        tv_charger_fee.setText(bean.eneryCharge+getString(R.string.yuan));
        tv_charge_service_fee.setText(bean.serviceCharge+getString(R.string.yuan));
        tv_total_fee.setText("￥"+bean.consumeTotalMoney);
    }

    private PayResultBean payResultBean;

    @Override
    public void paySuccess(PayResultBean payResultBean) {
        this.payResultBean=payResultBean;
        if(payResultBean.type==3||payResultBean.type==5){
            while(!AppManager.getAppManager().currentActivity().getClass().equals( MainActivity.class)){
                AppManager.getAppManager().finishActivity();
            }
            //给首页发送一个消息去掉未支付提示栏
            HomeRefreshBean bean =new HomeRefreshBean();
            bean.type=0;
            RxBus.getDefault().send(bean, Constant.HOME_STATUE_REFRESH);
            startActivity(PayCompleteActivity.getLauncher(context,orderNum));
            finish();
        }else if(payResultBean.type==1){
            payForAli();
        }

    }

    @Override
    public void payBalanceNotEnough() {
            commonDialog.show();
            commonDialog.setDialogBackground();
    }

    @Override
    public void payFail() {
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //刷新首页的界面
        RxBus.getDefault().send(new Object(),Constant.REFRESH_HOME_STATUE);
    }

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(context));
    }
}
