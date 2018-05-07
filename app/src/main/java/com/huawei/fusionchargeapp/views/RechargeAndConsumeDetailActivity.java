package com.huawei.fusionchargeapp.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.RechargeAndConsumeDetailAdapter;
import com.huawei.fusionchargeapp.model.beans.RechargeAndConsumeBean;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by admin on 2018/5/7.
 */

public class RechargeAndConsumeDetailActivity extends BaseActivity{

    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.list_view)
    ExpandableListView listView;

    private List<String> groupList = new ArrayList<>();
    private List<List<RechargeAndConsumeBean>> itemList = new ArrayList<>();
    private RechargeAndConsumeDetailAdapter adpter;

    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_and_consume_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setNavTitle("明细");
        bar.setColorRes(R.color.blue);

        //fake data
        initFakeData();

        adpter = new RechargeAndConsumeDetailAdapter(this,groupList,itemList);
        listView.setAdapter(adpter);
        //reset listView show
        setExpandableListViewShowProperty();


    }

    private void setExpandableListViewShowProperty(){
        listView.setGroupIndicator(null);
        for (int i= 0; i<groupList.size();i++){
            listView.expandGroup(i);
        }
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    private void initFakeData(){
        groupList.add("本月");
        groupList.add("4月");

        RechargeAndConsumeBean bean = new RechargeAndConsumeBean();
        bean.rechargeType = 0;
        bean.money= "+33";
        bean.moneySubscription="充值33";
        bean.time="15:00";
        bean.weekDay="周二";
        List<RechargeAndConsumeBean> list = new ArrayList<>();
        list.add(bean);

        RechargeAndConsumeBean bean1 = new RechargeAndConsumeBean();
        bean1.rechargeType = 1;
        bean1.money= "+33";
        bean1.moneySubscription="充值33";
        bean1.time="15:00";
        bean1.weekDay="周二";
        list.add(bean1);

        RechargeAndConsumeBean bean2 = new RechargeAndConsumeBean();
        bean2.rechargeType = 2;
        bean2.money= "+33";
        bean2.moneySubscription="充值33";
        bean2.time="15:00";
        bean2.weekDay="周二";
        list.add(bean2);

        itemList.add(list);

        List<RechargeAndConsumeBean> list1 = new ArrayList<>();
        list1.add(bean);
        itemList.add(list1);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
