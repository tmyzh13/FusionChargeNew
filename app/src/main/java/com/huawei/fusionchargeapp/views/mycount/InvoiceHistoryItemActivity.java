package com.huawei.fusionchargeapp.views.mycount;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/9.
 */

public class InvoiceHistoryItemActivity extends BaseActivity {


    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.sort_and_status)
    TextView sort_and_status;
    @Bind(R.id.receive_adress)
    TextView rec_adress;
    @Bind(R.id.invoice_create_time)
    TextView invoice_create_time;
    @Bind(R.id.invoice_head)
    TextView invoice_head;
    @Bind(R.id.invoice_content)
    TextView invoice_content;
    @Bind(R.id.tax_num)
    TextView tax_num;
    @Bind(R.id.money)
    TextView money;
    @Bind(R.id.invoice_time)
    TextView invoice_time;
    @Bind(R.id.consume_detail)
    TextView consume_detail;
    @Bind(R.id.consume_time)
    TextView consume_time;


    @Override
    public void goLogin() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invoice_history_item_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setColorRes(R.color.blue);
        bar.setNavTitle("纸质发票详情");
    }

    private void initAllTextView(Intent intent){

    }

    @OnClick(R.id.go_consume)
    void goConsumeDetail(){
        //进入消费详情
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
