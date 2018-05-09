package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;

/**
 * Created by zhangwei on 2018/4/30.
 */

public interface ChargeInputNumberView extends BaseView {

    public void onGetDataSuccess(ScanChargeInfo info);

    public void onGetDataFail(String msg);
}
