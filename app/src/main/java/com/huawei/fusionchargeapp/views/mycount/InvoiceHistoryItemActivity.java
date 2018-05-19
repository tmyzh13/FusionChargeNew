package com.huawei.fusionchargeapp.views.mycount;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryItemBean;
import com.huawei.fusionchargeapp.presenter.InvoiceHistoryPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.InvoiceHistoryView;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/9.
 */

public class InvoiceHistoryItemActivity extends BaseActivity<InvoiceHistoryView,InvoiceHistoryPresenter> implements InvoiceHistoryView{


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

    private int id;
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
        bar.setNavTitle(getString(R.string.invoice_detail));

        id = getIntent().getIntExtra(InvoiceHistoryActivity.ORDER_ID,0);
        presenter.getInvoiceHistoryItem(id);
        presenter.getInvoiceHistoryConsume(id,1);
    }

    private void initAllTextView(InvoiceHistoryItemBean bean){
        consume_detail.setText(getString(R.string.invoice_and_consume_num,1,bean.count));
        invoice_create_time.setText(bean.detail.createTime.substring(0, Tools.DATE_LENGTH_FROM_SERVER));
        rec_adress.setText(bean.detail.recAddr);
        invoice_head.setText(bean.detail.title);
        tax_num.setText(bean.detail.code);
        invoice_content.setText(bean.detail.content);
        money.setText(bean.detail.amount+"");
        invoice_time.setText(bean.detail.createTime.substring(0, Tools.DATE_LENGTH_FROM_SERVER));
        consume_time.setText(bean.startEndTime);

    }

    @OnClick(R.id.go_consume)
    void goConsumeDetail(){
        //进入消费详情
        Intent intent = new Intent(this,InvoiceConsumeActivity.class);
        intent.putExtra(InvoiceHistoryActivity.ORDER_ID,id);
        startActivity(intent);
    }


    @Override
    public void onLoadingCompleted() {
    }

    @Override
    public void onAllPageLoaded() {
    }

    @Override
    public void getInvoiceHistory(List<InvoiceHistoryBean> bean) {
    }

    @Override
    public void getInvoiceConsumeFailed() {
    }

    @Override
    public void getInvoiceHistoryItem(InvoiceHistoryItemBean bean) {
        initAllTextView(bean);
    }

    @Override
    protected InvoiceHistoryPresenter createPresenter() {
        return new InvoiceHistoryPresenter();
    }

    @Override
    public void getInvoiceHistoryConsume(List<InvoiceConsumeBean> bean) {
    }
}
