package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.views.NoScrollingListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapters.PayStyleAdpter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.PayStyleBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.PayPresenter;
import com.huawei.fusionchargeapp.presenter.TaocanPayPresenter;
import com.huawei.fusionchargeapp.views.interfaces.PayView;
import com.huawei.fusionchargeapp.views.interfaces.TaoCanPayView;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

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

    private Context context=TaoCanPayActivity.this;
    private PayStyleAdpter adapter;
    private List<PayStyleBean> list;
    private int appBussinessId;
    private double totalFee;

    public static Intent getLauncher(Context context,int appBussinessId,double totalFee){
        Intent intent=new Intent(context,TaoCanPayActivity.class);
        intent.putExtra("id",appBussinessId);
        intent.putExtra("total",totalFee);
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

        tv_total_fee.setText(totalFee+"");

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
    }

    @Override
    protected TaocanPayPresenter createPresenter() {
        return new TaocanPayPresenter();
    }

    @Override
    public void paySuccess() {
        finish();
    }

    @OnClick(R.id.tv_pay)
    public void goPay(){
        if(adapter.getCurrentPosition()==0){
            //余额支付
            presenter.payTaocan(appBussinessId,totalFee);
        }
    }
}
