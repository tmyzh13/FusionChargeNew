package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.TaocanPayapi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestPayTaocanBean;
import com.huawei.fusionchargeapp.views.interfaces.TaoCanPayView;
import com.huawei.hae.mcloud.bundle.base.login.model.User;
import com.trello.rxlifecycle.ActivityEvent;

/**
 * Created by issuser on 2018/5/17.
 */

public class TaocanPayPresenter extends BasePresenter<TaoCanPayView> {

    private TaocanPayapi api;

    @Override
    public void onStart() {

    }

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(TaocanPayapi.class);
    }

    public void payTaocan(int type, int businessPackageId,double totalFee){
        RequestPayTaocanBean bean =new RequestPayTaocanBean();
        bean.packageId=businessPackageId;
        bean.totalFee=totalFee;
//        bean.appUserId= UserHelper.getSavedUser().appUserId;
        bean.payType = type;
        api.payTaocan(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData>(view) {
                    @Override
                    public void success(BaseData baseData) {
                        view.paySuccess();
                    }
                });
    }
}
