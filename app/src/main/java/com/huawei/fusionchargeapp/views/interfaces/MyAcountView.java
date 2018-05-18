package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.BalanceBean;

/**
 * Created by issuser on 2018/5/18.
 */

public interface MyAcountView extends BaseView{
    void renderBalance(BalanceBean bean);
}
