package com.huawei.fusionchargeapp.model.beans;

import java.util.List;

/**
 * Created by admin on 2018/5/9.
 */

public class InvoiceHistoryBean {
    public String createTime,amount;
    public int status;//1已开票，0未开票
    public int id;
    public int payStatus=1;//0未支付
    public String orderNumber;
}
