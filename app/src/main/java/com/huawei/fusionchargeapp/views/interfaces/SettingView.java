package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;


public interface SettingView extends BaseView{

    void onLogoutSuccess();

    void onLogoutFail(String message);
  
}
