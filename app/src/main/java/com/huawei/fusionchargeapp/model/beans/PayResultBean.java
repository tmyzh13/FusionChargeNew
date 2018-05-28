package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by issuser on 2018/5/28.
 */

public class PayResultBean {
    public String orderInfoId;
    public String orderInfo;
//    payType:支付类型，（1：支付宝、2：微信、3：余额、4：华为工号支付、5：套餐、6：到付）
    public int type;
    public String partnerid;
//    isNeedPay:是否需要支付（0：不需要，1：需要）/**/
    public int isNeedPay;

}
