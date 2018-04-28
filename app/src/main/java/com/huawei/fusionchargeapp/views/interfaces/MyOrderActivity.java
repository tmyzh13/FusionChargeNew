package com.huawei.fusionchargeapp.views.interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.OrderListAdapter;
import com.huawei.fusionchargeapp.model.beans.OrderBean;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.List;

import butterknife.Bind;

public class MyOrderActivity extends BaseActivity {
    private Context context = MyOrderActivity.this;

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.order_lv)
    ListView order_lv;

    private OrderListAdapter adapter;
    private List<OrderBean> list;

    @Override
    public void goLogin() {

    }

    public static Intent getLauncher(Context context){
        Intent intent = new Intent(context,MyOrderActivity.class);
        return intent;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setColorRes(R.color.app_blue);
        navBar.setNavTitle(context.getString(R.string.home_my_order));

//        adapter = new OrderListAdapter(context,list);
//        order_lv.setAdapter(adapter);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
