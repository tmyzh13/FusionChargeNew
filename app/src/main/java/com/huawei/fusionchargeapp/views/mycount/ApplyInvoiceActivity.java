package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.utils.ToastMgr;
import com.corelibs.views.NoScrollingListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapters.InvoicePayAdapter;
import com.huawei.fusionchargeapp.model.apis.ApplyInvoiceApi;
import com.huawei.fusionchargeapp.model.beans.AppointResponseBean;
import com.huawei.fusionchargeapp.model.beans.PayStyleBean;
import com.huawei.fusionchargeapp.presenter.ApplyInvoicePresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.interfaces.ApplyInvoiceView;
import com.huawei.fusionchargeapp.views.interfaces.AppointView;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
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

    private Context context=ApplyInvoiceActivity.this;
    private InvoicePayAdapter adapter;
    private ArrayList<String> orderNums;
    private double total;
    private String moreContent="";

    public static Intent getLauncher(Context context){
        Intent intent =new Intent(context,ApplyInvoiceActivity.class);
        return intent;
    }

    @Override
    public void goLogin() {
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

        total=getIntent().getDoubleExtra("selected_totaol_money",0);
        orderNums=getIntent().getStringArrayListExtra("selected_order_num");

        tv_invoice_money.setText(total+"");

        adapter=new InvoicePayAdapter(context);
        List<PayStyleBean> list =new ArrayList<>();
        PayStyleBean bean =new PayStyleBean();
        bean.imgRes=R.drawable.list_04;
        bean.name=getString(R.string.apply_invoice_to_pay);
        bean.hint=getString(R.string.apply_invoice_to_pay_postage);
        bean.type="0";
        PayStyleBean bean1=new PayStyleBean();
        bean1.imgRes=R.mipmap.list_ic_weixin;
        bean1.name=getString(R.string.pay_wechat);
        bean1.hint=getString(R.string.apply_invoice_wechat_postage);
        bean1.type="1";
        PayStyleBean bean2=new PayStyleBean();
        bean2.imgRes=R.mipmap.account_02;
        bean2.name=getString(R.string.pay_alipay);
        bean2.hint=getString(R.string.apply_invoice_ali_postage);
        bean2.type="2";
        PayStyleBean bean3=new PayStyleBean();
        bean3.imgRes=R.mipmap.account_03;
        bean3.name=getString(R.string.remain_pay);
        bean3.hint=getString(R.string.apply_invoice_to_pay_postage);
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

        if(total<200){
            ll_post.setVisibility(View.VISIBLE);
        }

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

        if(ll_post.getVisibility()==View.VISIBLE){
            //支付邮费费用的 逻辑
        }

        presenter.applyInvoice(orderNums,type,et_invoice_title.getText().toString(),
                et_invoice_tax.getText().toString().trim(),et_invoice_connect.getText().toString().trim(),
                total,moreContent,et_invoice_receive.getText().toString().trim(),et_invoice_connect.getText().toString().trim(),
                et_invoice_address.getText().toString().trim(),et_invoice_email.getText().toString().trim());
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
    public void applySuccess() {
        finish();
    }
}
