package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestIAdminRegister;
import com.huawei.fusionchargeapp.model.beans.RequestIAdminVerCode;
import com.huawei.fusionchargeapp.model.beans.RequestIadminLoginBean;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.W3AccountBindPhoneView;
import com.trello.rxlifecycle.ActivityEvent;

/**
 * Created by issuser on 2018/5/14.
 */

public class W3AccountBindPhonePresenter extends BasePresenter<W3AccountBindPhoneView> {


    private ScanApi api;

    private static final String HUAWEI_TYPE = "1";

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(ScanApi.class);
    }

    @Override
    public void onStart() {

    }

    public void getVerCode(final String phone) {

        RequestIAdminVerCode verCode = new RequestIAdminVerCode();
        verCode.setType(HUAWEI_TYPE);
        verCode.setPhone(phone);
        api.iAdmingetVerCode(verCode)
                .compose(new ResponseTransformer<>(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData>(view) {
                    @Override
                    public void success(BaseData baseData) {
                        view.getVerCodeSuccess(phone);
                    }

                    @Override
                    public boolean operationError(BaseData baseData, int status, String message) {
                        Log.e("zw","operationError");
                        Log.e("zw","operationError : " + baseData.toString());
                        view.getVerCodeFailed(phone,status);
                        return super.operationError(baseData, status, message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("zw","onError : " + e.getMessage());
                    }
                });

    }

    public void iAdminRegister(String verCode,String phone,String w3Account) {
        RequestIAdminRegister bean = new RequestIAdminRegister();
        bean.setCaptcha(verCode);
        bean.setJobNumber(w3Account);
        bean.setPhone(phone);

        api.iAdminRegisger(bean)
                .compose(new ResponseTransformer<>(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData>(view) {
                    @Override
                    public void success(BaseData baseData) {
                        Log.e("zw","success");
                        Log.e("zw","success : " + baseData.toString());
                        view.registerSuccess();
                    }

                    @Override
                    public boolean operationError(BaseData baseData, int status, String message) {
                        Log.e("zw","operationError");
                        Log.e("zw","operationError : " + baseData.toString());
                        view.registerFail();
                        return super.operationError(baseData, status, message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("zw","onError : " + e.getMessage());
                    }
                });
    }
}
