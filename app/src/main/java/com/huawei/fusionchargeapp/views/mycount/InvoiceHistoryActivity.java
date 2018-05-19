package com.huawei.fusionchargeapp.views.mycount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.InvoiceHistoryAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.InvoiceConsumeBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryBean;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryItemBean;
import com.huawei.fusionchargeapp.presenter.InvoiceHistoryPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.interfaces.InvoiceHistoryView;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by john on 2018/5/7.
 */

public class InvoiceHistoryActivity extends BaseActivity<InvoiceHistoryView,InvoiceHistoryPresenter> implements InvoiceHistoryView {
    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.lv_order)
    AutoLoadMoreListView listView;
    @Bind(R.id.ptrLayout)
    PtrAutoLoadMoreLayout ptrLayout;
    @Bind(R.id.empty_view)
    TextView emptyView;
    private InvoiceHistoryAdapter adapter;
    private List<InvoiceHistoryBean> data = new ArrayList<>();
    private static final int FIRST_PAGE = 1;
    private int page = FIRST_PAGE;
    public static final String ORDER_ID = "order_id";
    public static final int DEFAULT_ID = 29;

    @Override
    public void goLogin() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.invoice_list_with_page;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setNavTitle(getString(R.string.invoice_histroy));
        bar.setColorRes(R.color.blue);


        adapter  = new InvoiceHistoryAdapter(this,data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //进入详细界面
                Intent intent = new Intent(InvoiceHistoryActivity.this,InvoiceHistoryItemActivity.class);
                intent.putExtra(ORDER_ID,adapter.getClickOrderId(i));
                startActivity(intent);
            }
        });
        ptrLayout.setRefreshLoadCallback(new PtrAutoLoadMoreLayout.RefreshLoadCallback() {
            @Override
            public void onLoading(PtrFrameLayout frame) {
                ptrLayout.enableLoading();
                page ++;
                presenter.getInvoiceConsume(page);
            }

            @Override
            public void onRefreshing(PtrFrameLayout frame) {
                page = FIRST_PAGE;
                ptrLayout.enableLoading();
                if (!frame.isAutoRefresh()) {
                    presenter.getInvoiceConsume(page);
                }
            }
        });

        if(UserHelper.getSavedUser()==null|| Tools.isNull(UserHelper.getSavedUser().token)){
            startActivity(LoginActivity.getLauncher(this));
            return;
        }
        showLoading();
        presenter.getInvoiceConsume(page);
    }

    @Override
    public void onLoadingCompleted() {
        hideLoading();
    }

    @Override
    public void onAllPageLoaded() {
        ptrLayout.disableLoading();
    }

    @Override
    public void showLoading() {
        ptrLayout.setRefreshing();
    }

    @Override
    public void hideLoading() {
        ptrLayout.complete();
    }

    @Override
    public void getInvoiceHistory(List<InvoiceHistoryBean> bean) {
        if (page == FIRST_PAGE) {
            data = bean;
            if (data == null || data.size() == 0) {
                ptrLayout.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                ptrLayout.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        } else {
            for (int i=0;i<bean.size();i++){
                data.add(bean.get(i));
            }
        }
        adapter.setData(data);
    }

    @Override
    public void getInvoiceConsumeFailed() {
        if (page > FIRST_PAGE) {
            page --;
        }
    }

    @Override
    public void getInvoiceHistoryItem(InvoiceHistoryItemBean bean) {
    }

    @Override
    protected InvoiceHistoryPresenter createPresenter() {
        return new InvoiceHistoryPresenter();
    }

    @Override
    public void getInvoiceHistoryConsume(List<InvoiceConsumeBean> bean) {
    }
}
