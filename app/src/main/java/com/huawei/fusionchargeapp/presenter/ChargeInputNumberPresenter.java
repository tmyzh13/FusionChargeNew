package com.huawei.fusionchargeapp.presenter;

import android.content.Intent;
import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.google.gson.Gson;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.MyOrderData;
import com.huawei.fusionchargeapp.model.beans.RequestMyOrderBean;
import com.huawei.fusionchargeapp.model.beans.RequestMyOrderChildBean;
import com.huawei.fusionchargeapp.model.beans.RequestScanBean;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;
import com.huawei.fusionchargeapp.views.ChargeInputNumberActivity;
import com.huawei.fusionchargeapp.views.ChargeOrderDetailsActivity;
import com.huawei.fusionchargeapp.views.interfaces.ChargeInputNumberView;
import com.trello.rxlifecycle.ActivityEvent;

/**
 * Created by Administrator on 2018/5/9.
 */

public class ChargeInputNumberPresenter extends BasePresenter<ChargeInputNumberView> {

    private ScanApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();

        api = ApiFactory.getFactory().create(ScanApi.class);
    }

    @Override
    public void onStart() {

    }


    public void getData(String number){

        RequestScanBean bean = new RequestScanBean();
        bean.setAppUserId(UserHelper.getSavedUser().appUserId + "");
        bean.setQrCode(number);

        //正确
        api.getScanChargeInfo(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<ScanChargeInfo>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<ScanChargeInfo>>(view) {
                               @Override
                               public void success(BaseData<ScanChargeInfo> baseData) {
                                    view.onGetDataSuccess(baseData.data);
                               }

                               @Override
                               public boolean operationError(BaseData<ScanChargeInfo> scanChargeInfoBaseData, int status, String message) {
                                   view.onGetDataFail(scanChargeInfoBaseData.msg);
                                   return super.operationError(scanChargeInfoBaseData,status,message);

                               }

                               @Override
                               public void onError(Throwable e) {
                                   super.onError(e);
                                   view.onGetDataFail(getString(R.string.time_out_or_qrcode_wrong));
                               }
                           }
                );


     /*   LoginApi api1 = ApiFactory.getFactory().create(LoginApi.class);
        LoginRequestBean bean1 = new LoginRequestBean();
        bean1.phone = "17366206080";
//        bean.carrier = Tools.getPhoneType();
        bean1.carrier = "iphone";
        bean1.type = 0;
        if (0 == 0) {//密码登录
            bean1.passWord = "123";
        }
        api1.login(bean1)
                .compose(new ResponseTransformer<>(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData>() {
                    @Override
                    public void success(BaseData baseData) {
                        Log.e("zw",baseData.toString());

                    }
                });*/

    }

    private void testt(){

        RequestMyOrderBean bean = new RequestMyOrderBean();
        bean.setRp("10");
        bean.setPage("1");
        RequestMyOrderChildBean bean1 = new RequestMyOrderChildBean();
        bean1.setAppUserId(UserHelper.getSavedUser().appUserId + "");
        bean.setCondition(bean1);

        api.getMyOrder(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<MyOrderData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<MyOrderData>() {
                    @Override
                    public void success(MyOrderData baseData) {
                        Log.e("zw"," success : " + baseData.toString());
                    }

                    @Override
                    public boolean operationError(MyOrderData baseData, int status, String message) {
                        Log.e("zw"," base : " + baseData.toString());

                        return super.operationError(baseData, status, message);
                    }
                });
    }

}
