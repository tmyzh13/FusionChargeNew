package com.huawei.fusionchargeapp.presenter;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.GuaildApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestCancelAppointment;
import com.huawei.fusionchargeapp.views.interfaces.GuaildView;

/**
 * Created by issuser on 2018/4/25.
 */

public class GuaildPresenter extends BasePresenter<GuaildView> {

    private GuaildApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(GuaildApi.class);
    }

    @Override
    public void onStart() {

    }

    public void cancelAppointment(String reserveId,String appUserId,String gunCode,String chargingPileId){
        RequestCancelAppointment bean =new RequestCancelAppointment();
        bean.reserveId=reserveId;
        bean.appUserId=appUserId;
        bean.gunCode=gunCode;
        bean.chargingPileId=chargingPileId;
        api.cancelAppointment(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData>(view) {
                    @Override
                    public void success(BaseData baseData) {
                        view.cancelAppointmentSuccess();
                    }
                });
    }
}
