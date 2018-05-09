package com.huawei.fusionchargeapp.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreExpandableListView;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.RechargeAndConsumeDetailAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.RechargeAndConsumeBean;
import com.huawei.fusionchargeapp.model.beans.RechargeAndConsumeRequestBean;
import com.huawei.fusionchargeapp.presenter.RechargeAndConsumePresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.RechargeAndConsumeView;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by admin on 2018/5/7.
 */

public class RechargeAndConsumeDetailActivity extends BaseActivity<RechargeAndConsumeView,RechargeAndConsumePresenter> implements RechargeAndConsumeView{

    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.list_view)
    AutoLoadMoreExpandableListView lvOrder;
    @Bind(R.id.ptrLayout)
    PtrAutoLoadMoreLayout ptrLayout;

    private List<String> groupList = new ArrayList<>();
    private List<List<RechargeAndConsumeBean>> itemList = new ArrayList<>();
    private RechargeAndConsumeDetailAdapter adpter;
    private RechargeAndConsumeRequestBean requestBean = new RechargeAndConsumeRequestBean();

    private static final int PAGE_FIRST_NUM = 1;
    private int page = PAGE_FIRST_NUM;
    private static final int PAGE_LIMIT_NUM = 10;
    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_and_consume_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setNavTitle(getString(R.string.my_acount_detail));
        bar.setColorRes(R.color.blue);
        requestBean.rp = PAGE_LIMIT_NUM;
        requestBean.page = PAGE_FIRST_NUM;

        adpter = new RechargeAndConsumeDetailAdapter(this,groupList,itemList);
        lvOrder.setAdapter(adpter);
        //reset listView show
        setExpandableListViewShowProperty();
        ptrLayout.setRefreshLoadCallback(new PtrAutoLoadMoreLayout.RefreshLoadCallback() {
            @Override
            public void onLoading(PtrFrameLayout frame) {
                ptrLayout.enableLoading();
                page ++;
                requestBean.page = page;
                presenter.getBalanceDetail(requestBean);
            }

            @Override
            public void onRefreshing(PtrFrameLayout frame) {
                page = PAGE_FIRST_NUM;
                groupList.clear();
                itemList.clear();
                requestBean.page = page;

                ptrLayout.enableLoading();
                if (!frame.isAutoRefresh()) {
                    presenter.getBalanceDetail(requestBean);
                }
            }
        });

        if(UserHelper.getSavedUser()==null|| Tools.isNull(UserHelper.getSavedUser().token)){
            startActivity(LoginActivity.getLauncher(this));
            return;
        }
        showLoading();
        presenter.getBalanceDetail(requestBean);

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
    public void onLoadingCompleted() {
        hideLoading();
    }

    @Override
    public void onAllPageLoaded() {
        ptrLayout.disableLoading();
    }

    @Override
    public void getBalanceDetailFail() {
        if (page > PAGE_FIRST_NUM) {
            page--;
        }
    }

    @Override
    public void getBalanceDetail(List<RechargeAndConsumeBean> list) {
        //如果list存在数据才继续处理
        if (!noData(list)) {
            setAdapterData(list);
        }
    }

    //如何处理新增加的数据
    private void setAdapterData(List<RechargeAndConsumeBean> list){
        /*数据处理逻辑，数据分组以时间来分，即时间的年和月来分
        如果有不同的年和月，就加入组，相应子数据开始增加；
         */
        Log.e("liutao",list.size()+"");
        for (int i =0; i < list.size(); i++) {
            RechargeAndConsumeBean tempBean = list.get(i);
            if (groupList.size() == 0) {
                groupList.add(getGroupStringFromeTime(tempBean.dealTime));
                itemList.add(new ArrayList<RechargeAndConsumeBean>());
                itemList.get(itemList.size()-1).add(tempBean);
            }
            if (groupList.size() != 0) {
                if (groupList.contains(getGroupStringFromeTime(tempBean.dealTime))) {
                    itemList.get(itemList.size()-1).add(tempBean);
                } else {
                    groupList.add(getGroupStringFromeTime(tempBean.dealTime));
                    itemList.add(new ArrayList<RechargeAndConsumeBean>());
                    itemList.get(itemList.size()-1).add(tempBean);
                }
            }
        }
        adpter.setDatas(groupList,itemList);
        setExpandableListViewShowProperty();
    }

    private String getGroupStringFromeTime(String date) {
        return (date.substring(0,4) + "年" +date.substring(5,7) + "月");
    }

    @Override
    protected RechargeAndConsumePresenter createPresenter() {
        return new RechargeAndConsumePresenter();
    }

    public boolean noData(List<RechargeAndConsumeBean> bean) {
        if (null == bean || bean.isEmpty()){
            showToast(page == PAGE_FIRST_NUM ? "没有账单记录": "没有更多账单记录");
            ptrLayout.disableLoading();
            return true;
        }
        return false;
    }

    @Override
    public void showLoading() {
//        super.showLoading();
        ptrLayout.setRefreshing();
    }

    @Override
    public void hideLoading() {
        ptrLayout.complete();
    }
}
