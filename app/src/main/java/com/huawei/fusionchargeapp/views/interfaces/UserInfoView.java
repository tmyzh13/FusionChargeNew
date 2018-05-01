package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.UserInfoBean;

public interface UserInfoView extends BaseView{
    void onGetUserInfoSuccess(UserInfoBean userInfoBean);

    void onGetUsrInfoFail();

    void onModifyUserInfoFail();

    void onModifySuccess();
}
