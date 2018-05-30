package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;

/**
 * Created by issuser on 2018/5/30.
 */

public interface CashPatternView extends BaseView {

    void paySuccess(PayResultBean bean);
}
