package com.huawei.fusionchargeapp.views.interfaces;

import com.corelibs.base.BasePaginationView;
import com.huawei.fusionchargeapp.model.beans.InvoiceHistoryBean;

import java.util.List;

/**
 * Created by issuser on 2018/5/18.
 */

public interface InvoiceHistoryView extends BasePaginationView {
    void getInvoiceHistory(List<InvoiceHistoryBean> bean);
    void getInvoiceConsumeFailed();
}
