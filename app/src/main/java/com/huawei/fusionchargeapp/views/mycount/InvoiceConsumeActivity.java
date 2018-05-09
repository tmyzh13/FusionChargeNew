package com.huawei.fusionchargeapp.views.mycount;

import android.os.Bundle;
import android.widget.ListView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.InvoiceConsumeAdapter;
import com.huawei.fusionchargeapp.model.beans.ElectronicConsumeBean;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by admin on 2018/5/9.
 */

public class InvoiceConsumeActivity extends BaseActivity{
    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.lv_order)
    ListView listView;

    private InvoiceConsumeAdapter adapter;
    private List<ElectronicConsumeBean> data = new ArrayList<>();

    @Override
    public void goLogin() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.invoice_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setNavTitle(getString(R.string.invoice_detail));
        bar.setColorRes(R.color.blue);
        initFakeData();

        adapter  = new InvoiceConsumeAdapter(this,data);
        listView.setAdapter(adapter);
    }
    private void initFakeData(){
        ElectronicConsumeBean bean = new ElectronicConsumeBean();
        bean.money = "48.5元";
        bean.type = "充电消费";
        bean.adress = "武汉高新大道户口社区户口18栋";
        bean.time = "2016-12-23 19:00:34";
        data.add(bean);
        data.add(bean);
        data.add(bean);
        data.add(bean);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
