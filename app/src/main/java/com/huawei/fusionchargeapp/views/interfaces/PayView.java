package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.MyTaocanBean;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.PayResultBean;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;

import java.util.List;

/**
 * Created by issuser on 2018/4/25.
 */

public interface PayView  extends BaseView{
    void renderData(PayInfoBean bean);
    void paySuccess(PayResultBean bean);
    void payBalanceNotEnough();
    void payFail();
    void renderMyTaoCan(MyTaocanBean bean);
}
