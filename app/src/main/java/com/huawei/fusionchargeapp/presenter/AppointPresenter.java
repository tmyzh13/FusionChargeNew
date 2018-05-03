package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.AppointApi;
import com.huawei.fusionchargeapp.model.beans.AppointRequestBean;
import com.huawei.fusionchargeapp.model.beans.AppointResponseBean;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.views.interfaces.AppointView;

/**
 * Created by issuser on 2018/4/23.
 */

public class AppointPresenter extends BasePresenter<AppointView> {

    AppointApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(AppointApi.class);
    }

    @Override
    public void onStart() {
    }

    public void appointAocation(String gunCode,int chargingPileId, String chargingPileName,String nowTime,String endTime,int appointTime) {
        final AppointRequestBean bean = new AppointRequestBean();
        bean.setAppUserId(UserHelper.getSavedUser().appUserId + "");
//        bean.setAppUserId(71 + "");
        bean.setChargingPileId(chargingPileId + "");
        bean.setChargingPileName(chargingPileName);
//        bean.setReserveBeginTime(nowTime);
//        bean.setReserveEndTime(endTime);
        bean.setReserveDuration(appointTime + "");
        bean.setGunCode(gunCode);

        api.appoint(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<BaseData<AppointResponseBean>>(this.<BaseData<AppointResponseBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<AppointResponseBean>>(view) {
                    @Override
                    public void success(BaseData<AppointResponseBean> baseData) {
                        view.appointSuccess(baseData.data);
                    }

                    @Override
                    public boolean operationError(BaseData<AppointResponseBean> appointResponseBeanBaseData, int status, String message) {
                       //恶意预约过多
                        if(appointResponseBeanBaseData.code == 870) {
                            view.appointEvil(appointResponseBeanBaseData.data);
                        }
                        return super.operationError(appointResponseBeanBaseData, status, message);
                    }
                });
    }
}
