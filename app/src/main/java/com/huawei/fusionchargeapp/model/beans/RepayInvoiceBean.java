package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by issuser on 2018/5/25.
 */

public class RepayInvoiceBean {
    public String orderRecordNum; //发票订单号,
    public int payType;//支付类型（1：支付宝、2：微信、3：余额、4：华为工号支付、5：套餐、6：到付）,
    public double postage = 10;//10.00(邮费，到付和大于200传0.00)
}