package com.huawei.fusionchargeapp.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.google.gson.Gson;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestScanBean;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.views.ChargeCaptureActivity;
import com.huawei.fusionchargeapp.views.ChargeOrderDetailsActivity;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.interfaces.ChargeCaptureView;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.trello.rxlifecycle.ActivityEvent;

/**
 * Created by zhangwei on 2018/4/30.
 */

public class ChargeCapturePresenter extends BasePresenter<ChargeCaptureView>{

    private ScanApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(ScanApi.class);
    }

    @Override
    public void onStart() {

    }

    public void getData(String contents){
        RequestScanBean bean = new RequestScanBean();
        bean.setAppUserId(UserHelper.getSavedUser().appUserId + "");
        Log.e("zw","UserHelper.getSavedUser().userId : " + UserHelper.getSavedUser().appUserId);
//        bean.setQrCode("1300000001");
        bean.setQrCode(contents);
        api.getScanChargeInfo(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<ScanChargeInfo>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<ScanChargeInfo>>(view) {
                               @Override
                               public void success(BaseData<ScanChargeInfo> baseData) {
                                   view.onSuccess(baseData.data);
                               }

                               @Override
                               public boolean operationError(BaseData<ScanChargeInfo> scanChargeInfoBaseData, int status, String message) {
                                   if(!TextUtils.isEmpty(scanChargeInfoBaseData.msg)) {
                                       view.onOperationError(scanChargeInfoBaseData.msg);
                                       return true;
                                   }
                                   return super.operationError(scanChargeInfoBaseData, status, message);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   super.onError(e);
                                  view.onError();
                               }
                           }
                );
    }
}
