package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;

/**
 * Created by issuser on 2018/5/17.
 */

public interface TaoCanPayView extends BaseView {


      void paySuccess(PayResultBean bean);
}
