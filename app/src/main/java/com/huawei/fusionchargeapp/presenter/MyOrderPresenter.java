package com.huawei.fusionchargeapp.presenter;

import android.text.TextUtils;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.pagination.presenter.PagePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.MyOrderData;
import com.huawei.fusionchargeapp.model.beans.RawRecordBean;
import com.huawei.fusionchargeapp.model.beans.RequestMyOrderBean;
import com.huawei.fusionchargeapp.model.beans.RequestMyOrderChildBean;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.interfaces.MyOrderView;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

/**
 * Created by zhangwei on 2018/5/2.
 */

public class MyOrderPresenter extends PagePresenter<MyOrderView> {

    private final String RP = "10";
    private int page = 1;

    private ScanApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(ScanApi.class);
    }

    @Override
    public void onStart() {

    }

    public void getData(final boolean isRefresh){
        page = isRefresh ? 1 : ++page;

        RequestMyOrderBean bean = new RequestMyOrderBean();
        bean.setRp(RP);
        bean.setPage(page + "");
        RequestMyOrderChildBean bean1 = new RequestMyOrderChildBean();
        bean1.setAppUserId(UserHelper.getSavedUser().appUserId + "");
        bean.setCondition(bean1);

        api.getMyOrder(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<MyOrderData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<MyOrderData>(view) {
                    @Override
                    public void success(MyOrderData baseData) {
                        List<RawRecordBean> newBeans = baseData.rawRecords;
                        if(null == newBeans || newBeans.isEmpty()) {
                            view.noData();
                            page = isRefresh ? 1 : page--;
                        } else {
                            if(isRefresh){
                                view.onRefreshSuccess(newBeans);
                            } else {
                                view.onLoadingSuccess(newBeans);
                            }
                        }

                    }

                    @Override
                    public boolean operationError(MyOrderData myOrderData, int status, String message) {
                        page = isRefresh ? 1 : page--;
                        return super.operationError(myOrderData, status, message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        page = isRefresh ? 1 : page--;
                    }
                });
    }
}
