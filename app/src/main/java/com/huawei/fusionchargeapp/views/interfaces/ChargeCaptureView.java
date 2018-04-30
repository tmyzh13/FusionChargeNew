package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;

/**
 * Created by zhangwei on 2018/4/30.
 */

public interface ChargeCaptureView extends BaseView{

    public void onSuccess(ScanChargeInfo info);

    public void onOperationError(String msg);

    public void onError();

}
