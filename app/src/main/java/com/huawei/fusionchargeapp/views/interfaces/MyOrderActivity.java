package com.huawei.fusionchargeapp.views.interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.corelibs.utils.ToastMgr;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.OrderListAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.MyOrderData;
import com.huawei.fusionchargeapp.model.beans.OrderBean;
import com.huawei.fusionchargeapp.model.beans.RawRecordBean;
import com.huawei.fusionchargeapp.model.beans.RequestMyOrderBean;
import com.huawei.fusionchargeapp.model.beans.RequestMyOrderChildBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.MyOrderPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.ChargeInputNumberActivity;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyOrderActivity extends BaseActivity<MyOrderView,MyOrderPresenter> implements MyOrderView {


   // private int page = 0;

    @Bind(R.id.lv_order)
    AutoLoadMoreListView lvOrder;
    @Bind(R.id.ptrLayout)
    PtrAutoLoadMoreLayout ptrLayout;
    private Context context = MyOrderActivity.this;

    @Bind(R.id.nav)
    NavBar navBar;


    private OrderListAdapter adapter;

    private List<RawRecordBean> recordBeanList;
    private boolean isRefresh = true;

    @Override
    public void goLogin() {
        ToastMgr.show(getString(R.string.login_fail));
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(MyOrderActivity.this));
    }

    public static Intent getLauncher(Context context) {
        Intent intent = new Intent(context, MyOrderActivity.class);
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

        adapter = new OrderListAdapter(context,recordBeanList);
        lvOrder.setAdapter(adapter);

//        ptrLayout.disableLoading();
//        ptrLayout.setCanRefresh(false);
        ptrLayout.setRefreshLoadCallback(new PtrAutoLoadMoreLayout.RefreshLoadCallback() {
            @Override
            public void onLoading(PtrFrameLayout frame) {
               // page++;
                isRefresh = false;
                presenter.getData(isRefresh);
            }

            //下拉
            @Override
            public void onRefreshing(PtrFrameLayout frame) {


                //page = 0;
                isRefresh = true;
                ptrLayout.enableLoading();
                if (!frame.isAutoRefresh()) {
                    presenter.getData(isRefresh);
                }
            }
        });

        showLoading();

        if(UserHelper.getSavedUser()==null|| Tools.isNull(UserHelper.getSavedUser().token)){
            startActivity(LoginActivity.getLauncher(this));
            return;
        }

        presenter.getData(isRefresh);

    }



    @Override
    protected MyOrderPresenter createPresenter() {
        return new MyOrderPresenter();
    }


    @Override
    public void onLoadingSuccess(List<RawRecordBean> newBeans) {
        recordBeanList.addAll(newBeans);
        adapter.setDatas(recordBeanList);
        adapter.notifyDataSetChanged();
        hideLoading();
    }

    @Override
    public void onLoadingFail(String msg) {
        if(TextUtils.isEmpty(msg)){
            showToast(getString(R.string.server_wrong));
        } else {
            showToast(msg);
        }
        hideLoading();
    }

    @Override
    public void onRefreshSuccess(List<RawRecordBean> newBeans) {
        recordBeanList = newBeans;
        adapter.setDatas(newBeans);
        adapter.notifyDataSetChanged();
        hideLoading();
    }

    @Override
    public void onRefreshFail(String msg) {

    }

    @Override
    public void noData() {
        showToast(getString(R.string.no_order_data));
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

    @Override
    public void onLoadingCompleted() {
            hideLoading();
    }

    @Override
    public void onAllPageLoaded() {
        ptrLayout.disableLoading();
    }
}
