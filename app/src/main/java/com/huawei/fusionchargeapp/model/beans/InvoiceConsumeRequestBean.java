package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by admin on 2018/5/15.
 */

public class InvoiceConsumeRequestBean {
    public int page,rp;
    public Condition condition = new Condition();

    public void setCondition(long id){
        condition.appUserId = id;
    }

    public class Condition{
        public long appUserId;
    }
}
