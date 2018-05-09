package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.views.NoScrollingListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapters.InvoicePayAdapter;
import com.huawei.fusionchargeapp.model.beans.PayStyleBean;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by john on 2018/5/7.
 */

public class ApplyInvoiceActivity extends BaseActivity {

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.list)
    NoScrollingListView listView;

    private Context context=ApplyInvoiceActivity.this;
    private InvoicePayAdapter adapter;

    public static Intent getLauncher(Context context){
        Intent intent =new Intent(context,ApplyInvoiceActivity.class);
        return intent;
    }

    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_invoice;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getString(R.string.pay));
        navBar.setImageBackground(R.drawable.nan_bg);

        adapter=new InvoicePayAdapter(context);
        List<PayStyleBean> list =new ArrayList<>();
        PayStyleBean bean =new PayStyleBean();
        bean.imgRes=R.mipmap.account_03;
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
        list.add(bean1);
        list.add(bean2);
        list.add(bean);

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
    protected BasePresenter createPresenter() {
        return null;
    }
}
