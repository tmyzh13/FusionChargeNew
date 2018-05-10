package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreExpandableListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.ApplyInvoiceAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.ApplyInvoiceBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by john on 2018/5/9.
 */

public class ApplyOrderListActivity extends BaseActivity implements ApplyInvoiceAdapter.OnItemClickListener{

    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.list_view)
    AutoLoadMoreExpandableListView lvOrder;
    @Bind(R.id.ptrLayout)
    PtrAutoLoadMoreLayout ptrLayout;
    @Bind(R.id.empty_view)
    TextView empty_view;
    @Bind(R.id.select_all)
    CheckBox select_all;
    @Bind(R.id.next)
    TextView next;
    @Bind(R.id.bottom_tool)
    LinearLayout bootom;
    @Bind(R.id.apply_total)
    TextView apply_total;

    private int orderNum = 0;
    private int totalNum = 0;
    private double totalMoney = 0;
    private int nowNum;
    private double nowMoney;
    private String money = "";
    private List<String> groupList = new ArrayList<>();
    private List<List<ApplyInvoiceBean>> itemList = new ArrayList<>();
    private ApplyInvoiceAdapter adpter;
    private CompoundButton.OnCheckedChangeListener allSelectListener;

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(this));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_order_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setColorRes(R.color.blue);
        bar.setNavTitle(getString(R.string.apply_invoice));
        apply_total.setText(getString(R.string.invoice_apply_total,0,0.0));

        adpter = new ApplyInvoiceAdapter(this,groupList,itemList,this);
        lvOrder.setAdapter(adpter);
        setExpandableListViewShowProperty();
        allSelectListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    nowNum = totalNum;
                    nowMoney = totalMoney;
                } else {
                    nowNum = 0;
                    nowMoney = 0;
                }
                apply_total.setText(getString(R.string.invoice_apply_total,nowNum,nowMoney));
                adpter.setAllCheckBoxChecked(b);
            }
        };

        select_all.setOnCheckedChangeListener(allSelectListener);
        ptrLayout.disableLoading();
        ptrLayout.setCanRefresh(false);

        initFakeData();
    }

    private void initFakeData(){
        itemList = new ArrayList<>();
        groupList = new ArrayList<>();
        ApplyInvoiceBean bean = new ApplyInvoiceBean();
        bean.chargeTime = "1989-03-10 19:23:23";
        bean.money="88元";
        bean.pileAdress= "南京市天下桩无底洞223号气度小区";
        bean.pileNum = 234546567L;
        List<ApplyInvoiceBean> list = new ArrayList<>();
        list.add(bean);
        ApplyInvoiceBean bean1 = new ApplyInvoiceBean();
        bean1.chargeTime = "1999-03-10 19:23:23";
        bean1.money="88元";
        bean1.pileAdress= "南京市天下桩无底洞223号气度小区";
        bean1.pileNum = 234546567L;
        list.add(bean1);
        setAdapterData(list);
    }

    @OnClick(R.id.next)
    void geNext(){
        startActivity(ApplyInvoiceActivity.getLauncher(this));
    }

    private void setAdapterData(List<ApplyInvoiceBean> list){
        /*数据处理逻辑，数据分组以时间来分，即时间的年和月来分
        如果有不同的年和月，就加入组，相应子数据开始增加；
         */
        Log.e("liutao",list.size()+"");
        for (int i =0; i < list.size(); i++) {
            ApplyInvoiceBean tempBean = list.get(i);
            if (groupList.size() == 0) {
                groupList.add(getGroupStringFromeTime(tempBean.chargeTime));
                itemList.add(new ArrayList<ApplyInvoiceBean>());
                itemList.get(itemList.size()-1).add(tempBean);
                totalNum++;
                totalMoney += Double.parseDouble(tempBean.money.substring(0,tempBean.money.length()-1));
            } else {
                if (groupList.contains(getGroupStringFromeTime(tempBean.chargeTime))) {
                    itemList.get(itemList.size()-1).add(tempBean);
                    totalNum++;
                    totalMoney += Double.parseDouble(tempBean.money.substring(0,tempBean.money.length()-1));
                } else {
                    groupList.add(getGroupStringFromeTime(tempBean.chargeTime));
                    itemList.add(new ArrayList<ApplyInvoiceBean>());
                    itemList.get(itemList.size()-1).add(tempBean);
                    totalNum++;
                    totalMoney += Double.parseDouble(tempBean.money.substring(0,tempBean.money.length()-1));
                }
            }
        }
        adpter.setDatas(groupList,itemList);
        setExpandableListViewShowProperty();
    }
    private String getGroupStringFromeTime(String date) {
        return (date.substring(0,4) + "年" +date.substring(5,7) + "月");
    }

    private void setExpandableListViewShowProperty(){
        lvOrder.setGroupIndicator(null);
        for (int i= 0; i<groupList.size();i++){
            lvOrder.expandGroup(i);
        }
        lvOrder.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void afterClick(int indexOfGroup, int indexOfItem,boolean status) {
        //当点击item时，记录个数和金额进行相应的变化
        if (!status) {
            if (nowNum <= 0){
                return;
            }
            nowNum --;
            String money = itemList.get(indexOfGroup).get(indexOfItem).money;
            nowMoney -= Double.parseDouble(money.substring(0,money.length()-1));
        } else {
            if (nowNum >= totalNum){
                return;
            }
            nowNum ++;
            String money = itemList.get(indexOfGroup).get(indexOfItem).money;
            nowMoney += Double.parseDouble(money.substring(0,money.length()-1));
        }
        boolean isChange = false;
        if (nowNum == totalNum) {
            select_all.setOnCheckedChangeListener(null);
            select_all.setChecked(true);
            isChange = true;
        } else if (select_all.isChecked()){
            select_all.setOnCheckedChangeListener(null);
            select_all.setChecked(false);
            isChange = true;
        }
        if (isChange){
            select_all.setOnCheckedChangeListener(allSelectListener);
        }
        apply_total.setText(getString(R.string.invoice_apply_total,nowNum,nowMoney));
    }
}
