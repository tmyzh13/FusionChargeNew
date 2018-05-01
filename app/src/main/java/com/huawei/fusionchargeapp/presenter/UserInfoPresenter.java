package com.huawei.fusionchargeapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.corelibs.utils.PreferencesHelper;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.LoginApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.ModifyUserInfoRequestBean;
import com.huawei.fusionchargeapp.model.beans.ResponseMessageBean;
import com.huawei.fusionchargeapp.model.beans.UserInfoBean;
import com.huawei.fusionchargeapp.views.interfaces.UserInfoView;
import com.trello.rxlifecycle.ActivityEvent;


public class UserInfoPresenter extends BasePresenter<UserInfoView> {


    private LoginApi api;

    @Override
    public void onStart() {

    }

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api = ApiFactory.getFactory().create(LoginApi.class);
    }

    /**
     * 获取用户信息请求
     */
    public void doGetUserInfoRequest() {
        api.getUserInfo(PreferencesHelper.getData("token"))
                .compose(new ResponseTransformer<>(this.<BaseData<UserInfoBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<UserInfoBean>>(view) {
                    @Override
                    public void success(BaseData<UserInfoBean> baseData) {

                        Log.e("getuserinfo", baseData.data.toString());
                        if (baseData.code == 0) {
                            UserHelper.saveUserInfo(baseData.data);
                            view.onGetUserInfoSuccess(baseData.data);
                        } else {
                            view.onGetUsrInfoFail();
                        }
                    }
                    @Override
                    public boolean operationError(BaseData<UserInfoBean> userBeanBaseData, int status, String message) {

                        return super.operationError(userBeanBaseData, status, message);
                    }
                });
    }

    /**
     * 修改用户信息
     * @param userInfoRequest
     */
    public void doModifyUserInfo(ModifyUserInfoRequestBean userInfoRequest) {
        api.modifyUserInfo(PreferencesHelper.getData("token"),userInfoRequest)
                .compose(new ResponseTransformer<>(this.<BaseData<ResponseMessageBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<ResponseMessageBean>>(view) {
                    @Override
                    public void success(BaseData<ResponseMessageBean> baseData) {

                        if (baseData.code == 0) {
                            view.onModifySuccess();
                        } else {
                            view.onModifyUserInfoFail();
                        }
                    }
                    @Override
                    public boolean operationError(BaseData<ResponseMessageBean> userBeanBaseData, int status, String message) {

                        return super.operationError(userBeanBaseData, status, message);
                    }
                });
    }
}
