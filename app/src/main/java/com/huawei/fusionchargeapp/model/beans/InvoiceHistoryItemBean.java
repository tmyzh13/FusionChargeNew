package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by issuser on 2018/5/18.
 */

public class InvoiceHistoryItemBean {
    public String startEndTime;
    public int count;
    public Detail detail;
    public class Detail{
        public int id,payType,status;
        public String orderNumber,createTime,type,title,code,content,name,phone,email,province,city,district
                ,recArea,recAddr,postage,kpRemark,weekDay,earlyTime,endTime;
        public long appUserId;
        public double amount;
    }
}
