package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.ApplyInvoiceResultBean;

/**
 * Created by issuser on 2018/5/18.
 */

public interface ApplyInvoiceView extends BaseView{
    void applySuccess(ApplyInvoiceResultBean bean);
}

