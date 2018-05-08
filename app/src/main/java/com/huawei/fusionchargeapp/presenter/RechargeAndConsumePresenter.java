package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.pagination.presenter.PagePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.RechargeAndConsumeApi;
import com.huawei.fusionchargeapp.model.beans.BalanceResultBean;
import com.huawei.fusionchargeapp.model.beans.RechargeAndConsumeRequestBean;
import com.huawei.fusionchargeapp.views.interfaces.RechargeAndConsumeView;
import com.trello.rxlifecycle.ActivityEvent;

/**
 * Created by admin on 2018/5/8.
 */

public class RechargeAndConsumePresenter extends PagePresenter<RechargeAndConsumeView> {

    RechargeAndConsumeApi api;
    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(RechargeAndConsumeApi.class);
    }
    @Override
    public void onStart() {

    }

    public void getBalanceDetail(RechargeAndConsumeRequestBean bean){
        api.getAppointmentInfo(UserHelper.getSavedUser().token, bean)
                .compose(new ResponseTransformer<>(this.<BalanceResultBean>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BalanceResultBean>(view) {
                    @Override
                    public void success(BalanceResultBean baseData) {
                        view.getBalanceDetail(baseData.rawRecords);
                    }
                    @Override
                    public void onError(Throwable e) {
                        view.getBalanceDetailFail();
                        super.onError(e);
                    }
                });

    }
}
