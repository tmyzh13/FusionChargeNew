package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;

/**
 * Created by issuser on 2018/4/19.
 */

public interface LoginView extends BaseView{
    void loginSuccess();
    void registerSuccess();
    void registerFailure();
    void getCodeSuccess();
    void loginFailure();
    void checkCodeSuccess();
}
