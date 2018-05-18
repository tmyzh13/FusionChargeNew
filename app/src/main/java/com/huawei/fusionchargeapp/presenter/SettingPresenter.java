package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.LoginApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestLogoutBean;
import com.huawei.fusionchargeapp.views.interfaces.SettingView;
import com.trello.rxlifecycle.ActivityEvent;

public class SettingPresenter extends BasePresenter<SettingView> {


    private LoginApi api;

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(LoginApi.class);
    }

    @Override
    public void onStart() {

    }

    public void logout(String userId){
        RequestLogoutBean bean = new RequestLogoutBean();
        bean.setAppUserId(userId);
        api.logOut(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData>(view) {
                    @Override
                    public void success(BaseData baseData) {
                        Log.e("zw","logout presenter : " + baseData.toString());
                        view.onLogoutSuccess();
                    }

                    @Override
                    public boolean operationError(BaseData baseData, int status, String message) {
                        view.onLogoutFail();
                        return super.operationError(baseData, status, message);
                    }
                });
    }
}
