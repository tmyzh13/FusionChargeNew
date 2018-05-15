package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;

/**
 * Created by issuser on 2018/5/14.
 */

public interface W3AccountBindPhoneView extends BaseView {

    public void getVerCodeSuccess(String verCodePhone);

    public void getVerCodeFailed();
}
