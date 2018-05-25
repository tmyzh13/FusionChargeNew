package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BaseView;
import com.huawei.fusionchargeapp.model.beans.ApplyInvoiceResultBean;
import com.huawei.fusionchargeapp.model.beans.RequestApplyInvoiceBean;

/**
 * Created by issuser on 2018/5/18.
 */

public interface ApplyInvoiceView extends BaseView{
    void applySuccess(ApplyInvoiceResultBean bean);
    void getUnpayInvoiceSucess(RequestApplyInvoiceBean bean);
    void repayInvoiceSucess(ApplyInvoiceResultBean bean);
}

