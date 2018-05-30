package com.huawei.fusionchargeapp.views.mycount;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.utils.ToastMgr;
import com.corelibs.utils.rxbus.RxBus;
import com.corelibs.views.NoScrollingListView;
import com.corelibs.views.navigation.AndroidBug5497Workaround;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapters.InvoicePayAdapter;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ApplyInvoiceApi;
import com.huawei.fusionchargeapp.model.beans.ApplyInvoiceResultBean;
import com.huawei.fusionchargeapp.model.beans.AppointResponseBean;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.PayStyleBean;
import com.huawei.fusionchargeapp.model.beans.RepayInvoiceBean;
import com.huawei.fusionchargeapp.model.beans.RequestApplyInvoiceBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.ApplyInvoicePresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.utils.alipay.AuthResult;
import com.huawei.fusionchargeapp.utils.alipay.PayResult;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.interfaces.ApplyInvoiceView;
import com.huawei.fusionchargeapp.views.interfaces.AppointView;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.functions.Func6;

/**
 * Created by john on 2018/5/7.
 */

public class ApplyInvoiceActivity extends BaseActivity <ApplyInvoiceView,ApplyInvoicePresenter> implements ApplyInvoiceView{

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.list)
    NoScrollingListView listView;
    @Bind(R.id.et_invoice_title)
    EditText et_invoice_title;
    @Bind(R.id.et_invoice_tax)
    EditText et_invoice_tax;
    @Bind(R.id.tv_invoice_money)
    TextView tv_invoice_money;
    @Bind(R.id.et_invoice_receive)
    EditText et_invoice_receive;
    @Bind(R.id.et_invoice_connect)
    EditText et_invoice_connect;
    @Bind(R.id.et_invoice_address)
    EditText et_invoice_address;
    @Bind(R.id.et_invoice_email)
    EditText et_invoice_email;
    @Bind(R.id.tv_submit)
    TextView tv_submit;
    @Bind(R.id.tv_more_info)
    TextView tv_more_info;
    @Bind(R.id.rb_company)
    RadioButton rb_company;
    @Bind(R.id.rb_people)
    RadioButton rb_people;
    @Bind(R.id.ll_post)
    LinearLayout ll_post;
    @Bind(R.id.ll_go_input)
    LinearLayout ll_go_input;

    private Context context=ApplyInvoiceActivity.this;
    private InvoicePayAdapter adapter;
    private List<String> orderNums;
    private double total;
    private String moreContent="";
    private List<PayStyleBean> list =new ArrayList<>();
    private double postage = 10;
    private boolean is_order_no_pay_but_had_invoiced = false;
    private String orderId;

    public static Intent getLauncher(Context context){
        Intent intent =new Intent(context,ApplyInvoiceActivity.class);
        return intent;
    }

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(context));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_invoice;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getString(R.string.apply_invoice));
        navBar.setImageBackground(R.drawable.nan_bg);
        AndroidBug5497Workaround.assistActivity(this);
        adapter=new InvoicePayAdapter(context);

        PayStyleBean bean =new PayStyleBean();
        bean.imgRes=R.drawable.list_04;
        bean.name=getString(R.string.apply_invoice_to_pay);
        bean.hint=getString(R.string.apply_invoice_to_pay_postage);
        bean.type="6";
        bean.needMoney = 10;
        PayStyleBean bean1=new PayStyleBean();
        bean1.imgRes=R.mipmap.list_ic_weixin;
        bean1.name=getString(R.string.pay_wechat);
        bean1.hint=getString(R.string.apply_invoice_wechat_postage);
        bean1.type="2";
        PayStyleBean bean2=new PayStyleBean();
        bean2.imgRes=R.mipmap.account_02;
        bean2.name=getString(R.string.pay_alipay);
        bean2.hint=getString(R.string.apply_invoice_ali_postage);
        bean2.type="1";
        PayStyleBean bean3=new PayStyleBean();
        bean3.imgRes=R.mipmap.account_03;
        bean3.name=getString(R.string.remain_pay);
        bean3.hint=getString(R.string.apply_invoice_to_pay_postage_8);
        bean3.type="3";
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean);

        adapter.addAll(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setCurrentPosition(position);
            }
        });

        //rxjava联合判断
        Observable<CharSequence> invoiceTitle= RxTextView.textChanges(et_invoice_title).skip(1);
        Observable<CharSequence> invoiceTax= RxTextView.textChanges(et_invoice_tax).skip(1);
        Observable<CharSequence> invoiceReceive= RxTextView.textChanges(et_invoice_receive).skip(1);
        Observable<CharSequence> invoiceConnect= RxTextView.textChanges(et_invoice_connect).skip(1);
        Observable<CharSequence> invoiceAddress= RxTextView.textChanges(et_invoice_address).skip(1);
        Observable<CharSequence> invoiceEmail= RxTextView.textChanges(et_invoice_email).skip(1);
        Observable.combineLatest(invoiceTitle, invoiceTax, invoiceReceive,invoiceConnect ,invoiceAddress,invoiceEmail,new Func6<CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, CharSequence charSequence5, CharSequence charSequence6) {
                boolean isTitleValid= !TextUtils.isEmpty(et_invoice_title.getText().toString());
                boolean isTaxValid=!TextUtils.isEmpty(et_invoice_tax.getText().toString());
                boolean isReceiveValid=!TextUtils.isEmpty(et_invoice_receive.getText().toString());
                boolean isConnectValid= !TextUtils.isEmpty(et_invoice_connect.getText().toString());
                boolean isAddressValid=!TextUtils.isEmpty(et_invoice_address.getText().toString());
                boolean isEmailValid=!TextUtils.isEmpty(et_invoice_email.getText().toString());
                return isTitleValid&isTaxValid&isReceiveValid&isConnectValid&isAddressValid&isEmailValid;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                //控制按钮的使用
                if(aBoolean){
                    tv_submit.setBackgroundResource(R.drawable.tv_corner_blue);
                    tv_submit.setEnabled(true);
                }else{
                    tv_submit.setBackgroundResource(R.drawable.tv_corner_gray);
                    tv_submit.setEnabled(false);
                }

            }
        });

        Intent intent = getIntent();
        is_order_no_pay_but_had_invoiced = intent.getBooleanExtra(InvoiceHistoryActivity.UNPAY_INVOICE,false);
        if (is_order_no_pay_but_had_invoiced) {
            orderId = intent.getStringExtra(InvoiceHistoryActivity.ORDER_ID);
            presenter.getUnpayInvocie(orderId);
        } else{
            total=intent.getDoubleExtra("selected_totaol_money",0);
            orderNums=intent.getStringArrayListExtra("selected_order_num");
            tv_invoice_money.setText(String.format("%1$.2f",total));
            if(total<200){
                ll_post.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void getUnpayInvoiceSucess(RequestApplyInvoiceBean bean) {
        initView(bean);
    }

    @Override
    public void repayInvoiceSucess(ApplyInvoiceResultBean bean) {
        applySuccess(bean);
    }

    //未支付订单进行支付时,依据订单号初始化
    private void initView(RequestApplyInvoiceBean bean) {
        if (getString(R.string.invoice_company).equals(bean.invoiceInfo.type)) {
            rb_company.setChecked(true);
        } else {
            rb_people.setChecked(true);
        }
        rb_people.setClickable(false);
        rb_company.setClickable(false);

        total = bean.invoiceInfo.amount;
        tv_invoice_money.setText(total+"");
        if(total<200){
            ll_post.setVisibility(View.VISIBLE);
        }

        et_invoice_title.setText(bean.invoiceInfo.title);
        et_invoice_title.setEnabled(false);
        et_invoice_tax.setText(bean.invoiceInfo.code);
        et_invoice_tax.setEnabled(false);
        et_invoice_address.setText(bean.invoiceInfo.recAddr);
        et_invoice_address.setEnabled(false);
        et_invoice_email.setText(bean.invoiceInfo.email);
        et_invoice_email.setEnabled(false);
        et_invoice_receive.setText(bean.invoiceInfo.name);
        et_invoice_receive.setEnabled(false);
        et_invoice_connect.setText(bean.invoiceInfo.phone);
        et_invoice_connect.setEnabled(false);
        if (null != bean.invoiceInfo.kpRemark) {
            tv_more_info.setText(bean.invoiceInfo.kpRemark);
        }
        ll_go_input.setEnabled(false);
        tv_more_info.setEnabled(false);
        orderNums = bean.orderRecordNums;
    }

    @Override
    protected ApplyInvoicePresenter createPresenter() {
        return new ApplyInvoicePresenter();
    }

    @OnClick(R.id.ll_go_input)
    void goInputContent(){
        startActivityForResult(InputContentActivity.getLauncher(context,tv_more_info.getText().toString().trim()),10);
    }

    @OnClick(R.id.tv_submit)
    public void submit(){

        if(!Tools.isChinaPhoneLegal(et_invoice_connect.getText().toString().trim())){
            ToastMgr.show(getString(R.string.input_correct_phone));
            return;
        }

        if(!Tools.isEmailVailid(et_invoice_email.getText().toString().trim())){
            ToastMgr.show(getString(R.string.hint_input_right_email));
            return;
        }

        String type="";
        if(rb_company.isChecked()){
            type=getString(R.string.invoice_company);
        }else if(rb_people.isChecked()){
            type=getString(R.string.invoice_people);
        }

        postage = total < 200 ? list.get(adapter.getCurrentPosition()).needMoney: 0;
        int payType = total < 200 ? Integer.parseInt(list.get(adapter.getCurrentPosition()).type) : 3;
//        if(ll_post.getVisibility()==View.VISIBLE){
//            //支付邮费费用的 逻辑
//            if(adapter.getCurrentPosition()==2){
//                //支付邮费
//            }
//        }
        if (is_order_no_pay_but_had_invoiced) {
            RepayInvoiceBean bean = new RepayInvoiceBean();
            bean.orderRecordNum = orderId;
            bean.payType = payType;
            bean.postage = postage;
            presenter.repayInvoice(bean);
        } else {
            presenter.applyInvoice(orderNums,payType,postage,type,et_invoice_title.getText().toString(),
                    et_invoice_tax.getText().toString().trim(),et_invoice_connect.getText().toString().trim(),
                    total,moreContent,et_invoice_receive.getText().toString().trim(),et_invoice_connect.getText().toString().trim(),
                    et_invoice_address.getText().toString().trim(),et_invoice_email.getText().toString().trim());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10){
            if(resultCode==100){
                moreContent=data.getStringExtra("content");
                tv_more_info.setText(data.getStringExtra("content"));
            }
        }
    }


    @Override
    public void applySuccess(ApplyInvoiceResultBean bean) {

//        if (bean == null || bean.isNeedPay == 0) {
//            finish();
//        } else {
//            //根据支付类型调用相应接口
//        }
        if(bean.payType==1){
            //支付宝
            payForAli(bean.orderInfo);
        }else if(bean.payType==2){
            //微信
        }else if(bean.payType==3||bean.payType==6){
            finish();
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
                PayTask alipay = new PayTask(ApplyInvoiceActivity.this);
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
                        RxBus.getDefault().send(new Object(), Constant.REFRESH_APPLY_ORDER_LIST_ACTIVITY);
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
