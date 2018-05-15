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
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreExpandableListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.ApplyInvoiceAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.ApplyInvoiceBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.InvoicePresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.interfaces.InvoiceView;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by john on 2018/5/9.
 */

public class ApplyOrderListActivity extends BaseActivity<InvoiceView,InvoicePresenter> implements InvoiceView, ApplyInvoiceAdapter.OnItemClickListener{

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
    private static final int FIRST_PAGE = 1;
    private int page = FIRST_PAGE;

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
                groupList.clear();
                itemList.clear();

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
    public void getInvoiceConsume(List<ApplyInvoiceBean> bean) {
        if (bean == null || bean.size() <= 0){
            ptrLayout.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
            bootom.setVisibility(View.GONE);
        } else {
            ptrLayout.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
            bootom.setVisibility(View.VISIBLE);
            setAdapterData(bean);
        }
    }

    @Override
    public void getInvoiceConsumeFailed() {
        if (page > FIRST_PAGE) {
            page --;
        }
    }

    @OnClick(R.id.next)
    void geNext(){
        if (nowNum == 0) {
            showToast(getString(R.string.select_no_consume_item));
            return;
        }
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
                groupList.add(getGroupStringFromeTime(tempBean.chargeStartTime));
                itemList.add(new ArrayList<ApplyInvoiceBean>());
                itemList.get(itemList.size()-1).add(tempBean);
                totalNum++;
                totalMoney += tempBean.consumeTotalMoney;
            } else {
                if (groupList.contains(getGroupStringFromeTime(tempBean.chargeStartTime))) {
                    itemList.get(itemList.size()-1).add(tempBean);
                    totalNum++;
                    totalMoney += tempBean.consumeTotalMoney;
                } else {
                    groupList.add(getGroupStringFromeTime(tempBean.chargeStartTime));
                    itemList.add(new ArrayList<ApplyInvoiceBean>());
                    itemList.get(itemList.size()-1).add(tempBean);
                    totalNum++;
                    totalMoney += tempBean.consumeTotalMoney;
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
    public void afterClick(int indexOfGroup, int indexOfItem,boolean status) {
        //当点击item时，记录个数和金额进行相应的变化
        if (!status) {
            if (nowNum <= 0){
                return;
            }
            nowNum --;
            nowMoney -= itemList.get(indexOfGroup).get(indexOfItem).consumeTotalMoney;
        } else {
            if (nowNum >= totalNum){
                return;
            }
            nowNum ++;
            nowMoney += itemList.get(indexOfGroup).get(indexOfItem).consumeTotalMoney;
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

    @Override
    public void onLoadingCompleted() {
        hideLoading();
    }

    @Override
    public void onAllPageLoaded() {
        ptrLayout.disableLoading();
    }

    @Override
    protected InvoicePresenter createPresenter() {
        return new InvoicePresenter();
    }
    @Override
    public void showLoading() {
        ptrLayout.setRefreshing();
    }

    @Override
    public void hideLoading() {
        ptrLayout.complete();
    }
}
