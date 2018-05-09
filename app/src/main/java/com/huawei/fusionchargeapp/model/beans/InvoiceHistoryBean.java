package com.huawei.fusionchargeapp.model.beans;

import java.util.List;

/**
 * Created by admin on 2018/5/9.
 */

public class InvoiceHistoryBean {
    public String time,status,sort,money;
    public String receiveAddr,invoiceHead,invoiceContent;
    public long taxNum;
    public int invoiceNum,eleNum;
    public List<ElectronicConsumeBean> eleList;
}
