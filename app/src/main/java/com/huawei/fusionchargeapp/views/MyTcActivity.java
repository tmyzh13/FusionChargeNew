package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.TaoCanAdapter;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;
import com.huawei.fusionchargeapp.presenter.MyTcPresenter;
import com.huawei.fusionchargeapp.views.interfaces.MyTcView;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by john on 2018/5/8.
 */

public class MyTcActivity extends BaseActivity<MyTcView,MyTcPresenter> implements MyTcView  {

    @Bind(R.id.nav)
    NavBar nav;
    @Bind(R.id.list_tc)
    ListView listView;

    private TaoCanAdapter adapter;
    private Context context=MyTcActivity.this;

    public static Intent getLauncher(Context context){
        Intent intent=new Intent(context,MyTcActivity.class);
        return intent;
    }


    @Override
    public void goLogin() {
        startActivity(LoginActivity.getLauncher(context));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tc;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setNavTitle(getString(R.string.tao_can));
        nav.setImageBackground(R.drawable.nan_bg);

        adapter=new TaoCanAdapter(context);
        listView.setAdapter(adapter);
//        List<TaocanBean> list =new ArrayList<>();
//        TaocanBean taocanBean =new TaocanBean();
//        list.add(taocanBean);
//        list.add(taocanBean);
//        adapter.replaceAll(list);

        presenter.getTaocan();
    }

    @Override
    protected MyTcPresenter createPresenter() {
        return new MyTcPresenter();
    }

    @Override
    public void renderTaocanDatas(List<TaocanBean> list) {
        adapter.replaceAll(list);
    }
}
