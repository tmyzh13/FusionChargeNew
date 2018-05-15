package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BasePaginationView;
import com.huawei.fusionchargeapp.model.beans.ApplyInvoiceBean;

import java.util.List;

/**
 * Created by admin on 2018/5/15.
 */

public interface InvoiceView extends BasePaginationView {
    void getInvoiceConsume(List<ApplyInvoiceBean> bean);
    void getInvoiceConsumeFailed();
}
