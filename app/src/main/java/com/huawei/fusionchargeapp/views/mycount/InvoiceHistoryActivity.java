package com.huawei.fusionchargeapp.views.mycount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.InvoiceHistoryAdapter;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryBean;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by john on 2018/5/7.
 */

public class InvoiceHistoryActivity extends BaseActivity {
    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.lv_order)
    AutoLoadMoreListView listView;
    @Bind(R.id.ptrLayout)
    PtrAutoLoadMoreLayout ptyLayout;

    private InvoiceHistoryAdapter adapter;
    private List<InvoiceHistoryBean> data = new ArrayList<>();

    @Override
    public void goLogin() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.invoice_list_with_page;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setNavTitle("开票历史");
        bar.setColorRes(R.color.blue);

        //假数据
        initFakeData();

        adapter  = new InvoiceHistoryAdapter(this,data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //进入详细界面
                Intent intent = new Intent(InvoiceHistoryActivity.this,InvoiceHistoryItemActivity.class);
                startActivity(intent);
            }
        });
        ptyLayout.setRefreshLoadCallback(new PtrAutoLoadMoreLayout.RefreshLoadCallback() {
            @Override
            public void onLoading(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshing(PtrFrameLayout frame) {

            }
        });
        ptyLayout.setCanRefresh(false);
    }

    private void initFakeData(){
        InvoiceHistoryBean bean = new InvoiceHistoryBean();
        bean.money = "48.5元";
        bean.sort = "纸质发票 充电发票";
        bean.status = "已开票";
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
