package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeBean;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;

import java.util.List;

/**
 * Created by zhangwei on 2018/5/24.
 */

public interface ChargeDetails2View extends BaseView {

    void onGetChargeStationDetailSuccess(ChargeStationDetailBean chargeStationDetailBean, List<ChargeDetailFeeBean> feeList);

    void onGetChargeStationDetailFail(String message);
}
