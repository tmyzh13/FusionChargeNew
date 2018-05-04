package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.PileFeeBean;

/**
 * Created by zhangwei on 2018/5/2.
 */

public interface ChargeOrderDetailView extends BaseView{

    public void onSuccess();

    public void onFail();
    void showPileFeeInfo(PileFeeBean bean);
}
