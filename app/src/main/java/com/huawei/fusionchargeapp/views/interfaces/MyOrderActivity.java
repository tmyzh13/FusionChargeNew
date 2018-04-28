package com.huawei.fusionchargeapp.views.interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.corelibs.utils.ToastMgr;
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
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.ChargeInputNumberActivity;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyOrderActivity extends BaseActivity {
    private final String RP = "10";

    private int page = 0;

    private ScanApi api;


    @Bind(R.id.lv_order)
    AutoLoadMoreListView lvOrder;
    @Bind(R.id.ptrLayout)
    PtrAutoLoadMoreLayout ptrLayout;
    private Context context = MyOrderActivity.this;

    @Bind(R.id.nav)
    NavBar navBar;


    private OrderListAdapter adapter;

    private List<RawRecordBean> recordBeanList;

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
        api = ApiFactory.getFactory().create(ScanApi.class);

        navBar.setColorRes(R.color.app_blue);
        navBar.setNavTitle(context.getString(R.string.home_my_order));

        adapter = new OrderListAdapter(context,recordBeanList);
        lvOrder.setAdapter(adapter);
        getData();

    }

    private void getData(){

        if(UserHelper.getSavedUser()==null|| Tools.isNull(UserHelper.getSavedUser().token)){
            startActivity(LoginActivity.getLauncher(this));
            return;
        }

        showLoading();

        RequestMyOrderBean bean = new RequestMyOrderBean();
        bean.setRp(RP);
        bean.setPage(page + "");
        RequestMyOrderChildBean bean1 = new RequestMyOrderChildBean();
        bean1.setAppUserId(UserHelper.getSavedUser().appUserId + "");
        bean.setCondition(bean1);

        api.getMyOrder(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<MyOrderData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<MyOrderData>() {
                    @Override
                    public void success(MyOrderData baseData) {
                        recordBeanList = baseData.rawRecords;
                        if(null == recordBeanList || recordBeanList.isEmpty()) {
                            showToast(getString(R.string.no_data));
                        } else {
                            adapter.setDatas(recordBeanList);
                            adapter.notifyDataSetChanged();
                        }
                        hideLoading();
                    }

                    @Override
                    public boolean operationError(MyOrderData baseData, int status, String message) {
                        hideLoading();
                        if(baseData.code == 403) {
                            goLogin();
                        }
                        showToast(getString(R.string.server_wrong));
                        return super.operationError(baseData, status, message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideLoading();
                        showToast(getString(R.string.time_out));
                    }
                });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
